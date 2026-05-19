package com.grupoSI.crudApp.database.repository;

import com.grupoSI.crudApp.database.model.Funcionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionaryRepository extends JpaRepository<Funcionary, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    Optional<Funcionary> findByEmail(String email);
}
