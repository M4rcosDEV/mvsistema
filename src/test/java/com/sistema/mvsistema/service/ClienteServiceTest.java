package com.sistema.mvsistema.service;

import com.sistema.mvsistema.dto.ClienteBusca;
import com.sistema.mvsistema.entity.Cliente;
import com.sistema.mvsistema.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClienteServiceTest {

    @Test
    void deveBuscarClientePorNome() {
        // mock do repositório
        ClienteRepository repo = mock(ClienteRepository.class);

        // dado de exemplo
        ClienteBusca cliente = new ClienteBusca(1L, "Marcos", "12345678900", "Física", "999999999");

        // paginação
        int itensPorPagina = 10;
        Pageable pageableInicial = PageRequest.of(0, itensPorPagina);

        // Page<ClienteBusca> com um item
        Page<ClienteBusca> paginaSimulada = new PageImpl<>(List.of(cliente));

        // comportamento simulado
        when(repo.buscarClientesComFiltros("Marcos", null, null, pageableInicial))
                .thenReturn(paginaSimulada);

        // criar o service com esse repo mockado
        ClienteService service = new ClienteService(repo);

        // chamada real
        Page<ClienteBusca> resultado = service.buscarClientes("Marcos", null, null, pageableInicial);

        // asserts
        assert resultado.getContent().size() == 1;
        assert resultado.getContent().get(0).getNome().equals("Marcos");

    }

    @Test
    void deveFalharQuandoNaoEncontrarClientes() {
        ClienteRepository repo = mock(ClienteRepository.class);
        Pageable pageable = PageRequest.of(0, 10);

        when(repo.buscarClientesComFiltros("Marcos", null, null, pageable))
                .thenReturn(Page.empty());

        Page<ClienteBusca> resultado = repo.buscarClientesComFiltros("Marcos", null, null, pageable);

        assertTrue(resultado.isEmpty(), "A lista deveria estar vazia");
    }

    @Test
    void deveBuscarUmClienteFetchEndereco(){
        ClienteRepository repo = mock(ClienteRepository.class);

        Cliente cli = new Cliente();
        cli.setId(1L);
        cli.setNome("Marcos");
        Optional<Cliente> clienteMock = Optional.of(cli);

        when(repo.findByIdWithEnderecos(1L)).thenReturn(clienteMock);

        Optional<Cliente> resultado = repo.findByIdWithEnderecos(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Marcos", resultado.get().getNome());
    }
}