package com.grupoSI.crudApp.service;

import com.grupoSI.crudApp.database.model.Departament;
import com.grupoSI.crudApp.database.model.Funcionary;
import com.grupoSI.crudApp.database.model.User;
import com.grupoSI.crudApp.database.repository.DepartamentRepository;
import com.grupoSI.crudApp.database.repository.FuncionaryRepository;
import com.grupoSI.crudApp.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class FuncionaryService {

    private final FuncionaryRepository funcionaryRepository;
    private final DepartamentRepository departamentRepository;
    private final UserRepository userRepository;

    public FuncionaryService(FuncionaryRepository funcionaryRepository, DepartamentRepository departamentRepository, UserRepository userRepository) {
        this.funcionaryRepository = funcionaryRepository;
        this.departamentRepository = departamentRepository;
        this.userRepository = userRepository;
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

    public List<Funcionary> findAll() {
        return funcionaryRepository.findAll();
    }

    public Funcionary findById(Long id) {
        return funcionaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com ID: " + id));
    }

    @Transactional
    public Funcionary update(Long id, Funcionary novosDados, Long departamentId) {
        Funcionary existente = findById(id);

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
    public void delete(Long id) {
        Funcionary funcionary = findById(id);
        funcionaryRepository.delete(funcionary);
    }
}