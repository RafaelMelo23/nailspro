package com.rafael.agendanails.webapp.infrastructure.controller.pages;

import com.rafael.agendanails.webapp.application.salon.business.SalonProfileService;
import com.rafael.agendanails.webapp.domain.model.SalonProfile;
import com.rafael.agendanails.webapp.shared.tenant.TenantContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    private final SalonProfileService salonProfileService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        SalonProfile salon = salonProfileService.getByTenantIdElseNull(TenantContext.getTenant());
        if (salon != null) {
            model.addAttribute("salon", salon);
        }

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("status", statusCode);

            if (statusCode == 404) {
                return "error/404";
            } else if (statusCode == 500) {
                return "error/500";
            }
        }
        
        return "error/error";
    }
}
