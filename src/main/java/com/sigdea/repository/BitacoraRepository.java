
package com.sigdea.repository;

import com.sigdea.model.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BitacoraRepository
        extends JpaRepository<Bitacora, Long> {

}

