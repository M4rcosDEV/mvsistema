package com.sistema.mvsistema.service;

import com.sistema.mvsistema.model.Usuario;
import com.sistema.mvsistema.repository.UsuarioRepository;
import com.sistema.mvsistema.util.CriptografiaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CacheService cacheService;

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorNome(String nome){
        return usuarioRepository.findByNome(nome);
    }

    public boolean validacaoLogin(String nome, String senha){

        Optional<Usuario> usuarioOptional = usuarioRepository.findByNome(nome);

        if (usuarioOptional.isEmpty()) {
            System.out.println("Informações incorretas: Usuário não existe.");
            return false;
        }
        Usuario userInfo =  usuarioOptional.get();

        if(CriptografiaUtil.verificarSenha(senha, userInfo.getSenha())){
            SessaoUsuario.setUsuarioLogado(userInfo);
            System.out.println("Usuário logado: " + userInfo.getNome());
            cacheService.carregarDados();

            return true;
        }else{
            System.out.println("Informações incorretas: senha inválida.");
            return false;
        }

    }
}
