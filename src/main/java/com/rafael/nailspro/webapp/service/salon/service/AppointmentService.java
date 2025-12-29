package com.rafael.nailspro.webapp.service.salon.service;

import com.rafael.nailspro.webapp.model.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.model.dto.appointment.TimeInterval;
import com.rafael.nailspro.webapp.model.entity.Appointment;
import com.rafael.nailspro.webapp.model.entity.AppointmentAddOn;
import com.rafael.nailspro.webapp.model.entity.SalonService;
import com.rafael.nailspro.webapp.model.entity.user.Client;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.model.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.service.admin.client.AdminClientService;
import com.rafael.nailspro.webapp.service.client.ClientService;
import com.rafael.nailspro.webapp.service.professional.ProfessionalService;
import com.rafael.nailspro.webapp.service.professional.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final SalonServiceService salonService;
    private final WorkScheduleService workScheduleService;
    private final ProfessionalService professionalService;
    private final AdminClientService adminClientService;
    private final ClientService clientService;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public void createAppointment(AppointmentCreateDTO dto, UserPrincipal principal) {
        List<AppointmentAddOn> addOnServices = mapAddOns(dto.addOnsIds());
        SalonService mainService = findService(dto.mainServiceId());

        TimeInterval interval = calculateIntervalAndBuffer(dto, principal, mainService, addOnServices);

        workScheduleService.checkProfessionalAvailability(UUID.fromString(dto.professionalExternalId()), interval);
        professionalService.checkIfProfessionalHasTimeConflicts(UUID.fromString(dto.professionalExternalId()), interval);

        Appointment appointment = buildAppointment(dto, principal.getUserId(), interval, mainService, addOnServices);
        repository.save(appointment);
    }

    private TimeInterval calculateIntervalAndBuffer(AppointmentCreateDTO dto,
                                                                    UserPrincipal principal,
                                                                    SalonService mainService,
                                                                    List<AppointmentAddOn> addOnServices) {

        int totalDurationInMinutes = calculateAppointmentMinutage(mainService, addOnServices);

        Integer salonBufferTime = salonService.getSalonBufferTime(principal.getTenantId());

        LocalDateTime start = dto.appointmentDate();
        LocalDateTime realEnd = start.plusMinutes(totalDurationInMinutes);
        LocalDateTime endWithBuffer = realEnd.plusMinutes(salonBufferTime);

        return TimeInterval.builder()
                .realStart(start)
                .realEnd(realEnd)
                .endWithBuffer(endWithBuffer)
                .build();
    }

    private Appointment buildAppointment(AppointmentCreateDTO dto,
                                         Long clientId,
                                         TimeInterval interval,
                                         SalonService mainService,
                                         List<AppointmentAddOn> addOnServices) {

        Appointment appointment = Appointment.builder()
                .appointmentStatus(AppointmentStatus.PENDING)
                .startDate(interval.realStart())
                .endDate(interval.realEnd())
                .client(findClient(clientId))
                .professional(findProfessional(dto.professionalExternalId()))
                .mainSalonService(mainService)
                .addOns(addOnServices)
                .observations(dto.observation().get())
                .build();

        appointment.setTotalValue(appointment.calculateTotalValue());
        appointment.setEndDate(interval.realEnd());
        return appointment;
    }

    private static int calculateAppointmentMinutage(SalonService mainService, List<AppointmentAddOn> addOnServices) {
        return mainService.getDurationMinutes() + addOnServices.stream()
                .mapToInt(addon -> addon.getService().getDurationMinutes())
                .sum();
    }

    public Client findClient(Long clientId) {

        return adminClientService.getClient(clientId);
    }

    public Professional findProfessional(String professionalExternalId) {

        return professionalService.findByExternalId(professionalExternalId);
    }

    public SalonService findService(Long serviceId) {

        return salonService.findById(serviceId);
    }

    public List<AppointmentAddOn> mapAddOns(List<Long> addOnIds) {

        return salonService.findAddOns(addOnIds);
    }

    public void cancelAppointment(Long appointmentId) {

        appointmentRepository.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED);
    }

    @Transactional
    public void cancelAppointmentAndFlagClient(Long clientId,
                                               Long appointmentId) {

        clientService.incrementClientCancelledAppointments(clientId);
        cancelAppointment(appointmentId);
    }
}
