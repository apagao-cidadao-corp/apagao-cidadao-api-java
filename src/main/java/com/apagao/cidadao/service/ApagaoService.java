package com.apagao.cidadao.service;

import com.apagao.cidadao.model.Apagao;
import com.apagao.cidadao.model.ApagaoRequestDTO;
import com.apagao.cidadao.model.ApagaoResponseDTO;
import com.apagao.cidadao.repository.ApagaoRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.apagao.cidadao.service.ViaCepService;
import com.apagao.cidadao.service.OpenMeteoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApagaoService {
    @Autowired
    private ViaCepService viaCepService;

    @Autowired
    private OpenMeteoService openMeteoService;


    @Autowired
    private ApagaoRepository repository;

    public List<ApagaoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public Optional<ApagaoResponseDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::toResponseDTO);
    }

    public ApagaoResponseDTO salvar(ApagaoRequestDTO dto) {
        Apagao apagao = toEntity(dto);
        return toResponseDTO(repository.save(apagao));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public ApagaoResponseDTO atualizar(Long id, ApagaoRequestDTO dto) {
        return repository.findById(id).map(apagao -> {
            apagao.setBairro(dto.getBairro());
            apagao.setCidade(dto.getCidade());
            apagao.setEstado(dto.getEstado());
            apagao.setDataHora(dto.getDataHora());
            apagao.setDescricao(dto.getDescricao());
            return toResponseDTO(repository.save(apagao));
        }).orElseThrow(() -> new RuntimeException("Apagão não encontrado"));
    }

    private Apagao toEntity(ApagaoRequestDTO dto) {
        Apagao apagao = new Apagao();
        // Obter dados de localização do ViaCEP
        Map<String, String> endereco = viaCepService.consultar(dto.getCep());
        String cidade = endereco.getOrDefault("localidade", dto.getCidade());
        String estado = endereco.getOrDefault("uf", dto.getEstado());

        // Consultar clima pela cidade e estado
        
            // Coordenadas aproximadas (substituir por integração real se necessário)
            double lat = -23.5505; // exemplo: São Paulo
            double lon = -46.6333;

            Map<String, Object> clima = openMeteoService.getWeatherData(lat, lon);
            Map<String, Object> currentWeather = (Map<String, Object>) clima.get("current_weather");
            int codigoClima = (int) currentWeather.get("weathercode");
            String condicao = traduzirCondicao(codigoClima);
            Double temperatura = (Double) currentWeather.get("temperature");

        apagao.setCidade(cidade);
        apagao.setEstado(estado);
        apagao.setDataHora(LocalDateTime.now());
        apagao.setCondicaoClimatica(condicao);
        apagao.setTemperatura(temperatura);
        apagao.setBairro(dto.getBairro());
        apagao.setCidade(dto.getCidade());
        apagao.setEstado(dto.getEstado());
        apagao.setDataHora(dto.getDataHora());
        apagao.setDescricao(dto.getDescricao());
        return apagao;
    }

    private ApagaoResponseDTO toResponseDTO(Apagao apagao) {
        ApagaoResponseDTO dto = new ApagaoResponseDTO();
        dto.setId(apagao.getId());
        dto.setBairro(apagao.getBairro());
        dto.setCidade(apagao.getCidade());
        dto.setEstado(apagao.getEstado());
        dto.setDataHora(apagao.getDataHora());
        dto.setDescricao(apagao.getDescricao());
        dto.setCondicaoClimatica(apagao.getCondicaoClimatica());
        dto.setTemperatura(apagao.getTemperatura());
        return dto;
    }


private String traduzirCondicao(int codigo) {
        return switch (codigo) {
            case 0 -> "Céu limpo";
            case 1 -> "Principalmente limpo";
            case 2 -> "Parcialmente nublado";
            case 3 -> "Nublado";
            case 45 -> "Névoa";
            case 48 -> "Névoa com cristais de gelo";
            case 51 -> "Chuvisco fraco";
            case 53 -> "Chuvisco moderado";
            case 55 -> "Chuvisco forte";
            case 61 -> "Chuva leve";
            case 63 -> "Chuva moderada";
            case 65 -> "Chuva forte";
            case 80 -> "Pancadas fracas";
            case 81 -> "Pancadas moderadas";
            case 82 -> "Pancadas fortes";
            default -> "Desconhecido";
        };
    }
}