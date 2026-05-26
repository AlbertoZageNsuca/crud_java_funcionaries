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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // ── Utilitários ──────────────────────────────────────────────────────────

    private User getAuthenticatedUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilizador autenticado não encontrado."));
    }

    /** Preenche os atributos comuns que a vista lista.html sempre precisa. */
    private void preencherModelLista(Model model, User user) {
        List<Funcionary> funcionarios = funcionaryService.findByUser(user.getId());

        double massaSalarial = funcionarios.stream()
                .mapToDouble(Funcionary::getSalary)
                .sum();

        long totalDepartamentos = funcionarios.stream()
                .map(f -> f.getDepartament() != null ? f.getDepartament().getName() : null)
                .filter(n -> n != null)
                .distinct()
                .count();

        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("massaSalarial", massaSalarial);
        model.addAttribute("totalDepartamentos", totalDepartamentos);
        model.addAttribute("departamentos", departamentRepository.findAll());
        model.addAttribute("abrirModalNovo", false);
        model.addAttribute("abrirModalEditar", false);

        // Garante que os objectos dos modais nunca são null no Thymeleaf
        if (!model.containsAttribute("novoFuncionario")) {
            model.addAttribute("novoFuncionario", new Funcionary());
        }
        if (!model.containsAttribute("funcionarioEdit")) {
            model.addAttribute("funcionarioEdit", new Funcionary());
        }
    }

    // ── GET /funcionarios ────────────────────────────────────────────────────

    @GetMapping
    public String listarTodos(Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        preencherModelLista(model, user);
        return "funcionarios/lista";
    }

    // ── POST /funcionarios/novo ──────────────────────────────────────────────

    @PostMapping("/novo")
    public String criar(@Valid @ModelAttribute("novoFuncionario") Funcionary funcionary,
                        BindingResult result,
                        @RequestParam(required = false) Long departamentId,
                        @AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam("foto") MultipartFile foto,
                        Model model,
                        RedirectAttributes redirectAttributes) throws IOException {

        // Processa foto antes de validar para não perder o ficheiro em caso de erro
        if (foto != null && !foto.isEmpty()) {
            String nomeArquivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();
            Path destino = Paths.get("uploads/" + nomeArquivo);
            Files.createDirectories(destino.getParent());
            foto.transferTo(destino);
            funcionary.setPhotoUrl("/uploads/" + nomeArquivo);
        }

        if (departamentId == null) {
            result.rejectValue("departament", "departament.obrigatorio", "Selecione um departamento.");
        }

        if (result.hasErrors()) {
            User user = getAuthenticatedUser(userDetails);
            preencherModelLista(model, user);
            model.addAttribute("abrirModalNovo", true);
            return "funcionarios/lista";
        }

        try {
            User user = getAuthenticatedUser(userDetails);
            funcionaryService.save(funcionary, departamentId, user.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário criado com sucesso!");
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "email.duplicado", e.getMessage());
            User user = getAuthenticatedUser(userDetails);
            preencherModelLista(model, user);
            model.addAttribute("abrirModalNovo", true);
            return "funcionarios/lista";
        }

        return "redirect:/funcionarios";
    }

    // ── GET /funcionarios/{id}/editar ────────────────────────────────────────

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id,
                                   Model model,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        preencherModelLista(model, user);
        model.addAttribute("funcionarioEdit", funcionaryService.findByIdAndUser(id, user.getId()));
        model.addAttribute("abrirModalEditar", id);
        return "funcionarios/lista";
    }

    // ── POST /funcionarios/{id}/editar ───────────────────────────────────────

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
            preencherModelLista(model, user);
            model.addAttribute("funcionarioEdit", novosDados);
            model.addAttribute("abrirModalEditar", id);
            return "funcionarios/lista";
        }

        try {
            User user = getAuthenticatedUser(userDetails);
            funcionaryService.update(id, novosDados, departamentId, user.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "email.duplicado", e.getMessage());
            User user = getAuthenticatedUser(userDetails);
            preencherModelLista(model, user);
            model.addAttribute("funcionarioEdit", novosDados);
            model.addAttribute("abrirModalEditar", id);
            return "funcionarios/lista";
        }

        return "redirect:/funcionarios";
    }

    // ── POST /funcionarios/{id}/eliminar ─────────────────────────────────────

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

    // ── POST /funcionarios/{id}/restaurar ────────────────────────────────────

    @PostMapping("/{id}/restaurar")
    public String restaurar(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        try {
            funcionaryService.restore(id);
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário restaurado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao restaurar: " + e.getMessage());
        }
        return "redirect:/funcionarios";
    }

    // ── POST /funcionarios/{id}/eliminar-permanente ──────────────────────────

    @PostMapping("/{id}/eliminar-permanente")
    public String eliminarPermanente(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        try {
            funcionaryService.deletePermanente(id);
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário eliminado permanentemente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro: " + e.getMessage());
        }
        return "redirect:/funcionarios/lixeira";
    }

    // ── GET /funcionarios/lixeira ────────────────────────────────────────────

    @GetMapping("/lixeira")
    public String lixeira(Model model,
                          @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);

        List<Funcionary> todos    = funcionaryService.findAllIncludingDeleted(user.getId());
        List<Funcionary> activos  = funcionaryService.findByUser(user.getId());
        List<Long> activosIds     = activos.stream().map(Funcionary::getId).collect(Collectors.toList());

        List<Funcionary> eliminados = todos.stream()
                .filter(f -> !activosIds.contains(f.getId()))
                .collect(Collectors.toList());

        model.addAttribute("eliminados", eliminados);
        return "funcionarios/lixeira";
    }
}