package com.sigdea.controller;

import com.sigdea.model.EvidenciaCna;
import com.sigdea.repository.EvidenciaCnaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/evidencias")
public class EvidenciaCnaController {

    private final EvidenciaCnaRepository repository;

    public EvidenciaCnaController(EvidenciaCnaRepository repository) {
        this.repository = repository;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model){

        model.addAttribute(
                "evidencia",
                new EvidenciaCna()
        );

        model.addAttribute(
                "lista",
                repository.findAll()
        );

        return "evidencias";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute EvidenciaCna evidencia
    ){

        repository.save(evidencia);

        return "redirect:/evidencias";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model
    ){

        EvidenciaCna evidencia =
                repository.findById(id).orElse(null);

        model.addAttribute(
                "evidencia",
                evidencia
        );

        return "editarEvidenciascna";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar")
    public String actualizar(
            @ModelAttribute EvidenciaCna evidencia
    ){

        repository.save(evidencia);

        return "redirect:/evidencias";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id
    ){

        repository.deleteById(id);

        return "redirect:/evidencias";
    }

}

