package com.sigdea.repository;

import com.sigdea.model.Comunicado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComunicadoRepository
        extends JpaRepository<Comunicado, Long> {
}