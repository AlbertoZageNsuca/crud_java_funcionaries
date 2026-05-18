package com.grupoSI.crudApp.database.repository;

import com.grupoSI.crudApp.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Optional<User> findByEmail(String email);
}
