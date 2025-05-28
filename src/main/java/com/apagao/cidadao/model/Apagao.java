package com.apagao.cidadao.model;

import jakarta.persistence.*;
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
    
    private String cep;

    private String rua;

    private String bairro;

    private String cidade;

    private String estado;
    
    private LocalDateTime dataHora;

    private String descricao;

    private String condicaoClimatica;

    private Double temperatura;

    @PrePersist
    public void definirDataAtual() {
        this.dataHora = LocalDateTime.now();
    }
}
