package com.apagao.cidadao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApagaoRequestDTO {

    @NotBlank(message = "O bairro é obrigatório.")
    @Schema(example = "Centro", description = "Nome do bairro onde ocorreu o apagão")
    private String bairro;

    @NotBlank(message = "O cep é obrigatório.")
    private String cep;

    @NotBlank(message = "A cidade é obrigatória.")
    @Schema(example = "São Paulo", description = "Nome da cidade")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    @Schema(example = "SP", description = "Sigla do estado")
    private String estado;

    @Schema(example = "2025-05-27T19:00:00", description = "Data e hora do apagão no formato ISO")
    private LocalDateTime dataHora;

    @NotBlank(message = "A descrição é obrigatória.")
    @Schema(example = "Apagão total por mais de 2 horas", description = "Descrição adicional do ocorrido")
    private String descricao;

}