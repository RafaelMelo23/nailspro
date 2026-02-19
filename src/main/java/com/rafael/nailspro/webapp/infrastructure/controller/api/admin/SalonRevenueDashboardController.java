package com.rafael.nailspro.webapp.infrastructure.controller.api.admin;

import com.rafael.nailspro.webapp.application.admin.dashboard.SalonRevenueDashboardService;
import com.rafael.nailspro.webapp.infrastructure.dto.dashboard.SalonDashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard/salon")
@RequiredArgsConstructor
public class SalonRevenueDashboardController {

    private final SalonRevenueDashboardService dashboardService;

    @GetMapping("/revenue")
    public ResponseEntity<SalonDashboardDTO> getMonthlyRevenue() {
        SalonDashboardDTO dashboard = dashboardService.getMonthlySalonRevenue();
        return ResponseEntity.ok(dashboard);
    }
}