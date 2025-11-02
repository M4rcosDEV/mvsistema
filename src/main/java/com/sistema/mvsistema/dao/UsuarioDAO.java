package com.sistema.mvsistema.dao;


import com.sistema.mvsistema.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario buscarUsuarioNome(String nome){
        String query = "SELECT * FROM usuarios WHERE nome = ?";
        Usuario usuario = null;

//        try (Connection conn = Conexao.getConexaoDB();
//             PreparedStatement stmt = conn.prepareStatement(query)){
//
//            stmt.setString(1, nome);
//
//            try (ResultSet result = stmt.executeQuery()) {
//                if (result.next()) {
//                    usuario = new Usuario();
//                    usuario.setId(result.getInt("id"));
//                    usuario.setNomeUsuario(result.getString("nome"));
//                    usuario.setSenhaHash(result.getString("senha"));
//                }
//            }
//
//        } catch (SQLException e) {
//            System.err.println("Erro ao buscar usuário: " + e.getMessage());
//            e.printStackTrace();
//        }

        return usuario;
    }

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, senha) VALUES (?, ?)";

//        try (Connection conn = Conexao.getConexaoDB();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, usuario.getNomeUsuario());
//            stmt.setString(2, usuario.getSenhaHash());
//
//            stmt.executeUpdate();
//
//            System.out.println("Usuario cadastrado com sucesso");
//        } catch (SQLException e) {
//            System.err.println("Erro ao salvar usuário: " + e.getMessage());
//            e.printStackTrace();
//        }
    }
}
