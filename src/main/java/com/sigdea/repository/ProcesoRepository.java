package com.sigdea.repository;

import com.sigdea.model.Proceso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcesoRepository extends JpaRepository<Proceso, Long> {
}