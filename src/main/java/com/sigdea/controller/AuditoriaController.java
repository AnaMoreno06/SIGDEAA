package com.sigdea.controller;

import com.sigdea.service.BitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditoriaController {

    @Autowired
    private BitacoraService bitacoraService;

    // VISTA AUDITORIA

    @GetMapping("/auditoria")
    public String auditoria(Model model){

        model.addAttribute(
                "eventos",
                bitacoraService.listarEventos()
        );

        return "auditoria";

    }

}

