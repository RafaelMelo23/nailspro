package com.rafael.nailspro.webapp.infrastructure.controller.pages;

import com.rafael.nailspro.webapp.application.salon.service.SalonProfileService;
import com.rafael.nailspro.webapp.shared.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PagesController {

    private final SalonProfileService salonProfileService;

    @RequestMapping("/redefinir-senha")
    public String redefinirSenha(@RequestParam String resetToken,
                                 Model model) {

        model.addAttribute("resetToken", resetToken);
        return "redefinir-senha";
    }

    @RequestMapping("/offline")
    public String renderMaintenenceHtml(Model model) {

        String salonOperationalMessage =
                salonProfileService.getSalonOperationalMessageByTenantId(TenantContext.getTenant());

        if (salonOperationalMessage != null) {
            model.addAttribute("salonOperationalMessage", salonOperationalMessage);
        }

        return "maintenence-ui";
    }

//    @RequestMapping("/agendar")
//    public String scheduling() {
//        return "";
//    }
//
//    @RequestMapping("/entrar")
//    public String login() {
//        return "";
//    }
//
//    @RequestMapping("/cadastro")
//    public String register() {
//        return "";
//    }
//
//    @RequestMapping("/perfil")
//    public String profile() {
//        return "";
//    }
//
//    @RequestMapping("/agendar")
//    public String createAppointment() {
//        return "";
//    }
//
//    @RequestMapping("/admin/servicos")
//    public String adminServices() {
//        return "";
//    }
//
//    @RequestMapping("/admin/configuracoes")
//    public String adminSettings() {
//        return "";
//    }
//
//    @RequestMapping("/agenda-profissional")
//    public String professionalAppointments() {
//        return "";
//    }
}
