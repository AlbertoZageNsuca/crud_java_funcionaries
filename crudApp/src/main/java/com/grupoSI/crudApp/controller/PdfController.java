package com.grupoSI.crudApp.controller;

import com.grupoSI.crudApp.database.model.Funcionary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.grupoSI.crudApp.service.PdfServices;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.grupoSI.crudApp.database.repository.FuncionaryRepository;
import java.util.*;



@RestController
public class PdfController {

    private final FuncionaryRepository repo;
    private final PdfServices pdfService;

    public PdfController(FuncionaryRepository repo, PdfServices pdfService) {
        this.repo = repo;
        this.pdfService = pdfService;
    }

    @GetMapping("/funcionarios/pdf")
    public ResponseEntity<byte[]> gerarPdf() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailUser = auth.getName(); // normalmente o username/email

        List<Funcionary> funcionarios = repo.findByUserEmail(emailUser);

        byte[] pdf = pdfService.gerarPdfFuncionarios(funcionarios);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=funcionarios.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}