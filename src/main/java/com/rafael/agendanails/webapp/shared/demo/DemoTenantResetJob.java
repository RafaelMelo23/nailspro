package com.rafael.agendanails.webapp.shared.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoTenantResetJob {

    private final DataSource dataSource;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void resetDemoTenantData() {
        log.info("[DEMO-RESET] Iniciando limpeza e re-população do tenant demo...");

        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("scripts/demo-salon-data.sql"));
            populator.setSqlScriptEncoding("UTF-8");

            populator.execute(dataSource);

            log.info("[DEMO-RESET] O ambiente demo foi restaurado para o estado inicial com sucesso.");
        } catch (Exception e) {
            log.error("[DEMO-RESET] Falha ao executar o script de reset: {}", e.getMessage());
        }
    }
}