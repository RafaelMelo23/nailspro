package com.rafael.nailspro.webapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;


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