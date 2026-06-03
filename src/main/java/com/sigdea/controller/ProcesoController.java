package com.sigdea.controller;

import com.sigdea.model.Proceso;
import com.sigdea.repository.ProcesoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ClassPathResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

@Controller
public class ProcesoController {

    private final ProcesoRepository procesoRepository;

    public ProcesoController(ProcesoRepository procesoRepository) {
        this.procesoRepository = procesoRepository;
    }

    // ======================================
    // MOSTRAR PROCESOS
    // ======================================

    @GetMapping("/procesos")
    public String procesos(Model model) {
        model.addAttribute("procesos", procesoRepository.findAll());
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
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(required = false) String linkDrive
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

        if (!archivo.isEmpty()) {
            String nombreArchivo = archivo.getOriginalFilename();
            Path carpeta = Paths.get("src/main/resources/uploads");
            if (!Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }
            Path ruta = carpeta.resolve(nombreArchivo);
            Files.write(ruta, archivo.getBytes());
            proceso.setDocumento(nombreArchivo);
        }

        procesoRepository.save(proceso);
        return "redirect:/procesos";
    }

    // ======================================
    // ELIMINAR PROCESO
    // ======================================

    @GetMapping("/eliminar-proceso/{id}")
    public String eliminarProceso(@PathVariable Long id) {
        procesoRepository.deleteById(id);
        return "redirect:/procesos";
    }

    // ======================================
    // VER PROCESO
    // ======================================

    @GetMapping("/ver-proceso/{id}")
    public String verProceso(@PathVariable Long id, Model model) {
        Proceso proceso = procesoRepository.findById(id).orElse(null);
        model.addAttribute("proceso", proceso);
        return "proceso-detalle";
    }

    // ======================================
    // EDITAR PROCESO
    // ======================================

    @GetMapping("/editar-proceso/{id}")
    public String editarProceso(@PathVariable Long id, Model model) {
        Proceso proceso = procesoRepository.findById(id).orElse(null);
        model.addAttribute("proceso", proceso);
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
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(required = false) String linkDrive
    ) throws IOException {

        Proceso proceso = procesoRepository.findById(id).orElse(null);
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
            String nombreArchivo = archivo.getOriginalFilename();
            Path carpeta = Paths.get("src/main/resources/uploads");
            if (!Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }
            Path ruta = carpeta.resolve(nombreArchivo);
            Files.write(ruta, archivo.getBytes());
            proceso.setDocumento(nombreArchivo);
        }

