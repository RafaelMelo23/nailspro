package com.rafael.nailspro.webapp.infrastructure.email.template;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    //todo: decide the name
    private static final String SAAS_NAME = "to decide yet";

    public String buildForgotPasswordEmail(String userEmail, String linkReset) {
        return """
            Olá, %s!
            
            Recebemos uma solicitação para redefinir sua senha no %s.
            Clique no link abaixo para criar uma nova senha:
            
            %s
            
            Se você não solicitou isso, ignore este e-mail.
            """.formatted(userEmail, SAAS_NAME, linkReset);
    }
}
