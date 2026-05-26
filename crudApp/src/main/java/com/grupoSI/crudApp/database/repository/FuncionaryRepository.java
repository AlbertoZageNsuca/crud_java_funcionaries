package com.grupoSI.crudApp.database.repository;

import com.grupoSI.crudApp.database.model.Funcionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionaryRepository extends JpaRepository<Funcionary, Long> {

    // @SoftDelete já filtra deleted=false automaticamente nestas queries
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    Optional<Funcionary> findByEmail(String email);
    List<Funcionary> findByUserId(Long userId);
    Optional<Funcionary> findByIdAndUserId(Long id, Long userId);
    List<Funcionary> findByUserEmail(String email);

    // Lista activos + eliminados (para painel de lixo/restauro)
    @Query(value = "SELECT * FROM funcionaries WHERE user_id = :userId", nativeQuery = true)
    List<Funcionary> findAllIncludingDeletedByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM funcionaries WHERE id = :id", nativeQuery = true)
    void deletePermanente(@Param("id") Long id);
    // Restaura um funcionário eliminado
    @Modifying
    @Query(value = "UPDATE funcionaries SET deleted = false WHERE id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);
}