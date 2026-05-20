package com.sigdea.controller;

import com.sigdea.model.Comunicado;
import com.sigdea.repository.ComunicadoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PublicacionController {

    private final ComunicadoRepository comunicadoRepository;

    public PublicacionController(
            ComunicadoRepository comunicadoRepository
    ) {
        this.comunicadoRepository = comunicadoRepository;
    }

    // GUARDAR COMUNICADO

    @PostMapping("/guardar-comunicado")
    public String guardarComunicado(

            @RequestParam String titulo,
            @RequestParam String contenido

    ) {

        Comunicado comunicado = new Comunicado();

        comunicado.setTitulo(titulo);
        comunicado.setContenido(contenido);

        comunicadoRepository.save(comunicado);

        return "redirect:/directora";
    }

    // MOSTRAR DIRECTORA

    @GetMapping("/directora")
    public String directora(Model model) {

        model.addAttribute(
                "comunicados",
                comunicadoRepository.findAll()
        );

        return "directora";
    }
}