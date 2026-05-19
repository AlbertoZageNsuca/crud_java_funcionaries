package com.grupoSI.crudApp.database.repository;

import com.grupoSI.crudApp.database.model.Departament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartamentRepository extends JpaRepository<Departament, Long> {
    Optional<Departament> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
