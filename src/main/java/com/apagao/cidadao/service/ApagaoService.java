package com.apagao.cidadao.service;

import com.apagao.cidadao.model.Apagao;
import com.apagao.cidadao.model.ApagaoRequestDTO;
import com.apagao.cidadao.model.ApagaoResponseDTO;
import com.apagao.cidadao.repository.ApagaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApagaoService {

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
        return dto;
    }
}
