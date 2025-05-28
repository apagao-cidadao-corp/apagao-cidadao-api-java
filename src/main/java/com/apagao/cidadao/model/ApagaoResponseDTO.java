package com.apagao.cidadao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApagaoResponseDTO {

    @Schema(example = "1", description = "ID gerado para o apagão")
    private Long id;

    @Schema(example = "Centro")
    private String bairro;

    @Schema(example = "São Paulo")
    private String cidade;

    @Schema(example = "SP")
    private String estado;

    @Schema(example = "2025-05-27T19:00:00")
    private LocalDateTime dataHora;

    @Schema(example = "Apagão total por mais de 2 horas")
    private String descricao;
}
