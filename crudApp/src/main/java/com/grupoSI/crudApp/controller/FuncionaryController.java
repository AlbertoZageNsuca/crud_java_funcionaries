package com.grupoSI.crudApp.controller;

import com.grupoSI.crudApp.database.model.Departament;
import com.grupoSI.crudApp.database.model.Funcionary;
import com.grupoSI.crudApp.database.repository.DepartamentRepository;
import com.grupoSI.crudApp.service.FuncionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionaryController {

    private final FuncionaryService funcionaryService;
    private final DepartamentRepository departamentRepository;

    // ─── READ (Listar todos) ──────────────────────────────────────────────────

    /**
     * GET /funcionarios
     * Lista todos os funcionários e envia para a view.
     */
    @GetMapping
    public String listarTodos(Model model) {
        List<Funcionary> funcionarios = funcionaryService.findAll();
        model.addAttribute("funcionarios", funcionarios);
        return "funcionarios/lista"; // src/main/resources/templates/funcionarios/lista.html
    }

    // ─── READ (Ver um) ────────────────────────────────────────────────────────

    /**
     * GET /funcionarios/{id}
     * Mostra os detalhes de um único funcionário.
     */
    @GetMapping("/{id}")
    public String ver(@PathVariable Long id, Model model) {
        Funcionary funcionary = funcionaryService.findById(id);
        model.addAttribute("funcionary", funcionary);
        return "funcionarios/detalhe"; // src/main/resources/templates/funcionarios/detalhe.html
    }

    // ─── CREATE (Formulário) ──────────────────────────────────────────────────

    /**
     * GET /funcionarios/novo
     * Abre o formulário de criação.
     */
    @GetMapping("/novo")
    public String formularioCriar(Model model) {
        model.addAttribute("funcionary", new Funcionary());
        model.addAttribute("departamentos", departamentRepository.findAll());
        return "funcionarios/formulario"; // src/main/resources/templates/funcionarios/formulario.html
    }

    /**
     * POST /funcionarios/novo
     * Recebe os dados do formulário e cria o funcionário.
     *
     * @param funcionary    Objeto populado pelo Thymeleaf
     * @param departamentId ID do departamento selecionado no formulário
     * @param userDetails   Utilizador autenticado (para pegar o user_id)
     */
    @PostMapping("/novo")
    public String criar(
            @ModelAttribute Funcionary funcionary,
            @RequestParam Long departamentId,
            @RequestParam Long userId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            funcionaryService.save(funcionary, departamentId, userId);
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }

    // ─── UPDATE (Formulário) ──────────────────────────────────────────────────

    /**
     * GET /funcionarios/{id}/editar
     * Abre o formulário de edição com os dados atuais.
     */
    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Funcionary funcionary = funcionaryService.findById(id);
        List<Departament> departamentos = departamentRepository.findAll();
        model.addAttribute("funcionary", funcionary);
        model.addAttribute("departamentos", departamentos);
        return "funcionarios/formulario"; // Reutiliza o mesmo formulário
    }

    /**
     * POST /funcionarios/{id}/editar
     * Recebe os novos dados e atualiza o funcionário.
     */
    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Funcionary novosDados,
            @RequestParam Long departamentId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            funcionaryService.update(id, novosDados, departamentId);
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    /**
     * POST /funcionarios/{id}/eliminar
     * Elimina (soft delete) o funcionário com o ID indicado.
     * Usa POST em vez de DELETE para compatibilidade com formulários HTML.
     */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            funcionaryService.delete(id);
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário eliminado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao eliminar: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }
}
