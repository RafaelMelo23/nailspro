package com.rafael.nailspro.webapp.application.professional;

import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.domain.model.WorkSchedule;
import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.domain.repository.WorkScheduleRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.date.TimeInterval;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository repository;
    private final ProfessionalRepository professionalRepository;

    @Transactional
    public void registerSchedules(List<WorkScheduleRecordDTO> workScheduleDTO, Long professionalId) {
        Professional professional = professionalRepository.findById(professionalId).orElseThrow(() -> new BusinessException("Professional não encontrado(a)"));

        Set<DayOfWeek> professionalCurrentSchedule = getProfessionalCurrentSchedule(professional);

        Set<WorkScheduleRecordDTO> schedules = workScheduleDTO.stream()
                .filter(ws -> !professionalCurrentSchedule.contains(ws.dayOfWeek()))
                .collect(Collectors.toSet());

        List<WorkSchedule> toSave = schedules
                .stream()
                .map((WorkScheduleRecordDTO dto) ->
                        WorkSchedule.builder()
                                .dayOfWeek(dto.dayOfWeek())
                                .workStart(dto.startTime())
                                .workEnd(dto.endTime())
                                .lunchBreakStartTime(dto.lunchBreakStartTime())
                                .lunchBreakEndTime(dto.lunchBreakEndTime())
                                .isActive(true)
                                .professional(professional)
                                .build())
                .collect(Collectors.toList());

        try {
            repository.saveAll(toSave);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Erro: Você já possui horários cadastrados para um ou mais dos dias selecionados.");
        }
    }

    private Set<DayOfWeek> getProfessionalCurrentSchedule(Professional professional) {
        return professional.getWorkSchedules().stream().map(WorkSchedule::getDayOfWeek).collect(Collectors.toSet());
    }

    @Transactional
    public void modifyWeekSchedule(List<WorkScheduleRecordDTO> dtos, Long professionalId) {
        for (WorkScheduleRecordDTO dto : dtos) {
            if (dto.id() == null) {
                throw new BusinessException("ID é obrigatório para atualizar um horário existente.");
            }

            List<WorkSchedule> workSchedules = repository.findAllById(dtos.stream().map(WorkScheduleRecordDTO::id).toList());

            if (!workSchedules.stream().allMatch(sch -> professionalId.equals(sch.getProfessional().getId()))) {
                throw new BusinessException("Essa operação não é permitida ou não foi encontrada");
            }

            Map<Long, WorkScheduleRecordDTO> dtoMap = dtos.stream()
                    .collect(Collectors.toMap(WorkScheduleRecordDTO::id, dtoToMap -> dtoToMap));

            workSchedules.forEach(ws -> {
                WorkScheduleRecordDTO wsrDTO = dtoMap.get(ws.getId());
                ws.updateFromDto(wsrDTO);
            });
        }
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long professionalId) {

        WorkSchedule schedule = repository.findById(scheduleId).orElseThrow(() -> new BusinessException("Horário não encontrado no sistema."));

        if (!Objects.equals(schedule.getProfessional().getId(), professionalId)) {
            throw new BusinessException("Essa operação não é permitida.");
        }

        repository.delete(schedule);
    }

    public Set<WorkScheduleRecordDTO> getWorkSchedules(Long userId) {
        List<WorkSchedule> schedules = repository.findByProfessional_Id(userId);

        if (schedules.isEmpty()) {
            throw new BusinessException("Nenhum cronograma de trabalho encontrado para este profissional.");
        }

        return schedules.stream().map(wsc -> new WorkScheduleRecordDTO(wsc.getId(), wsc.getDayOfWeek(), wsc.getWorkStart(), wsc.getWorkEnd(), wsc.getLunchBreakStartTime(), wsc.getLunchBreakEndTime(), wsc.getIsActive())).collect(Collectors.toSet());
    }

    public void checkProfessionalAvailability(UUID professionalExternalId, TimeInterval interval) {
        if (!repository.checkIfProfessionalIsAvailable(professionalExternalId, interval.getStartTimeOnly(), interval.getEndTimeOnly(), interval.getDayOfWeek())) {
            throw new BusinessException("O profissional não está disponível neste período.");
        }
    }
}