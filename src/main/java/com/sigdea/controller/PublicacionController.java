package com.sigdea.controller;

import com.sigdea.model.Publicacion;
import com.sigdea.repository.PublicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class PublicacionController {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @PostMapping("/guardarPublicacion")
    public String guardarPublicacion(@ModelAttribute Publicacion publicacion) {
        publicacion.setFecha(LocalDate.now());
        publicacionRepository.save(publicacion);

        return "redirect:/directora";
    }

    @GetMapping("/directora")
    public String mostrarDirectora(Model model) {
        model.addAttribute("publicacion", new Publicacion());
        model.addAttribute("listaPublicaciones", publicacionRepository.findAll());

        return "directora";
    }
}