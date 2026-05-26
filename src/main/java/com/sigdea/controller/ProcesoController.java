package com.sigdea.controller;

import com.sigdea.model.Proceso;
import com.sigdea.repository.ProcesoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProcesoController {

    private final ProcesoRepository procesoRepository;

    public ProcesoController(
            ProcesoRepository procesoRepository
    ) {
        this.procesoRepository = procesoRepository;
    }

    // MOSTRAR PROCESOS

    @GetMapping("/procesos")
    public String procesos(Model model){

        model.addAttribute(
                "procesos",
                procesoRepository.findAll()
        );

        return "procesos";
    }

    // NUEVO PROCESO

    @GetMapping("/nuevo-proceso")
    public String nuevoProceso(){

        return "nuevo-proceso";
    }

    // GUARDAR

    @PostMapping("/guardar-proceso")
    public String guardarProceso(

            @RequestParam String codigo,
            @RequestParam String nombre,
            @RequestParam String responsable,
            @RequestParam String estado,
            @RequestParam String objetivo,
            @RequestParam String alcance,
            @RequestParam String entradas,
            @RequestParam String recursos

    ){

        Proceso proceso = new Proceso();

        proceso.setCodigo(codigo);
        proceso.setNombre(nombre);
        proceso.setResponsable(responsable);
        proceso.setEstado(estado);

        proceso.setObjetivo(objetivo);
        proceso.setAlcance(alcance);
        proceso.setEntradas(entradas);
        proceso.setRecursos(recursos);

        procesoRepository.save(proceso);

        return "redirect:/procesos";
    }

    // ELIMINAR

    @GetMapping("/eliminar-proceso/{id}")
    public String eliminarProceso(
            @PathVariable Long id
    ){

        procesoRepository.deleteById(id);

        return "redirect:/procesos";
    }
    // VER PROCESO

    @GetMapping("/ver-proceso/{id}")
    public String verProceso(
            @PathVariable Long id,
            Model model
    ){

        Proceso proceso =
                procesoRepository.findById(id).orElse(null);

        model.addAttribute("proceso", proceso);

        return "proceso-detalle";
    }

// EDITAR

    @GetMapping("/editar-proceso/{id}")
    public String editarProceso(
            @PathVariable Long id,
            Model model
    ){

        Proceso proceso =
                procesoRepository.findById(id).orElse(null);

        model.addAttribute("proceso", proceso);

        return "editar-proceso";
    }
    // GUARDAR EDICIÓN

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
            @RequestParam String recursos

    ){

        Proceso proceso =
                procesoRepository.findById(id).orElse(null);

        proceso.setCodigo(codigo);
        proceso.setNombre(nombre);
        proceso.setResponsable(responsable);
        proceso.setEstado(estado);

        proceso.setObjetivo(objetivo);
        proceso.setAlcance(alcance);
        proceso.setEntradas(entradas);
        proceso.setRecursos(recursos);

        procesoRepository.save(proceso);

        return "redirect:/procesos";
    }

}