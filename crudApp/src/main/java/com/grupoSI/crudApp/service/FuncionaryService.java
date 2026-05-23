package com.grupoSI.crudApp.service;

import com.grupoSI.crudApp.database.model.Departament;
import com.grupoSI.crudApp.database.model.Funcionary;
import com.grupoSI.crudApp.database.model.User;
import com.grupoSI.crudApp.database.repository.DepartamentRepository;
import com.grupoSI.crudApp.database.repository.FuncionaryRepository;
import com.grupoSI.crudApp.database.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FuncionaryService {

    private final FuncionaryRepository funcionaryRepository;
    private final DepartamentRepository departamentRepository;
    private final UserRepository userRepository;

    public FuncionaryService(FuncionaryRepository funcionaryRepository,
                             DepartamentRepository departamentRepository,
                             UserRepository userRepository) {
        this.funcionaryRepository = funcionaryRepository;
        this.departamentRepository = departamentRepository;
        this.userRepository = userRepository;
    }

    // NOVO — lista só os funcionários do utilizador autenticado
    public List<Funcionary> findByUser(Long userId) {
        return funcionaryRepository.findByUserId(userId);
    }

    // Mantido para uso interno
    public Funcionary findById(Long id) {
        return funcionaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com ID: " + id));
    }

    // NOVO — busca por ID garantindo que pertence ao utilizador (segurança)
    public Funcionary findByIdAndUser(Long id, Long userId) {
        return funcionaryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado ou sem permissão."));
    }

    @Transactional
    public Funcionary save(Funcionary funcionary, Long departamentId, Long userId) {
        Departament departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + departamentId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado com ID: " + userId));

        if (funcionaryRepository.existsByEmail(funcionary.getEmail())) {
            throw new IllegalArgumentException("Já existe um funcionário com este e-mail.");
        }

        funcionary.setDepartament(departament);
        funcionary.setUser(user);
        return funcionaryRepository.save(funcionary);
    }

    @Transactional
    public Funcionary update(Long id, Funcionary novosDados, Long departamentId, Long userId) {
        // Garante que o funcionário pertence ao utilizador autenticado
        Funcionary existente = findByIdAndUser(id, userId);

        Departament departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + departamentId));

        if (funcionaryRepository.existsByEmailAndIdNot(novosDados.getEmail(), id)) {
            throw new IllegalArgumentException("Já existe outro funcionário com este e-mail.");
        }

        existente.setName(novosDados.getName());
        existente.setLocalization(novosDados.getLocalization());
        existente.setOcupation(novosDados.getOcupation());
        existente.setBirthDate(novosDados.getBirthDate());
        existente.setEmail(novosDados.getEmail());
        existente.setPhoneNumber(novosDados.getPhoneNumber());
        existente.setSalary(novosDados.getSalary());
        existente.setDepartament(departament);
        return funcionaryRepository.save(existente);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        // Garante que o funcionário pertence ao utilizador autenticado
        Funcionary funcionary = findByIdAndUser(id, userId);
        funcionaryRepository.delete(funcionary);
    }
}