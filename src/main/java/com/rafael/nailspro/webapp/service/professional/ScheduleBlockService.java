package com.rafael.nailspro.webapp.service.professional;

import com.rafael.nailspro.webapp.model.dto.professional.schedule.block.ScheduleBlockDTO;
import com.rafael.nailspro.webapp.model.dto.professional.schedule.block.ScheduleBlockOutDTO;
import com.rafael.nailspro.webapp.model.entity.professional.ScheduleBlock;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.repository.ScheduleBlockRepository;
import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleBlockService {

    private final ScheduleBlockRepository repository;
    private final ProfessionalService professionalService;
    @Autowired
    EntityManager entityManager;

    public void createBlock(ScheduleBlockDTO blockDTO, Long professionalId) {
        Professional professional = professionalService.findById(professionalId);

        ScheduleBlock block = ScheduleBlock.builder()
                .reason(blockDTO.reason())
                .professional(professional)
                .isWholeDayBlocked(blockDTO.isWholeDayBlocked())
                .dateAndStartTime(blockDTO.dateAndStartTime())
                .dateAndEndTime(blockDTO.dateAndEndTime())
                .build();

        if (!block.getIsWholeDayBlocked() && block.getDateAndStartTime() == null) {
            throw new BusinessException("Data e hora de início são obrigatórias para bloqueios totais.");
        }

        repository.save(block);
    }

    public void deleteBlock(Long professionalId, Long blockId) {

        repository.findById(blockId).ifPresent(sb -> {
            if (sb.getProfessional().getId().equals(professionalId)) {
                repository.delete(sb);
            }
        });
    }

    public List<ScheduleBlockOutDTO> getBlocks(Long userId, Optional<LocalDateTime> from) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduleBlock> cq = cb.createQuery(ScheduleBlock.class);
        Root<ScheduleBlock> root = cq.from(ScheduleBlock.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(
                cb.equal(
                        root.get("professional").get("id"),
                        userId
                )
        );

        from.ifPresent(localDateTime -> predicates.add(
                cb.greaterThanOrEqualTo(
                        root.get("dateAndStartTime"),
                        localDateTime
                )
        ));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        List<ScheduleBlock> resultList = entityManager.createQuery(cq).getResultList();

        return resultList.stream()
                .map(sb -> ScheduleBlockOutDTO.builder()
                        .id(sb.getId())
                        .dateAndStartTime(sb.getDateAndStartTime())
                        .dateAndEndTime(sb.getDateAndEndTime())
                        .isWholeDayBlocked(sb.getIsWholeDayBlocked())
                        .professionalId(sb.getProfessional().getId())
                        .reason(sb.getReason())
                        .build()
                ).collect(Collectors.toList());
    }
}
