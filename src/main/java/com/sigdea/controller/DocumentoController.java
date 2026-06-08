package com.sigdea.controller;

import com.sigdea.model.Documento;
import com.sigdea.repository.DocumentoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentoController {

    private final DocumentoRepository documentoRepository;

    public DocumentoController(
            DocumentoRepository documentoRepository
    ) {
        this.documentoRepository = documentoRepository;
    }

    // LISTA DOCUMENTOS

    @GetMapping("/gestion-documental")
    public String gestionDocumental(Model model) {

        model.addAttribute(
                "documentos",
                documentoRepository.findAll()
        );

        return "GestionDocumental/gestionDocumental";
    }

    // NUEVO DOCUMENTO

    @GetMapping("/nuevo-documento")
    public String nuevoDocumento() {

        return "GestionDocumental/nuevoDocumento";
    }

    // DETALLE DOCUMENTO

    @GetMapping("/detalle-documento")
    public String detalleDocumento() {

        return "GestionDocumental/detalleDocumento";
    }
}