        procesoRepository.save(proceso);
        return "redirect:/procesos";
    }

    // ======================================
    // DESCARGAR FICHA PDF
    // ======================================

    @GetMapping("/descargar-ficha/{id}")
    public ResponseEntity<byte[]> descargarFicha(@PathVariable Long id) throws Exception {

        Proceso proceso = procesoRepository.findById(id).orElse(null);

        if (proceso == null) {
            return ResponseEntity.notFound().build();
        }

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        float pageWidth  = PDRectangle.A4.getWidth();   // 595
        float pageHeight = PDRectangle.A4.getHeight();  // 842

        PDPageContentStream cs = new PDPageContentStream(document, page);

        // ============================================================
        // COLORES
        // ============================================================
        Color rojoOscuro = new Color(139, 0,   0);
        Color grisFondo  = new Color(248, 248, 248);
        Color grisLinea  = new Color(200, 200, 200);
        Color grisTexto  = new Color(80,  80,  80);
        Color grisTitulo = new Color(50,  50,  50);
        Color blanco     = Color.WHITE;

        // ============================================================
        // FUENTES
        // ============================================================
        PDType1Font bold   = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font normal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        PDType1Font italic = new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE);

        float margenIzq = 40f;
        float margenDer = 555f;
        float anchoUtil = margenDer - margenIzq; // 515

        // ============================================================
        // ENCABEZADO — borde exterior
        // ============================================================
        cs.setNonStrokingColor(blanco);
        cs.addRect(margenIzq, pageHeight - 80, anchoUtil, 70);
        cs.fill();

        cs.setStrokingColor(grisLinea);
        cs.setLineWidth(1f);
        cs.addRect(margenIzq, pageHeight - 80, anchoUtil, 70);
        cs.stroke();

        // Líneas verticales divisoras del header
        cs.moveTo(margenIzq + 115, pageHeight - 80);
        cs.lineTo(margenIzq + 115, pageHeight - 10);
        cs.stroke();

        cs.moveTo(margenIzq + 380, pageHeight - 80);
        cs.lineTo(margenIzq + 380, pageHeight - 10);
        cs.stroke();

        // LOGO — usando ClassPathResource para funcionar tanto en dev como en producción
        try {
            ClassPathResource logoResource =
                    new ClassPathResource("static/img/logo_sistemas.png");
            InputStream logoStream = logoResource.getInputStream();
            PDImageXObject logo =
                    PDImageXObject.createFromByteArray(
                            document,
                            logoStream.readAllBytes(),
                            "logo_sistemas"
                    );
            cs.drawImage(logo, margenIzq + 5, pageHeight - 72, 105, 52);
        } catch (Exception e) {
            // Placeholder si no encuentra el logo
            cs.setNonStrokingColor(new Color(220, 220, 220));
            cs.addRect(margenIzq + 5, pageHeight - 72, 105, 52);
            cs.fill();
            cs.setNonStrokingColor(grisTexto);
            cs.beginText();
            cs.setFont(normal, 7);
            cs.newLineAtOffset(margenIzq + 20, pageHeight - 44);
            cs.showText("LOGO");
            cs.endText();
        }

        // TÍTULO CENTRAL
        cs.beginText();
        cs.setNonStrokingColor(grisTexto);
        cs.setFont(normal, 9);
        cs.newLineAtOffset(margenIzq + 120, pageHeight - 25);
        cs.showText("SISTEMA INTEGRADO DE GESTIÓN (SIGDEA)");
        cs.endText();

        cs.beginText();
        cs.setNonStrokingColor(grisTitulo);
        cs.setFont(bold, 14);
        cs.newLineAtOffset(margenIzq + 135, pageHeight - 43);
        cs.showText("DOCUMENTO DE");
        cs.endText();

        cs.beginText();
        cs.setFont(bold, 14);
        cs.newLineAtOffset(margenIzq + 130, pageHeight - 57);
        cs.showText("CARACTERIZACIÓN");
        cs.endText();

        // DATOS DERECHA: Código / Versión / Fecha
        String[][] metaDatos = {
                {"Código:",  proceso.getCodigo() != null ? proceso.getCodigo() : ""},
                {"Versión:", "01"},
                {"Fecha:",   LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}
        };

        float yMeta = pageHeight - 14;
        for (String[] fila : metaDatos) {
            // Línea horizontal separadora
            cs.setStrokingColor(grisLinea);
            cs.setLineWidth(0.5f);
            cs.moveTo(margenIzq + 380, yMeta - 8);
            cs.lineTo(margenDer, yMeta - 8);
            cs.stroke();

            cs.beginText();
            cs.setNonStrokingColor(grisTitulo);
            cs.setFont(bold, 9);
            cs.newLineAtOffset(margenIzq + 385, yMeta - 18);
            cs.showText(fila[0]);
            cs.endText();

            cs.beginText();
            cs.setNonStrokingColor(grisTexto);
            cs.setFont(normal, 9);
            cs.newLineAtOffset(margenIzq + 440, yMeta - 18);
            cs.showText(fila[1]);
            cs.endText();

            yMeta -= 23;
        }

        // ============================================================
        // BARRA ROJA — FICHA DE CARACTERIZACIÓN DE PROCESO
        // ============================================================
        float yBarra = pageHeight - 100;

        cs.setNonStrokingColor(rojoOscuro);
        cs.addRect(margenIzq, yBarra, anchoUtil, 22);
        cs.fill();

        cs.beginText();
        cs.setNonStrokingColor(blanco);
        cs.setFont(bold, 11);
        cs.newLineAtOffset(margenIzq + 125, yBarra + 7);
        cs.showText("FICHA DE CARACTERIZACIÓN DE PROCESO");
        cs.endText();

        float yCursor = yBarra - 8;

        // ============================================================
        // SECCIÓN I — IDENTIFICACIÓN DEL PROCESO
        // ============================================================
        yCursor = dibujarSeccion(cs, bold, "I. IDENTIFICACIÓN DEL PROCESO",
                margenIzq, yCursor, anchoUtil, rojoOscuro, blanco);

        String[][] identificacion = {
                {"Nombre del Proceso", proceso.getNombre()},
                {"Responsable",        proceso.getResponsable()},
                {"Tipo de Proceso",    proceso.getEstado()},
                {"Objetivo",           proceso.getObjetivo()}
        };

        for (String[] fila : identificacion) {
            boolean esObjetivo = "Objetivo".equals(fila[0]);
            yCursor = dibujarFilaTabla(cs, bold, normal, italic,
                    fila[0], fila[1],
                    margenIzq, yCursor, anchoUtil,
                    grisFondo, grisLinea, grisTitulo, grisTexto, esObjetivo);
        }

        // ============================================================
        // SECCIÓN II — DESCRIPCIÓN OPERATIVA
        // ============================================================
        yCursor -= 6;
        yCursor = dibujarSeccion(cs, bold, "II. DESCRIPCIÓN OPERATIVA",
                margenIzq, yCursor, anchoUtil, rojoOscuro, blanco);

        // ALCANCE
        yCursor = dibujarSubtitulo(cs, bold, "ALCANCE",
                margenIzq, yCursor, anchoUtil, grisLinea, grisTitulo);

        yCursor = dibujarParrafo(cs, normal, proceso.getAlcance(),
                margenIzq, yCursor, anchoUtil, grisTexto);

        // ENTRADAS y SALIDAS — dos columnas
        yCursor -= 4;
        float anchoCol = (anchoUtil - 8) / 2f;

        // Calculamos la altura de ambas tarjetas por adelantado para alinearlas
        float yEntradas = yCursor;
        float ySalidas  = yCursor;

        dibujarTarjeta(cs, bold, normal,
                "ENTRADAS", proceso.getEntradas(),
                margenIzq, yEntradas, anchoCol,
                grisLinea, grisTitulo, grisTexto);

        // "salidas" viene del mismo campo entradas separado por "|"
        // Si tienes un campo salidas en tu modelo, cámbialo aquí
        String salidas = "";
        if (proceso.getEntradas() != null && proceso.getEntradas().contains("|")) {
            String[] partes = proceso.getEntradas().split("\\|");
            salidas = partes.length > 1 ? partes[1].trim() : "";
        }

        dibujarTarjeta(cs, bold, normal,
                "SALIDAS", salidas,
                margenIzq + anchoCol + 8, ySalidas, anchoCol,
                grisLinea, grisTitulo, grisTexto);

        yCursor -= 68; // altura de las tarjetas

        // ============================================================
        // SECCIÓN III — RECURSOS NECESARIOS
        // ============================================================
        yCursor -= 6;
        yCursor = dibujarSeccion(cs, bold, "III. RECURSOS NECESARIOS",
                margenIzq, yCursor, anchoUtil, rojoOscuro, blanco);

        // 3 columnas: Humanos | Técnicos | Infraestructura
        // Si tienes los recursos separados por "|" en el campo recursos
        // Ej: "Rector, Vicerrectores|Software SIGDEA|Oficinas Administrativas"
        String[] recursosArr = {"", "", ""};
        if (proceso.getRecursos() != null) {
            String[] partes = proceso.getRecursos().split("\\|");
            for (int i = 0; i < Math.min(partes.length, 3); i++) {
                recursosArr[i] = partes[i].trim();
            }
        }

        String[] titulosRec = {"HUMANOS", "TÉCNICOS", "INFRAESTRUCTURA"};
        float anchoTercio = (anchoUtil - 16) / 3f;
        float xCol = margenIzq;

        for (int i = 0; i < 3; i++) {
            dibujarTarjeta(cs, bold, normal,
                    titulosRec[i], recursosArr[i],
                    xCol, yCursor, anchoTercio,
                    grisLinea, grisTitulo, grisTexto);
            xCol += anchoTercio + 8;
        }

        yCursor -= 68;

        // ============================================================
        // SECCIÓN IV — DOCUMENTOS ASOCIADOS
        // ============================================================
        yCursor -= 6;
        yCursor = dibujarSeccion(cs, bold, "IV. DOCUMENTOS ASOCIADOS",
                margenIzq, yCursor, anchoUtil, rojoOscuro, blanco);

        // Cabecera de tabla
        float[] anchosCols = {80f, 310f, 125f};
        String[] cabeceras = {"CÓDIGO", "NOMBRE DEL DOCUMENTO", "TIPO"};

        float yTabla = yCursor - 4;

        cs.setNonStrokingColor(new Color(230, 230, 230));
        cs.addRect(margenIzq, yTabla - 16, anchoUtil, 16);
        cs.fill();

        cs.setStrokingColor(grisLinea);
        cs.setLineWidth(0.5f);
        cs.addRect(margenIzq, yTabla - 16, anchoUtil, 16);
        cs.stroke();

        float xCab = margenIzq + 5;
        for (int i = 0; i < cabeceras.length; i++) {
            cs.beginText();
            cs.setNonStrokingColor(grisTitulo);
            cs.setFont(bold, 8);
            cs.newLineAtOffset(xCab, yTabla - 11);
            cs.showText(cabeceras[i]);
            cs.endText();
            xCab += anchosCols[i];
        }

        // Línea inferior de la tabla
        yTabla -= 16;
        cs.moveTo(margenIzq, yTabla);
        cs.lineTo(margenDer, yTabla);
        cs.stroke();

        // Si tienes documentos asociados en tu modelo, itera aquí.
        // Por ahora se deja la tabla vacía con espacio para 3 filas.
        for (int fila = 0; fila < 3; fila++) {
            cs.moveTo(margenIzq, yTabla - 14);
            cs.lineTo(margenDer, yTabla - 14);
            cs.stroke();
            yTabla -= 14;
        }

        yCursor = yTabla - 6;

        // ============================================================
        // PIE DE PÁGINA — Firmas
        // ============================================================
        float yPie = 75f;

        // Firma izquierda
        cs.setStrokingColor(new Color(100, 100, 100));
        cs.setLineWidth(1f);
        cs.moveTo(margenIzq + 10, yPie + 22);
        cs.lineTo(margenIzq + 190, yPie + 22);
        cs.stroke();

        cs.beginText();
        cs.setNonStrokingColor(grisTitulo);
        cs.setFont(bold, 8);
        cs.newLineAtOffset(margenIzq + 35, yPie + 12);
        cs.showText("ELABORÓ / REVISÓ");
        cs.endText();

        cs.beginText();
        cs.setNonStrokingColor(grisTexto);
        cs.setFont(normal, 7);
        cs.newLineAtOffset(margenIzq + 20, yPie + 2);
        cs.showText("Oficina de Planeación y Calidad");
        cs.endText();

        // Firma derecha
        cs.moveTo(margenDer - 190, yPie + 22);
        cs.lineTo(margenDer - 10, yPie + 22);
        cs.stroke();

        cs.beginText();
        cs.setNonStrokingColor(grisTitulo);
        cs.setFont(bold, 8);
        cs.newLineAtOffset(margenDer - 160, yPie + 12);
        cs.showText("APROBÓ");
        cs.endText();

        cs.beginText();
        cs.setNonStrokingColor(grisTexto);
        cs.setFont(normal, 7);
        cs.newLineAtOffset(margenDer - 185, yPie + 2);
        cs.showText("Consejo Superior Universitario");
        cs.endText();

        // Línea separadora pie
        cs.setStrokingColor(grisLinea);
        cs.setLineWidth(0.5f);
        cs.moveTo(margenIzq, yPie - 8);
        cs.lineTo(margenDer, yPie - 8);
        cs.stroke();

        // Copyright
        cs.beginText();
        cs.setFont(italic, 7);
        cs.setNonStrokingColor(grisTexto);
        cs.newLineAtOffset(margenIzq, yPie - 20);
        cs.showText("© " + Year.now().getValue()
                + " DEPARTAMENTO DE NORMALIZACIÓN TÉCNICA - CONFIDENCIAL");
        cs.endText();

        cs.beginText();
        cs.setFont(normal, 7);
        cs.setNonStrokingColor(grisTexto);
        cs.newLineAtOffset(margenDer - 50, yPie - 20);
        cs.showText("Página 1 de 1");
        cs.endText();

        cs.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ficha-proceso-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }

    // ============================================================
    // MÉTODOS AUXILIARES PARA GENERACIÓN DE PDF
    // ============================================================

    /**
     * Dibuja una barra de sección con fondo rojo y título en blanco.
     * Retorna la nueva posición Y (hacia abajo).
     */
    private float dibujarSeccion(PDPageContentStream cs,
                                 PDType1Font bold, String titulo,
                                 float x, float y, float ancho,
                                 Color colorFondo, Color colorTexto) throws IOException {

        cs.setNonStrokingColor(colorFondo);
        cs.addRect(x, y - 18, ancho, 18);
        cs.fill();

        cs.beginText();
        cs.setNonStrokingColor(colorTexto);
        cs.setFont(bold, 9);
        cs.newLineAtOffset(x + 6, y - 12);
        cs.showText(titulo);
        cs.endText();

        return y - 18;
    }

    /**
     * Dibuja una fila de tabla con etiqueta en negrita y valor.
     * Retorna la nueva posición Y.
     */
    private float dibujarFilaTabla(PDPageContentStream cs,
                                   PDType1Font bold, PDType1Font normal, PDType1Font italic,
                                   String etiqueta, String valor,
                                   float x, float y, float ancho,
                                   Color grisFondo, Color grisLinea,
                                   Color colorTitulo, Color colorTexto,
                                   boolean usarItalic) throws IOException {

        float altoFila = 22f;
        float anchoEtiqueta = 130f;

        cs.setNonStrokingColor(grisFondo);
        cs.addRect(x, y - altoFila, ancho, altoFila);
        cs.fill();

        cs.setStrokingColor(grisLinea);
        cs.setLineWidth(0.5f);
        cs.addRect(x, y - altoFila, ancho, altoFila);
        cs.stroke();

        // Separador vertical
        cs.moveTo(x + anchoEtiqueta, y - altoFila);
        cs.lineTo(x + anchoEtiqueta, y);
        cs.stroke();

        // Etiqueta
        cs.beginText();
        cs.setNonStrokingColor(colorTitulo);
        cs.setFont(bold, 9);
        cs.newLineAtOffset(x + 6, y - 14);
        cs.showText(etiqueta);
        cs.endText();

        // Valor (truncado si es muy largo)
        String val = valor != null ? valor : "";
        if (val.length() > 72) val = val.substring(0, 72) + "...";

        cs.beginText();
        cs.setNonStrokingColor(colorTexto);
        cs.setFont(usarItalic ? italic : normal, 9);
        cs.newLineAtOffset(x + anchoEtiqueta + 6, y - 14);
        cs.showText(val);
        cs.endText();

        return y - altoFila;
    }

    /**
     * Dibuja un subtítulo pequeño con línea inferior.
     * Retorna la nueva posición Y.
     */
    private float dibujarSubtitulo(PDPageContentStream cs,
                                   PDType1Font bold, String titulo,
                                   float x, float y, float ancho,
                                   Color grisLinea, Color colorTitulo) throws IOException {

        cs.beginText();
        cs.setNonStrokingColor(colorTitulo);
        cs.setFont(bold, 9);
        cs.newLineAtOffset(x + 6, y - 14);
        cs.showText(titulo);
        cs.endText();

        cs.setStrokingColor(grisLinea);
        cs.setLineWidth(0.5f);
        cs.moveTo(x, y - 18);
        cs.lineTo(x + ancho, y - 18);
        cs.stroke();

        return y - 20;
    }

    /**
     * Dibuja un párrafo de texto simple.
     * Retorna la nueva posición Y.
     */
    private float dibujarParrafo(PDPageContentStream cs,
                                 PDType1Font font, String texto,
                                 float x, float y, float ancho,
                                 Color colorTexto) throws IOException {

        String t = texto != null ? texto : "";
        if (t.length() > 105) t = t.substring(0, 105) + "...";

        cs.beginText();
        cs.setNonStrokingColor(colorTexto);
        cs.setFont(font, 9);
        cs.newLineAtOffset(x + 6, y - 14);
        cs.showText(t);
        cs.endText();

        return y - 22;
    }

    /**
     * Dibuja una tarjeta con título, línea divisora y lista de ítems.
     * Los ítems se separan por coma, punto y coma o salto de línea.
     * Retorna la nueva posición Y.
     */
    private float dibujarTarjeta(PDPageContentStream cs,
                                 PDType1Font bold, PDType1Font normal,
                                 String titulo, String contenido,
                                 float x, float y, float ancho,
                                 Color grisLinea, Color colorTitulo, Color colorTexto) throws IOException {

        float altoTarjeta = 65f;

        // Borde de la tarjeta
        cs.setStrokingColor(grisLinea);
        cs.setLineWidth(0.5f);
        cs.addRect(x, y - altoTarjeta, ancho, altoTarjeta);
        cs.stroke();

        // Título
        cs.beginText();
        cs.setNonStrokingColor(colorTitulo);
        cs.setFont(bold, 8);
        cs.newLineAtOffset(x + 5, y - 12);
        cs.showText(titulo);
        cs.endText();

        // Línea bajo el título
        cs.moveTo(x, y - 16);
        cs.lineTo(x + ancho, y - 16);
        cs.stroke();

        // Ítems del contenido
        if (contenido != null && !contenido.isBlank()) {
            String[] items = contenido.split("[,;\\n]+");
            float yItem = y - 27;
            for (String item : items) {
                if (yItem < y - altoTarjeta + 8) break;
                String itemTrim = item.trim();
                if (itemTrim.isEmpty()) continue;
                if (itemTrim.length() > 38) itemTrim = itemTrim.substring(0, 38) + "...";

                cs.beginText();
                cs.setNonStrokingColor(colorTexto);
                cs.setFont(normal, 7.5f);
                cs.newLineAtOffset(x + 8, yItem);
                cs.showText("\u2022 " + itemTrim);
                cs.endText();
                yItem -= 11;
            }
        }

        return y - altoTarjeta;
    }
}