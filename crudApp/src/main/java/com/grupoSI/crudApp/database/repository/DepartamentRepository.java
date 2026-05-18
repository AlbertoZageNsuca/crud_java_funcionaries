package com.grupoSI.crudApp.database.repository;

import com.grupoSI.crudApp.database.model.Departament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentRepository extends JpaRepository<Departament, Long> {
}
