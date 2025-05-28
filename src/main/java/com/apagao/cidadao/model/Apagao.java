package com.apagao.cidadao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Apagao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O bairro é obrigatório.")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    private String estado;

    private LocalDateTime dataHora;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @PrePersist
    public void definirDataAtual() {
        this.dataHora = LocalDateTime.now();
    }
}
