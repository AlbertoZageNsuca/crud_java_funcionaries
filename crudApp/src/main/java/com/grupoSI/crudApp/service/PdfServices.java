package com.grupoSI.crudApp.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.stereotype.Service;
import com.grupoSI.crudApp.database.model.Funcionary;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfServices {

    private final TemplateEngine templateEngine;

    public PdfServices(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] gerarPdfFuncionarios(List<Funcionary> funcionarios) {

        Context context = new Context();
        context.setVariable("funcionarios", funcionarios);
        double massaSalarial = funcionarios.stream()
                .mapToDouble(Funcionary::getSalary)
                .sum();
        context.setVariable("massaSalarial", massaSalarial);

        String html = templateEngine.process("pdf/funcionarios", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            return os.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}