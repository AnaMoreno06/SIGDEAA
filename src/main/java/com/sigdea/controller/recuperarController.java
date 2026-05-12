package com.sigdea.controller;

import com.sigdea.service.CorreoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class recuperarController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CorreoService correoService;

    @GetMapping("/recuperar")
    public String recuperar() {

        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String enviarCorreo(String correo) {

        correoService.enviarCorreo(
                correo,
                "Recuperación de contraseña",
                "Hola, este es un mensaje de recuperación de contraseña de SIGDEA."
        );

        return "redirect:/login";
    }
}