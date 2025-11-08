package com.sistema.mvsistema.service;

import com.sistema.mvsistema.dto.ClienteBusca;
import com.sistema.mvsistema.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Page<ClienteBusca> buscarClientes(String nome, String tipoPessoa, String cpfCnpj, Pageable pageable) {

        Page<ClienteBusca> clientes = clienteRepository.buscarClientesComFiltros(nome, tipoPessoa, cpfCnpj, pageable);

        return clientes.map(c-> new ClienteBusca(
                c.getId(),
                c.getNome(),
                c.getCpfCnpj(),
                c.getTipoPessoa(),
                c.getTelefone()
        ));
    }
}
