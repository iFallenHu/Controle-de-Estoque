package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.ProdutoEstoqueBaixoDTO;
import br.com.techsolucoes.ControleEstoque.repository.ProdutoRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RelatorioService {

    private final ProdutoRepository produtoRepository;

    public RelatorioService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ByteArrayInputStream gerarRelatorioEstoqueBaixo() {
        List<Object[]> resultados = produtoRepository.findProdutosComEstoqueBaixo();

        List<ProdutoEstoqueBaixoDTO> produtos = resultados.stream()
                .map(r -> new ProdutoEstoqueBaixoDTO(
                        ((Number) r[0]).longValue(),
                        (String) r[1],
                        ((Number) r[2]).intValue(),
                        ((Number) r[3]).intValue()
                ))
                .toList();

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // LOGOTIPO (texto estilizado)
            Font fontLogo = new Font(Font.HELVETICA, 26, Font.BOLD, new Color(30, 144, 255)); // Azul
            Paragraph logo = new Paragraph("Control+", fontLogo);
            logo.setAlignment(Element.ALIGN_LEFT);
            document.add(logo);

            document.add(Chunk.NEWLINE);

            // TÍTULO DO RELATÓRIO
            Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Relatório - Produtos com Estoque Baixo", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(Chunk.NEWLINE);

            // TABELA
            PdfPTable tabela = new PdfPTable(4);
            tabela.setWidthPercentage(100);

            // Cabeçalho com fundo cinza
            Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("ID", headFont));
            hcell.setBackgroundColor(new Color(100, 100, 100));
            tabela.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Produto", headFont));
            hcell.setBackgroundColor(new Color(100, 100, 100));
            tabela.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Quantidade Atual", headFont));
            hcell.setBackgroundColor(new Color(100, 100, 100));
            tabela.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Estoque Mínimo", headFont));
            hcell.setBackgroundColor(new Color(100, 100, 100));
            tabela.addCell(hcell);

            // Linhas da tabela
            for (ProdutoEstoqueBaixoDTO p : produtos) {
                tabela.addCell(p.getId().toString());
                tabela.addCell(p.getNome());
                tabela.addCell(p.getQuantidadeAtual().toString());
                tabela.addCell(p.getQuantidadeEstoqueMinimo().toString());
            }

            document.add(tabela);

            document.add(Chunk.NEWLINE);

            // Rodapé com data/hora
            Font fontRodape = new Font(Font.HELVETICA, 10, Font.ITALIC, Color.GRAY);
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            Paragraph rodape = new Paragraph("Gerado em: " + dataHora, fontRodape);
            rodape.setAlignment(Element.ALIGN_RIGHT);
            document.add(rodape);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
