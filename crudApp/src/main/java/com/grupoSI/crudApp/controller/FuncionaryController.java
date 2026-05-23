package com.grupoSI.crudApp.controller;

import com.grupoSI.crudApp.database.model.Funcionary;
import com.grupoSI.crudApp.database.model.User;
import com.grupoSI.crudApp.database.repository.DepartamentRepository;
import com.grupoSI.crudApp.database.repository.UserRepository;
import com.grupoSI.crudApp.service.FuncionaryService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/funcionarios")
public class FuncionaryController {

    private final FuncionaryService funcionaryService;
    private final DepartamentRepository departamentRepository;
    private final UserRepository userRepository;

    public FuncionaryController(FuncionaryService funcionaryService,
                                DepartamentRepository departamentRepository,
                                UserRepository userRepository) {
        this.funcionaryService = funcionaryService;
        this.departamentRepository = departamentRepository;
        this.userRepository = userRepository;
    }

    // Método auxiliar — obtém o utilizador autenticado da BD
    private User getAuthenticatedUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilizador autenticado não encontrado."));
    }

    @GetMapping
    public String listarTodos(Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        // Só mostra os funcionários deste utilizador
        model.addAttribute("funcionarios", funcionaryService.findByUser(user.getId()));
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
            User user = getAuthenticatedUser(userDetails);
            model.addAttribute("funcionarios", funcionaryService.findByUser(user.getId()));
            model.addAttribute("departamentos", departamentRepository.findAll());
            model.addAttribute("abrirModalNovo", true);
            return "funcionarios/lista";
        }

        try {
            User user = getAuthenticatedUser(userDetails);
            funcionaryService.save(funcionary, departamentId, user.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário criado com sucesso!");
        } catch (IllegalArgumentException e) {
            User user = getAuthenticatedUser(userDetails);
            result.rejectValue("email", "email.duplicado", e.getMessage());
            model.addAttribute("funcionarios", funcionaryService.findByUser(user.getId()));
            model.addAttribute("departamentos", departamentRepository.findAll());
            model.addAttribute("abrirModalNovo", true);
            return "funcionarios/lista";
        }
        return "redirect:/funcionarios";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id,
                                   Model model,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        model.addAttribute("funcionarios", funcionaryService.findByUser(user.getId()));
        model.addAttribute("novoFuncionario", new Funcionary());
        // Verifica que o funcionário pertence ao utilizador
        model.addAttribute("funcionarioEdit", funcionaryService.findByIdAndUser(id, user.getId()));
        model.addAttribute("departamentos", departamentRepository.findAll());
        model.addAttribute("abrirModalEditar", id);
        return "funcionarios/lista";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("funcionarioEdit") Funcionary novosDados,
                            BindingResult result,
                            @RequestParam(required = false) Long departamentId,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (departamentId == null) {
            result.rejectValue("departament", "departament.obrigatorio", "Selecione um departamento.");
        }

        if (result.hasErrors()) {
            User user = getAuthenticatedUser(userDetails);
            model.addAttribute("funcionarios", funcionaryService.findByUser(user.getId()));
            model.addAttribute("novoFuncionario", new Funcionary());
            model.addAttribute("departamentos", departamentRepository.findAll());
            model.addAttribute("abrirModalEditar", id);
            return "funcionarios/lista";
        }

        try {
            User user = getAuthenticatedUser(userDetails);
            funcionaryService.update(id, novosDados, departamentId, user.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            User user = getAuthenticatedUser(userDetails);
            result.rejectValue("email", "email.duplicado", e.getMessage());
            model.addAttribute("funcionarios", funcionaryService.findByUser(user.getId()));
            model.addAttribute("novoFuncionario", new Funcionary());
            model.addAttribute("departamentos", departamentRepository.findAll());
            model.addAttribute("abrirModalEditar", id);
            return "funcionarios/lista";
        }
        return "redirect:/funcionarios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = getAuthenticatedUser(userDetails);
            funcionaryService.delete(id, user.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }
}