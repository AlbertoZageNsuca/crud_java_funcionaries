package com.grupoSI.crudApp.controller;

import com.grupoSI.crudApp.database.model.Departament;
import com.grupoSI.crudApp.service.DepartamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/departamentos")
@RequiredArgsConstructor
public class DepartamentController {

    private final DepartamentService departamentService;



    @GetMapping
    public String listar(Model model) {
        model.addAttribute("departamentos", departamentService.findAll());
        model.addAttribute("novoDepartamento", new Departament());
        model.addAttribute("departamentoEdit", new Departament());
        model.addAttribute("abrirModalNovo", false);
        model.addAttribute("abrirModalEditar", false);
        return "departamentos/lista";
    }

    @PostMapping("/novo")
    public String criar(@Valid @ModelAttribute("novoDepartamento") Departament departament,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("departamentos", departamentService.findAll());
            model.addAttribute("abrirModalNovo", true);
            return "departamentos/lista";
        }

        try {
            departamentService.save(departament);
            redirectAttributes.addFlashAttribute("sucesso", "Departamento criado com sucesso!");
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "name.duplicado", e.getMessage());
            model.addAttribute("departamentos", departamentService.findAll());
            model.addAttribute("abrirModalNovo", true);
            return "departamentos/lista";
        }
        return "redirect:/departamentos";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("departamentos", departamentService.findAll());
        model.addAttribute("novoDepartamento", new Departament());
        model.addAttribute("departamentoEdit", departamentService.findById(id));
        model.addAttribute("abrirModalEditar", id);
        return "departamentos/lista";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("departamentoEdit") Departament novosDados,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("departamentos", departamentService.findAll());
            model.addAttribute("novoDepartamento", new Departament());
            model.addAttribute("abrirModalEditar", id);
            return "departamentos/lista";
        }

        try {
            departamentService.update(id, novosDados);
            redirectAttributes.addFlashAttribute("sucesso", "Departamento atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "name.duplicado", e.getMessage());
            model.addAttribute("departamentos", departamentService.findAll());
            model.addAttribute("novoDepartamento", new Departament());
            model.addAttribute("abrirModalEditar", id);
            return "departamentos/lista";
        }
        return "redirect:/departamentos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departamentService.delete(id);
            redirectAttributes.addFlashAttribute("sucesso", "Departamento excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/departamentos";
    }
}
