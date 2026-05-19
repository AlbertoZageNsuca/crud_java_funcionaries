package com.grupoSI.crudApp.service;

import com.grupoSI.crudApp.database.model.User;
import com.grupoSI.crudApp.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public boolean emailJaCadastrado(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public User registrar(User user) {
        if (emailJaCadastrado(user.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
