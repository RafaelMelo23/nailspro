package com.rafael.nailspro.webapp.service.salon.service;

import com.rafael.nailspro.webapp.model.dto.salon.service.SalonServiceDTO;
import com.rafael.nailspro.webapp.model.dto.salon.service.SalonServiceOutDTO;
import com.rafael.nailspro.webapp.model.entity.AppointmentAddOn;
import com.rafael.nailspro.webapp.model.entity.SalonService;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.repository.AddOnRepository;
import com.rafael.nailspro.webapp.model.repository.SalonProfileRepository;
import com.rafael.nailspro.webapp.model.repository.SalonServiceRepository;
import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import com.rafael.nailspro.webapp.service.infra.mapper.SalonServiceMapper;
import com.rafael.nailspro.webapp.service.professional.ProfessionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalonServiceService {

    private final SalonServiceRepository salonServiceRepository;
    private final ProfessionalService professionalService;
    private final SalonServiceMapper salonServiceMapper;
    private final AddOnRepository addOnRepository;
    private final SalonProfileRepository profileRepository;

    public SalonService findById(Long id) {

        return salonServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalonService not found"));
    }

    public Set<SalonService> findByIdIn(List<Long> ids) {

        return salonServiceRepository.findByIdIn(ids)
                .orElseThrow(() -> new RuntimeException("SalonService not found"));
    }

    public List<AppointmentAddOn> findAddOns(List<Long> addOnIds) {

        return addOnRepository.findAllById(addOnIds);
    }
    @Transactional
    public void createService(SalonServiceDTO salonServiceDTO) {

        Set<Professional> professionalsThatOfferSuchService = getProfessionals(salonServiceDTO);

        SalonService builtService = salonServiceMapper.buildSalonService(salonServiceDTO, professionalsThatOfferSuchService);
        salonServiceRepository.save(builtService);
    }

    private Set<Professional> getProfessionals(SalonServiceDTO salonServiceDTO) {
        Set<Professional> professionalsThatOfferSuchService = new HashSet<>();

        if (salonServiceDTO.professionals().isPresent()) {
            professionalsThatOfferSuchService = professionalService.findAllById
                    (salonServiceDTO.professionals().get());
        }
        return professionalsThatOfferSuchService;
    }

    public List<SalonServiceOutDTO> getServices() {
        List<SalonService> services = salonServiceRepository.findAll();
        return salonServiceMapper.mapToOutDTOList(services);
    }

    @Transactional
    public void changeSalonServiceVisibility(Long id, Boolean active) {

        salonServiceRepository.changeSalonServiceVisibility(id, active);
    }

    @Transactional
    public void deleteSalonService(Long id) {

        salonServiceRepository.deleteService(id);
    }

    @Transactional
    public void updateSalonService(Long serviceId, SalonServiceDTO dto) {
        SalonService service = findById(serviceId);

        if (dto.name() != null) {
            service.setName(dto.name());
        }

        if (dto.description() != null) {
            service.setDescription(dto.description());
        }

        if (dto.durationMinutes() != null) {
            service.setDurationMinutes(dto.durationMinutes());
        }

        if (dto.value() != null) {
            service.setValue(dto.value());
        }

        dto.professionals().ifPresent(newIds -> {

            service.getProfessionals().removeIf(p -> !newIds.contains(p.getId()));

            Set<Long> currentIds = service.getProfessionals().stream()
                    .map(Professional::getId)
                    .collect(Collectors.toSet());

            List<Long> idsToAdd = newIds.stream()
                    .filter(id -> !currentIds.contains(id))
                    .toList();

            if (!idsToAdd.isEmpty()) {
                Set<Professional> professionalsToAdd = professionalService.findAllById(idsToAdd);
                service.getProfessionals().addAll(professionalsToAdd);
            }
        });
    }

    public Integer getSalonBufferTime(String tenantId) {

        return profileRepository.findSalonProfileAppointmentBufferMinutesByTenantId(tenantId)
                .orElseThrow(() -> new BusinessException("Salão não encontrado."));
    }
}
