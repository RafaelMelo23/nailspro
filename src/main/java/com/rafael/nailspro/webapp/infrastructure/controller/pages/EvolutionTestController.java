package com.rafael.nailspro.webapp.infrastructure.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EvolutionTestController {

    @GetMapping("/evolution-test")
    public String evolutionTest() {
        return "evoconnection-test";
    }
}
