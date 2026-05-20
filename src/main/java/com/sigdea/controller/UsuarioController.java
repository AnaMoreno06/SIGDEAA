package com.sigdea.controller;

import com.sigdea.model.Usuario;
import com.sigdea.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // LISTAR USUARIOS
    @GetMapping("/usuarios")
    public String usuarios(Model model) {

        model.addAttribute(
                "listaUsuarios",
                usuarioRepository.findAll()
        );

        return "usuarios";
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevoUsuario")
    public String nuevoUsuario(Model model) {

        model.addAttribute(
                "usuario",
                new Usuario()
        );

        return "nuevoUsuario";
    }

    // GUARDAR
    @PostMapping("/guardarUsuario")
    public String guardarUsuario(
            @ModelAttribute Usuario usuario
    ) {

        usuario.setEstado("ACTIVO");

        usuarioRepository.save(usuario);

        return "redirect:/usuarios";
    }

    // EDITAR
    @GetMapping("/editarUsuario/{id}")
    public String editarUsuario(
            @PathVariable Long id,
            Model model
    ) {

        Usuario usuario = usuarioRepository
                .findById(id)
                .orElse(null);

        model.addAttribute("usuario", usuario);

        return "editarUsuario";
    }

    // ACTUALIZAR
    @PostMapping("/actualizarUsuario")
    public String actualizarUsuario(
            @ModelAttribute Usuario usuario
    ) {

        usuarioRepository.save(usuario);

        return "redirect:/usuarios";
    }

    // ELIMINAR
    @GetMapping("/eliminarUsuario/{id}")
    public String eliminarUsuario(
            @PathVariable Long id
    ) {

        usuarioRepository.deleteById(id);

        return "redirect:/usuarios";
    }

}