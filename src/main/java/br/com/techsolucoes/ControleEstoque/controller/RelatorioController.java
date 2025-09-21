package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.service.RelatorioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/api/relatorios/estoque-baixo/pdf")
    public ResponseEntity<byte[]> gerarPdf() {
        ByteArrayInputStream pdf = relatorioService.gerarRelatorioEstoqueBaixo();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estoque_baixo.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.readAllBytes());
    }
}
