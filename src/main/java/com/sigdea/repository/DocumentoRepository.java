package com.sigdea.repository;

import com.sigdea.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository
        extends JpaRepository<Documento, Long> {
}