package com.sigdea.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @GetMapping("/documentos")
    public String documentos(){
        return "documentos";
    }

    @GetMapping("/indicadores")
    public String indicadores(){
        return "indicadores";
    }

    @GetMapping("/reportes")
    public String reportes(){
        return "reportes";
    }

    @GetMapping("/calendario")
    public String calendario(){
        return "calendario";
    }


    @GetMapping("/evidencias")
    public String evidencias(){
        return "evidencias";
    }

    @GetMapping("/configuracion")
    public String configuracion(){
        return "configuracion";
    }

    @GetMapping("/perfil")
    public String perfil(){
        return "perfil";
    }
}