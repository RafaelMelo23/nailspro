package com.rafael.nailspro.webapp.service.professional;

import com.rafael.nailspro.webapp.model.dto.professional.schedule.WorkScheduleRecordDTO;
import com.rafael.nailspro.webapp.model.entity.professional.WorkSchedule;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.repository.WorkScheduleRepository;
import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository repository;
    private final ProfessionalService professionalService;

    @Transactional
    public void registerSchedules(Set<WorkScheduleRecordDTO> workScheduleDTO, Long professionalId) {
        Set<WorkSchedule> schedulesToBeSaved = new HashSet<>();

        Professional professional = professionalService.findById(professionalId);

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
                    .startTime(sch.startTime())
                    .endTime(sch.endTime())
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
                        wsc.getStartTime(),
                        wsc.getEndTime(),
                        wsc.getLunchBreakStartTime(),
                        wsc.getLunchBreakEndTime(),
                        wsc.getIsActive()
                ))
                .collect(Collectors.toSet());
    }
}