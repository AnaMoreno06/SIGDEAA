package com.sigdea.controller;

import com.sigdea.repository.ComunicadoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final ComunicadoRepository comunicadoRepository;

    // CONSTRUCTOR

    public AuthController(
            ComunicadoRepository comunicadoRepository
    ) {

        this.comunicadoRepository = comunicadoRepository;

    }

    // HOME

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "comunicados",
                comunicadoRepository.findAll()
        );

        return "inicio";
    }

    // LOGIN

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    // INICIO

    @GetMapping("/inicio")
    public String inicio(Model model) {

        model.addAttribute(
                "comunicados",
                comunicadoRepository.findAll()
        );

        return "inicio";
    }

}