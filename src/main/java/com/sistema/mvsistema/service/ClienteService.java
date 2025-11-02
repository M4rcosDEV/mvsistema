package com.sistema.mvsistema.service;

import com.sistema.mvsistema.dto.ClienteBusca;
import com.sistema.mvsistema.model.Cliente;
import com.sistema.mvsistema.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Page<ClienteBusca> buscarClientes(String nome, String tipoPessoa, String cpfCnpj, Pageable pageable) {
//        String nomeFiltro = (nome != null && !nome.isBlank()) ? nome : null;
//        String tipoPessoaFiltro = (tipoPessoa != null && !tipoPessoa.isBlank()) ? tipoPessoa : null;
//        String cpfCnpjFiltro = (cpfCnpj != null && !cpfCnpj.isBlank()) ? cpfCnpj : null;

        Page<ClienteBusca> clientes = clienteRepository.buscarClientesComFiltros(nome, tipoPessoa, cpfCnpj, pageable);

//        return clientes.stream()
//                .map(c -> new ClienteBusca(
//                        c.getId(),
//                        c.getNome(),
//                        c.getCpfCnpj(),
//                        c.getTipoPessoa(),
//                        c.getTelefone()
//                ))
//                .toList();

        return clientes.map(c-> new ClienteBusca(
                c.getId(),
                c.getNome(),
                c.getCpfCnpj(),
                c.getTipoPessoa(),
                c.getTelefone()
        ));
    }

    public Cliente salvar(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public List<ClienteBusca> buscar(String nome) {
        List<ClienteBusca> clientes = clienteRepository.findByNomeStartsWith(nome);

        for (ClienteBusca cliente : clientes) {
            System.out.println(cliente);
        }

        return clientes;
    }
}
