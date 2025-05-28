package com.apagao.cidadao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApagaoRequestDTO {

    @NotBlank(message = "O cep é obrigatório.")
    private String cep;

    @NotBlank(message = "A descrição é obrigatória.")
    @Schema(example = "Apagão total por mais de 2 horas", description = "Descrição adicional do ocorrido")
    private String descricao;

}