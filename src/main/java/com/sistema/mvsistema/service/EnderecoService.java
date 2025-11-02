package com.sistema.mvsistema.service;

import com.sistema.mvsistema.model.Cliente;
import com.sistema.mvsistema.model.Endereco;
import com.sistema.mvsistema.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class EnderecoService {

    @Autowired
    EnderecoRepository enderecoRepository;

    public Endereco salvar(Endereco endereco){
        return enderecoRepository.save(endereco);
    }
}
