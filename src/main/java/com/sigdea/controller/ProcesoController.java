package com.sigdea.controller;

import com.sigdea.model.Proceso;
import com.sigdea.repository.ProcesoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.ByteArrayOutputStream;
import java.awt.Color;

import org.springframework.http.MediaType;

import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

@Controller
public class ProcesoController {

    private final ProcesoRepository procesoRepository;

    public ProcesoController(
            ProcesoRepository procesoRepository
    ) {
        this.procesoRepository = procesoRepository;
    }

    // ======================================
    // MOSTRAR PROCESOS
    // ======================================

    @GetMapping("/procesos")
    public String procesos(Model model){

        model.addAttribute(
                "procesos",
                procesoRepository.findAll()
        );

        return "procesos";
    }

    // ======================================
    // NUEVO PROCESO
    // ======================================

    @GetMapping("/nuevo-proceso")
    public String nuevoProceso(){

        return "nuevo-proceso";
    }

    // ======================================
    // GUARDAR PROCESO
    // ======================================

    @PostMapping("/guardar-proceso")
    public String guardarProceso(

            @RequestParam String codigo,
            @RequestParam String nombre,
            @RequestParam String responsable,
            @RequestParam String estado,
            @RequestParam String objetivo,
            @RequestParam String alcance,
            @RequestParam String entradas,
            @RequestParam String recursos,

            @RequestParam("archivo")
            MultipartFile archivo,

            @RequestParam(required = false)
            String linkDrive

    ) throws IOException {

        Proceso proceso = new Proceso();

        proceso.setCodigo(codigo);
        proceso.setNombre(nombre);
        proceso.setResponsable(responsable);
        proceso.setEstado(estado);

        proceso.setObjetivo(objetivo);
        proceso.setAlcance(alcance);
        proceso.setEntradas(entradas);
        proceso.setRecursos(recursos);

        proceso.setLinkDrive(linkDrive);

        // ======================================
        // GUARDAR ARCHIVO
        // ======================================

        if (!archivo.isEmpty()) {

            String nombreArchivo =
                    archivo.getOriginalFilename();

            // carpeta uploads

            Path carpeta =
                    Paths.get(
                            "src/main/resources/uploads"
                    );

            // crear carpeta si no existe

            if (!Files.exists(carpeta)) {

                Files.createDirectories(carpeta);
            }

            // ruta completa

            Path ruta =
                    carpeta.resolve(nombreArchivo);

            // guardar archivo

            Files.write(
                    ruta,
                    archivo.getBytes()
            );

            // guardar nombre en bd

            proceso.setDocumento(nombreArchivo);
        }

        procesoRepository.save(proceso);

        return "redirect:/procesos";
    }


    @GetMapping("/eliminar-proceso/{id}")
    public String eliminarProceso(
            @PathVariable Long id
    ){

        procesoRepository.deleteById(id);

        return "redirect:/procesos";
    }


    @GetMapping("/ver-proceso/{id}")
    public String verProceso(

            @PathVariable Long id,
            Model model

    ){

        Proceso proceso =
                procesoRepository.findById(id).orElse(null);

        model.addAttribute(
                "proceso",
                proceso
        );

        return "proceso-detalle";
    }

    @GetMapping("/editar-proceso/{id}")
    public String editarProceso(

            @PathVariable Long id,
            Model model

    ){

        Proceso proceso =
                procesoRepository.findById(id).orElse(null);

        model.addAttribute(
                "proceso",
                proceso
        );

        return "editar-proceso";
    }

    @PostMapping("/guardar-edicion-proceso")
    public String guardarEdicion(

            @RequestParam Long id,
            @RequestParam String codigo,
            @RequestParam String nombre,
            @RequestParam String responsable,
            @RequestParam String estado,
            @RequestParam String objetivo,
            @RequestParam String alcance,
            @RequestParam String entradas,
            @RequestParam String recursos,

            @RequestParam("archivo")
            MultipartFile archivo,

            @RequestParam(required = false)
            String linkDrive

    ) throws IOException {

        Proceso proceso =
                procesoRepository.findById(id).orElse(null);

        if (proceso == null) {

            return "redirect:/procesos";
        }

        proceso.setCodigo(codigo);
        proceso.setNombre(nombre);
        proceso.setResponsable(responsable);
        proceso.setEstado(estado);

        proceso.setObjetivo(objetivo);
        proceso.setAlcance(alcance);
        proceso.setEntradas(entradas);
        proceso.setRecursos(recursos);

        proceso.setLinkDrive(linkDrive);


        if (!archivo.isEmpty()) {

            String nombreArchivo =
                    archivo.getOriginalFilename();

            Path carpeta =
                    Paths.get(
                            "src/main/resources/uploads"
                    );

            if (!Files.exists(carpeta)) {

                Files.createDirectories(carpeta);
            }

            Path ruta =
                    carpeta.resolve(nombreArchivo);

            Files.write(
                    ruta,
                    archivo.getBytes()
            );

            proceso.setDocumento(nombreArchivo);
        }

        procesoRepository.save(proceso);

        return "redirect:/procesos";
    }
    @GetMapping("/descargar-ficha/{id}")
    public ResponseEntity<byte[]> descargarFicha(
            @PathVariable Long id) throws Exception {

        Proceso proceso =
                procesoRepository.findById(id).orElse(null);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        document.addPage(page);

        PDPageContentStream content =
                new PDPageContentStream(document, page);

        // Línea roja superior
        content.setStrokingColor(
                new java.awt.Color(164, 0, 0)
        );
        content.setLineWidth(3);
        content.moveTo(50, 760);
        content.lineTo(550, 760);
        content.stroke();

        // Título
        content.beginText();

        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                ),
                22
        );

        content.newLineAtOffset(50, 720);
        content.showText("SIGDEA");

        content.newLineAtOffset(0, -40);
        content.showText("FICHA DEL PROCESO");

        // Datos
        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA
                ),
                13
        );

        content.newLineAtOffset(0, -50);
        content.showText("Codigo: PRUEBA");

        content.newLineAtOffset(0, -25);
        content.showText("Nombre: PRUEBA");

        content.newLineAtOffset(0, -25);
        content.showText("Responsable: PRUEBA");

        content.newLineAtOffset(0, -25);
        content.showText("Estado: ACTIVO");

        content.endText();

        content.close();

        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();

        document.save(baos);
        document.close();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ficha-proceso.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }

    // content.showText("Codigo: " + proceso.getCodigo());
// content.showText("Nombre: " + proceso.getNombre());
// content.showText("Responsable: " + proceso.getResponsable());
// content.showText("Estado: " + proceso.getEstado());
// content.showText("Objetivo: " + proceso.getObjetivo());
// content.showText("Alcance: " + proceso.getAlcance());

}