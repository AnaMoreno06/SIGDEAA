package com.sigdea.controller;

import com.sigdea.model.Proceso;
import com.sigdea.repository.ProcesoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProcesoController {

    private final ProcesoRepository procesoRepository;

    public ProcesoController(ProcesoRepository procesoRepository) {
        this.procesoRepository = procesoRepository;
    }

    // 🔹 LISTAR PROCESOS
    @GetMapping("/procesos")
    public String listarProcesos(Model model){
        model.addAttribute("listaProcesos", procesoRepository.findAll());
        return "procesos"; // 👉 debe existir procesos.html
    }

    // 🔹 VER DETALLE
    @GetMapping("/proceso/{id}")
    public String verProceso(@PathVariable Long id, Model model){

        Proceso proc = procesoRepository.findById(id).orElse(null);

        if(proc == null){
            return "redirect:/procesos"; // 🔥 evita error 500
        }

        model.addAttribute("proceso", proc);
        return "proceso-detalle"; // 👉 debe existir proceso-detalle.html
    }
}