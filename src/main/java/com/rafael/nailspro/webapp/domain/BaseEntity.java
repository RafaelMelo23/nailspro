package com.rafael.nailspro.webapp.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@SuperBuilder
@Getter
@MappedSuperclass
@FilterDef(
        name = "tenantFilter",
        parameters = @ParamDef(name = "tenantId", type = String.class)
)
@Filter(name = "tenantFilter",
        condition = "tenant_Id = :tenantId"
)
public class BaseEntity {

    @Column(name = "tenant_Id", nullable = false, updatable = false)
    private String tenantId;

    @PrePersist
    public void prePersist() {
        this.tenantId = TenantContext.getTenant();
    }
}