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
import org.apache.pdfbox.pdmodel.common.PDRectangle;

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
    public String procesos(Model model) {

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
    public String nuevoProceso() {

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
    ) {

        procesoRepository.deleteById(id);

        return "redirect:/procesos";
    }


    @GetMapping("/ver-proceso/{id}")
    public String verProceso(

            @PathVariable Long id,
            Model model

    ) {

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

    ) {

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

        PDPage page = new PDPage(PDRectangle.A4);

        document.addPage(page);

        PDPageContentStream content =
                new PDPageContentStream(document, page);

        // =========================
        // COLORES
        // =========================

        Color rojo = new Color(150, 0, 0);
        Color gris = new Color(240, 240, 240);
        Color negro = Color.BLACK;

        // =========================
        // FONDO ENCABEZADO
        // =========================

        content.setNonStrokingColor(gris);

        content.addRect(40, 690, 520, 100);

        content.fill();

        // =========================
        // LINEA SUPERIOR
        // =========================

        content.setStrokingColor(rojo);

        content.setLineWidth(4);

        content.moveTo(40, 790);

        content.lineTo(560, 790);

        content.stroke();

        // =========================
        // TITULO
        // =========================

        content.beginText();

        content.setNonStrokingColor(negro);

        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                ),
                30
        );

        content.newLineAtOffset(60, 735);

        content.showText("SIGDEA");

        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                ),
                24
        );

        content.newLineAtOffset(0, -40);

        content.showText("FICHA DEL PROCESO");

        content.endText();

        // =========================
        // TABLA DE INFORMACION
        // =========================

        float y = 620;

        String[][] datos = {
                {"Codigo", proceso.getCodigo()},
                {"Nombre", proceso.getNombre()},
                {"Responsable", proceso.getResponsable()},
                {"Estado", proceso.getEstado()},
                {"Objetivo", proceso.getObjetivo()},
                {"Alcance", proceso.getAlcance()},
                {"Entradas/Salidas", proceso.getEntradas()},
                {"Recursos", proceso.getRecursos()}
        };

        for (String[] fila : datos) {

            // Fondo gris fila
            content.setNonStrokingColor(
                    new Color(250, 250, 250)
            );

            content.addRect(50, y - 15, 500, 30);

            content.fill();

            // Borde fila
            content.setStrokingColor(Color.LIGHT_GRAY);

            content.addRect(50, y - 15, 500, 30);

            content.stroke();

            // Texto
            content.beginText();

            content.setNonStrokingColor(negro);

            content.setFont(
                    new PDType1Font(
                            Standard14Fonts.FontName.HELVETICA_BOLD
                    ),
                    12
            );

            content.newLineAtOffset(60, y);

            content.showText(fila[0] + ":");

            content.setFont(
                    new PDType1Font(
                            Standard14Fonts.FontName.HELVETICA
                    ),
                    12
            );

            content.newLineAtOffset(130, 0);

            content.showText(
                    fila[1] != null ? fila[1] : ""
            );

            content.endText();

            y -= 45;
        }

        // =========================
        // PIE DE PAGINA
        // =========================

        content.beginText();

        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_OBLIQUE
                ),
                10
        );

        content.newLineAtOffset(180, 40);

        content.showText(
                "Sistema Integrado de Gestion Documental"
        );

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
}