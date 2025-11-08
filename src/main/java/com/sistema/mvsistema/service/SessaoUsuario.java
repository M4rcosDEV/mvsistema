package com.sistema.mvsistema.service;

import com.sistema.mvsistema.entity.Usuario;

public class SessaoUsuario {
    private static Usuario usuarioLogado = null;

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void logout() {
        usuarioLogado = null;
    }
}
