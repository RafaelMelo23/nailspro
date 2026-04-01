package com.rafael.agendanails.webapp.infrastructure.controller.pages;

import com.rafael.agendanails.webapp.application.salon.business.SalonProfileService;
import com.rafael.agendanails.webapp.domain.model.SalonProfile;
import com.rafael.agendanails.webapp.shared.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PagesController {

    private void applySalonProfileToModel(Model model) {
        SalonProfile salon = salonProfileService.getByTenantIdElseNull(TenantContext.getTenant());
        if (salon != null) {
            model.addAttribute("salon", salon);
        }
    }

    private final SalonProfileService salonProfileService;

    @RequestMapping("/redefinir-senha")
    public String redefinirSenha(@RequestParam String resetToken,
                                 Model model) {

        model.addAttribute("resetToken", resetToken);
        return "redefinir-senha";
    }

    @RequestMapping("/offline")
    public String renderMaintenanceHtml(Model model) {
        String salonOperationalMessage =
                salonProfileService.getSalonOperationalMessageByTenantId(TenantContext.getTenant());

        if (salonOperationalMessage != null) {
            model.addAttribute("salonOperationalMessage", salonOperationalMessage);
        }

        return "maintenence-ui";
    }

    @RequestMapping("/agendar")
    public String scheduling(Model model) {
        applySalonProfileToModel(model);
        return "pages/booking/index";
    }

    @RequestMapping("/entrar")
    public String login() {
        return "pages/public/login";
    }

    @RequestMapping("/cadastro")
    public String register() {
        return "pages/public/register";
    }

    @RequestMapping("/perfil")
    public String profile(Model model) {
        applySalonProfileToModel(model);
        return "pages/public/profile";
    }

    @RequestMapping("/admin/servicos")
    public String adminServices(Model model) {
        applySalonProfileToModel(model);
        return "pages/admin/services";
    }

    @RequestMapping("/admin/configuracoes")
    public String adminSettings() {
        return "";
    }

    @RequestMapping("/profissional/agendamentos")
    public String professionalAppointments() {
        return "";
    }
}