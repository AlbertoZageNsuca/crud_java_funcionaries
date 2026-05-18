package com.grupoSI.crudApp.database.repository;

import com.grupoSI.crudApp.database.model.Funcionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionaryRepository extends JpaRepository<Funcionary, Long> {
}
