package com.sistema.mvsistema.service;

import com.sistema.mvsistema.dto.EnderecoDTO;
import com.sistema.mvsistema.feign.ViaCepClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CepService {
    private final ViaCepClient viaCepClient;


    public CepService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public EnderecoDTO buscarEnderecoPorCep(String cep) {
        return viaCepClient.buscarCep(cep);
    }
}
