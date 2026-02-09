package com.rafael.nailspro.webapp.application.admin.salon.profile;

import com.rafael.nailspro.webapp.domain.model.SalonProfile;
import com.rafael.nailspro.webapp.domain.repository.SalonProfileRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.admin.salon.profile.SalonProfileDTO;
import com.rafael.nailspro.webapp.infrastructure.files.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AdminSalonProfileService {

    private final SalonProfileRepository salonProfileRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public void createOrUpdateProfile(Long ownerId, SalonProfileDTO profile) throws IOException {

        SalonProfile salonProfile = salonProfileRepository.findByOwner_Id(ownerId)
                .orElse(new SalonProfile());

        if (profile.tradeName() != null) {
            salonProfile.setTradeName(profile.tradeName());
        }
        if (profile.slogan() != null) {
            salonProfile.setSlogan(profile.slogan());
        }
        if (profile.primaryColor() != null) {
            salonProfile.setPrimaryColor(profile.primaryColor());
        }

        if (profile.logoBase64() != null) {
            if (salonProfile.getLogoPath() != null) {
                deleteSalonLogo();
            }
            salonProfile.setLogoPath(fileUploadService.uploadBase64Image(profile.logoBase64()));
        }
        if (profile.comercialPhone() != null) {
            salonProfile.setComercialPhone(profile.comercialPhone());
        }
        if (profile.fullAddress() != null) {
            salonProfile.setFullAddress(profile.fullAddress());
        }
        if (profile.socialMediaLink() != null) {
            salonProfile.setSocialMediaLink(profile.socialMediaLink());
        }
        if (profile.warningMessage() != null) {
            salonProfile.setWarningMessage(profile.warningMessage());
        }
        if (profile.status() != null) {
            salonProfile.setOperationalStatus(profile.status());
        }
        if (profile.appointmentBufferMinutes() != null) {
            salonProfile.setAppointmentBufferMinutes(profile.appointmentBufferMinutes());
        }
    }

    public void deleteSalonLogo() throws IOException {

        SalonProfile salonProfile = salonProfileRepository.findById(1L)
                .orElse(new SalonProfile());

        fileUploadService.deleteFile(salonProfile.getLogoPath());
    }


}
