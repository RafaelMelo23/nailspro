package com.rafael.nailspro.webapp.application.professional;

import com.rafael.nailspro.webapp.domain.appointment.TimeInterval;
import com.rafael.nailspro.webapp.domain.professional.ProfessionalDomainService;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.domain.professional.WorkSchedule;
import com.rafael.nailspro.webapp.domain.user.Professional;
import com.rafael.nailspro.webapp.domain.professional.WorkScheduleRepository;
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
    private final ProfessionalDomainService professionalDomainService;

    @Transactional
    public void registerSchedules(List<WorkScheduleRecordDTO> workScheduleDTO, Long professionalId) {
        Set<WorkSchedule> schedulesToBeSaved = new HashSet<>();

        Professional professional = professionalDomainService.findById(professionalId);

        Set<DayOfWeek> professionalCurrentSchedule = getProfessionalCurrentSchedule(professional);

        Set<WorkScheduleRecordDTO> schedules = workScheduleDTO.stream()
                .filter(ws -> !professionalCurrentSchedule.contains(ws.dayOfWeek()))
                .collect(Collectors.toSet());

        schedules.forEach(sch -> {
            if (sch.endTime().isBefore(sch.startTime())) {
                throw new BusinessException("Horário de término não pode ser menor que o de início na " + sch.dayOfWeek());
            }

            schedulesToBeSaved.add(WorkSchedule.builder()
                    .dayOfWeek(sch.dayOfWeek())
                    .workStart(sch.startTime())
                    .workEnd(sch.endTime())
                    .lunchBreakStartTime(sch.lunchBreakStartTime())
                    .lunchBreakEndTime(sch.lunchBreakEndTime())
                    .isActive(true)
                    .professional(professional)
                    .build());
        });

        try {
            repository.saveAll(schedulesToBeSaved);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Erro: Você já possui horários cadastrados para um ou mais dos dias selecionados.");
        }
    }

    private Set<DayOfWeek> getProfessionalCurrentSchedule(Professional professional) {
         return professional.getWorkSchedules().stream()
                .map(WorkSchedule::getDayOfWeek)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void modifyWeekSchedule(List<WorkScheduleRecordDTO> dtos, Long professionalId) {
        for (WorkScheduleRecordDTO dto : dtos) {
            if (dto.id() == null) {
                throw new BusinessException("ID é obrigatório para atualizar um horário existente.");
            }

            WorkSchedule schedule = repository.findById(dto.id())
                    .orElseThrow(() -> new BusinessException("Horário não encontrado no sistema."));

            if (!Objects.equals(schedule.getProfessional().getId(), professionalId)) {
                throw new BusinessException("Essa operação não é permitida.");
            }

            schedule.updateFromDto(dto);
            repository.save(schedule);
        }
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long professionalId) {

        WorkSchedule schedule = repository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException("Horário não encontrado no sistema."));

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

        return schedules.stream()
                .map(wsc -> new WorkScheduleRecordDTO(
                        wsc.getId(),
                        wsc.getDayOfWeek(),
                        wsc.getWorkStart(),
                        wsc.getWorkEnd(),
                        wsc.getLunchBreakStartTime(),
                        wsc.getLunchBreakEndTime(),
                        wsc.getIsActive()
                ))
                .collect(Collectors.toSet());
    }

    public void checkProfessionalAvailability(UUID professionalExternalId, TimeInterval interval) {
        if (!repository.checkIfProfessionalIsAvailable(
                professionalExternalId,
                interval.getStartTimeOnly(),
                interval.getEndTimeOnly(),
                interval.getDayOfWeek())) {
            throw new BusinessException("O profissional não está disponível neste período.");
        }
    }
}