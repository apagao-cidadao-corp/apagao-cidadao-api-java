package com.apagao.cidadao.service;

import com.apagao.cidadao.model.Apagao;
import com.apagao.cidadao.model.ApagaoRequestDTO;
import com.apagao.cidadao.model.ApagaoResponseDTO;
import com.apagao.cidadao.repository.ApagaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
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
            apagao.setDescricao(dto.getDescricao());
            apagao.setCep(dto.getCep());
            apagao.setDataHora(LocalDateTime.now());
            return toResponseDTO(repository.save(apagao));
        }).orElseThrow(() -> new RuntimeException("Apagão não encontrado"));
    }

    private Apagao toEntity(ApagaoRequestDTO dto) {
        Apagao apagao = new Apagao();

        // Dados do endereço via CEP
        Map<String, String> endereco = viaCepService.consultar(dto.getCep());
        String rua = endereco.getOrDefault("logradouro", "");
        String bairro = endereco.getOrDefault("bairro", "");
        String cidade = endereco.getOrDefault("localidade", "");
        String estado = endereco.getOrDefault("uf", "");

        // Clima fixo de SP (pode ser substituído futuramente por geolocalização)
        double lat = -23.5505;
        double lon = -46.6333;

        Map<String, Object> clima = openMeteoService.getWeatherData(lat, lon);
        Map<String, Object> currentWeather = (Map<String, Object>) clima.get("current_weather");
        int codigoClima = (int) currentWeather.get("weathercode");
        String condicao = traduzirCondicao(codigoClima);
        Double temperatura = (Double) currentWeather.get("temperature");

        // Preencher entidade
        apagao.setCep(dto.getCep());
        apagao.setRua(rua);
        apagao.setBairro(bairro);
        apagao.setCidade(cidade);
        apagao.setEstado(estado);
        apagao.setDescricao(dto.getDescricao());
        apagao.setDataHora(LocalDateTime.now());
        apagao.setCondicaoClimatica(condicao);
        apagao.setTemperatura(temperatura);

        return apagao;
    }

    private ApagaoResponseDTO toResponseDTO(Apagao apagao) {
        ApagaoResponseDTO dto = new ApagaoResponseDTO();
        dto.setId(apagao.getId());
        dto.setCep(apagao.getCep());
        dto.setRua(apagao.getRua());
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
