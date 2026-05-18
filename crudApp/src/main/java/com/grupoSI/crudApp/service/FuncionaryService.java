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
@RequiredArgsConstructor
public class FuncionaryService {

    private final FuncionaryRepository funcionaryRepository;
    private final DepartamentRepository departamentRepository;
    private final UserRepository userRepository;

    // ─── CREATE ───────────────────────────────────────────────────────────────

    /**
     * Cria e guarda um novo funcionário na base de dados.
     *
     * @param funcionary     Objeto Funcionary com os dados preenchidos
     * @param departamentId  ID do departamento ao qual o funcionário pertence
     * @param userId         ID do utilizador associado ao funcionário
     * @return               O funcionário criado (com ID gerado)
     */
    @Transactional
    public Funcionary save(Funcionary funcionary, Long departamentId, Long userId) {
        Departament departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + departamentId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado com ID: " + userId));

        funcionary.setDepartament(departament);
        funcionary.setUser(user);

        return funcionaryRepository.save(funcionary);
    }

    // ─── READ ─────────────────────────────────────────────────────────────────

    /**
     * Retorna todos os funcionários (não eliminados pelo SoftDelete).
     *
     * @return Lista de funcionários
     */
    public List<Funcionary> findAll() {
        return funcionaryRepository.findAll();
    }

    /**
     * Busca um funcionário pelo seu ID.
     *
     * @param id  ID do funcionário
     * @return    O funcionário encontrado
     * @throws RuntimeException se não encontrado
     */
    public Funcionary findById(Long id) {
        return funcionaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com ID: " + id));
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    /**
     * Atualiza os dados de um funcionário existente.
     *
     * @param id             ID do funcionário a atualizar
     * @param novosDados     Objeto com os novos dados
     * @param departamentId  Novo departamento (pode ser o mesmo)
     * @return               O funcionário atualizado
     */
    @Transactional
    public Funcionary update(Long id, Funcionary novosDados, Long departamentId) {
        Funcionary funcionaryExistente = findById(id);

        Departament departament = departamentRepository.findById(departamentId)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado com ID: " + departamentId));

        // Atualiza apenas os campos editáveis
        funcionaryExistente.setName(novosDados.getName());
        funcionaryExistente.setLocalization(novosDados.getLocalization());
        funcionaryExistente.setOcupation(novosDados.getOcupation());
        funcionaryExistente.setBirthDate(novosDados.getBirthDate());
        funcionaryExistente.setEmail(novosDados.getEmail());
        funcionaryExistente.setPhoneNumber(novosDados.getPhoneNumber());
        funcionaryExistente.setSalary(novosDados.getSalary());
        funcionaryExistente.setProfileImg(novosDados.getProfileImg());
        funcionaryExistente.setDepartament(departament);

        return funcionaryRepository.save(funcionaryExistente);
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Elimina (soft delete) um funcionário pelo ID.
     * O Hibernate com @SoftDelete não apaga fisicamente o registo —
     * apenas marca a coluna "deleted" como true.
     *
     * @param id  ID do funcionário a eliminar
     */
    @Transactional
    public void delete(Long id) {
        Funcionary funcionary = findById(id);
        funcionaryRepository.delete(funcionary);
    }
}
