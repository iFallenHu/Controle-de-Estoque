package br.com.techsolucoes.ControleEstoque.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "fornecedor")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 18, nullable = false, unique = true) // formato 00.000.000/0000-00
    private String cnpj;

    @Column(length = 20, nullable = false)
    private String telefone;

    @Column(length = 100, nullable = false, unique = true)
    private String email;
}
