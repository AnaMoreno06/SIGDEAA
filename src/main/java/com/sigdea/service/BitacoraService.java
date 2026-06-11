package com.sigdea.service;

import com.sigdea.model.Bitacora;
import com.sigdea.repository.BitacoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BitacoraService {

    @Autowired
    private BitacoraRepository bitacoraRepository;

    // GUARDAR EVENTO

    public void guardarEvento(
            String usuario,
            String accion,
            String documento,
            String detalle
    ){

        Bitacora bitacora =
                new Bitacora();

        bitacora.setUsuario(usuario);

        bitacora.setAccion(accion);

        bitacora.setDocumento(documento);

        bitacora.setDetalle(detalle);

        bitacoraRepository.save(bitacora);

    }

    // LISTAR EVENTOS

    public List<Bitacora> listarEventos(){

        return bitacoraRepository.findAll();

    }

}

