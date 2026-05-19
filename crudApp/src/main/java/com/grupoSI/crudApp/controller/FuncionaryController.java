package com.grupoSI.crudApp.controller;

import com.grupoSI.crudApp.database.model.Funcionary;
import com.grupoSI.crudApp.database.model.User;
import com.grupoSI.crudApp.database.repository.DepartamentRepository;
import com.grupoSI.crudApp.database.repository.UserRepository;
import com.grupoSI.crudApp.service.FuncionaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionaryController {

    private final FuncionaryService funcionaryService;
    private final DepartamentRepository departamentRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("funcionarios", funcionaryService.findAll());
        model.addAttribute("novoFuncionario", new Funcionary());
        model.addAttribute("departamentos", departamentRepository.findAll());
        return "funcionarios/lista";
    }

    @PostMapping("/novo")
    public String criar(@Valid @ModelAttribute("novoFuncionario") Funcionary funcionary,
                        BindingResult result,
                        @RequestParam(required = false) Long departamentId,
                        @AuthenticationPrincipal UserDetails userDetails,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        if (departamentId == null) {
            result.rejectValue("departament", "departament.obrigatorio", "Selecione um departamento.");
        }

        if (result.hasErrors()) {
            model.addAttribute("funcionarios", funcionaryService.findAll());
            model.addAttribute("departamentos", departamentRepository.findAll());
            model.addAttribute("abrirModalNovo", true);
            return "funcionarios/lista";
        }

        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));
            funcionaryService.save(funcionary, departamentId, user.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário criado com sucesso!");
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "email.duplicado", e.getMessage());
            model.addAttribute("funcionarios", funcionaryService.findAll());
            model.addAttribute("departamentos", departamentRepository.findAll());
            model.addAttribute("abrirModalNovo", true);
            return "funcionarios/lista";
        }
        return "redirect:/funcionarios";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("funcionarios", funcionaryService.findAll());
        model.addAttribute("novoFuncionario", new Funcionary());
        model.addAttribute("funcionarioEdit", funcionaryService.findById(id));
        model.addAttribute("departamentos", departamentRepository.findAll());
        model.addAttribute("abrirModalEditar", id);
        return "funcionarios/lista";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("funcionarioEdit") Funcionary novosDados,
                            BindingResult result,
                            @RequestParam(required = false) Long departamentId,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (departamentId == null) {
            result.rejectValue("departament", "departament.obrigatorio", "Selecione um departamento.");
        }

        if (result.hasErrors()) {
            model.addAttribute("funcionarios", funcionaryService.findAll());
            model.addAttribute("novoFuncionario", new Funcionary());
            model.addAttribute("departamentos", departamentRepository.findAll());
            model.addAttribute("abrirModalEditar", id);
            return "funcionarios/lista";
        }

        try {
            funcionaryService.update(id, novosDados, departamentId);
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "email.duplicado", e.getMessage());
            model.addAttribute("funcionarios", funcionaryService.findAll());
            model.addAttribute("novoFuncionario", new Funcionary());
            model.addAttribute("departamentos", departamentRepository.findAll());
            model.addAttribute("abrirModalEditar", id);
            return "funcionarios/lista";
        }
        return "redirect:/funcionarios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            funcionaryService.delete(id);
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }
}
