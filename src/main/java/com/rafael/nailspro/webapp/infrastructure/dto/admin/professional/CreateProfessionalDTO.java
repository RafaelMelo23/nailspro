package com.rafael.nailspro.webapp.infrastructure.dto.admin.professional;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateProfessionalDTO(Long id,
                                    String fullName,
                                    String email,
                                    List<Long> servicesOfferedByProfessional
) {}