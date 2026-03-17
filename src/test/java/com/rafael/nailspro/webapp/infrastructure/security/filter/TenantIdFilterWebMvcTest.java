package com.rafael.nailspro.webapp.infrastructure.security.filter;

import com.rafael.nailspro.webapp.application.appointment.message.schedule.AppointmentReminderJob;
import com.rafael.nailspro.webapp.infrastructure.controller.TestController;
import com.rafael.nailspro.webapp.shared.tenant.TenantResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TestController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(TenantIdFilter.class)
class TenantIdFilterWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TenantResolver tenantResolver;
    @MockitoBean
    private ApplicationEventPublisher publisher;
    @MockitoBean
    private AppointmentReminderJob appointmentReminderJob;

    @Test
    void placeholder() {
        // TODO: add tests for tenant enforcement and bypass paths
    }
}