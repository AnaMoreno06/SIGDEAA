package com.sigdea.controller;

import com.sigdea.service.CorreoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecuperarController {

    @Autowired
    private CorreoService correoService;

    @GetMapping("/recuperar")
    public String recuperar() {

        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String enviarCorreo(
            @RequestParam("correo") String correo) {

        correoService.enviarCorreo(
                correo,
                "Recuperación de contraseña",
                "Hola. Tu contraseña es: 1234"
        );

        return "redirect:/login";
    }
}