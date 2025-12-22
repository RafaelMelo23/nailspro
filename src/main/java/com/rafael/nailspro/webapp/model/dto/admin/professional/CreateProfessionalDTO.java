package com.rafael.nailspro.webapp.model.dto.admin.professional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rafael.nailspro.webapp.model.dto.salon.service.SalonServiceDTO;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateProfessionalDTO(Long id,
                                    String fullName,
                                    String email,
                                    List<Long> servicesOfferedByProfessional
) {}