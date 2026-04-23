@FilterDefs({
        @FilterDef(
                name = "tenantFilter",
                parameters = @org.hibernate.annotations.ParamDef(name = "tenantId", type = String.class),
                defaultCondition = "tenant_id = :tenantId"
        ),
        @FilterDef(
                name = "deletedFilter",
                defaultCondition = "deleted = false"
        )
})

package com.rafael.agendanails.webapp.domain.model;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;