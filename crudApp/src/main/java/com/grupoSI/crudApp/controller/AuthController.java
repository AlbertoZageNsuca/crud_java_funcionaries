package com.grupoSI.crudApp.controller;

import com.grupoSI.crudApp.database.model.User;
import com.grupoSI.crudApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String exibirLogin(@RequestParam(required = false) String error,
                              @RequestParam(required = false) String logout,
                              Model model) {
        if (error != null) {
            model.addAttribute("erroLogin", "E-mail ou senha incorretos. Tente novamente.");
        }
        if (logout != null) {
            model.addAttribute("mensagemLogout", "Sessão encerrada com sucesso.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String exibirRegistro(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processarRegistro(@Valid @ModelAttribute("user") User user,
                                    BindingResult result,
                                    Model model) {
        if (userService.emailJaCadastrado(user.getEmail())) {
            result.rejectValue("email", "email.duplicado", "Este e-mail já está cadastrado.");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registrar(user);
            model.addAttribute("sucesso", "Conta criada com sucesso! Faça login para continuar.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("erroGeral", "Erro ao criar conta. Tente novamente.");
            return "auth/register";
        }
    }
}
