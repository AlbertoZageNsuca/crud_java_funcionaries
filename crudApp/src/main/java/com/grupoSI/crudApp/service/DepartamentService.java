package com.grupoSI.crudApp.service;

import com.grupoSI.crudApp.database.model.Departament;
import com.grupoSI.crudApp.database.repository.DepartamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentService {

    private final DepartamentRepository departamentRepository;

    public List<Departament> findAll() {
        return departamentRepository.findAll();
    }

    public Departament findById(Long id) {
        return departamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + id));
    }

    @Transactional
    public Departament save(Departament departament) {
        if (departamentRepository.existsByName(departament.getName())) {
            throw new IllegalArgumentException("Já existe um departamento com este nome.");
        }
        return departamentRepository.save(departament);
    }

    @Transactional
    public Departament update(Long id, Departament novosDados) {
        Departament existente = findById(id);
        if (departamentRepository.existsByNameAndIdNot(novosDados.getName(), id)) {
            throw new IllegalArgumentException("Já existe outro departamento com este nome.");
        }
        existente.setName(novosDados.getName());
        return departamentRepository.save(existente);
    }

    @Transactional
    public void delete(Long id) {
        Departament dep = findById(id);
        if (!dep.getFuncionaryList().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir um departamento que possui funcionários.");
        }
        departamentRepository.delete(dep);
    }
}
