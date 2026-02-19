package com.rafael.nailspro.webapp.application.admin.dashboard;

import com.rafael.nailspro.webapp.domain.model.SalonDailyRevenue;
import com.rafael.nailspro.webapp.domain.repository.SalonDailyRevenueRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.dashboard.DailyRevenueDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.dashboard.SalonDashboardDTO;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import com.rafael.nailspro.webapp.shared.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonRevenueDashboardService {

    private final SalonDailyRevenueRepository repository;

    public SalonDashboardDTO getMonthlySalonRevenue() {
        var now = LocalDate.now();
        var thirtyDaysAgo = now.minusDays(30);
        var sevenDaysAgo = now.minusDays(7);

        List<SalonDailyRevenue> dailyRevenues = repository.findByTenantIdAndDateBetween(TenantContext.getTenant(), thirtyDaysAgo, now)
                .orElseThrow(() -> new BusinessException("Não foi possível encontrar métricas para este salão."));

        BigDecimal monthlyRevenue = calculateRevenue(dailyRevenues, thirtyDaysAgo, now);
        BigDecimal weeklyRevenue = calculateRevenue(dailyRevenues, sevenDaysAgo, now);
        Long appointmentsCount = (long) dailyRevenues.size();
        BigDecimal averageTicket = calculateAvgTicket(monthlyRevenue, appointmentsCount);

        return SalonDashboardDTO.builder()
                .monthlyRevenue(monthlyRevenue)
                .weeklyRevenue(weeklyRevenue)
                .averageTicket(averageTicket)
                .monthlyAppointmentsCount(appointmentsCount)
                .chartData(mapToChartData(dailyRevenues))
                .build();
    }

    private List<DailyRevenueDTO> mapToChartData(List<SalonDailyRevenue> dailyRevenues) {

        return dailyRevenues.stream()
                .map(drv -> DailyRevenueDTO.builder()
                        .date(drv.getDate())
                        .value(drv.getTotalRevenue())
                        .appointmentsCount(drv.getAppointmentsCount())
                        .build()
                )
                .toList();
    }

    private BigDecimal calculateAvgTicket(BigDecimal total, Long count) {

        return total.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateRevenue(List<SalonDailyRevenue> dailyRevenues, LocalDate start, LocalDate end) {
        return dailyRevenues.stream()
                .filter(drv -> drv.getDate().isAfter(start) && drv.getDate().isBefore(end))
                .map(SalonDailyRevenue::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}