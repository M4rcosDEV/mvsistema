package com.sistema.mvsistema.entity.enums;

public enum EstadoFormulario {
    NOVO,
    EDITANDO_NOVO,
    EDITANDO_EXISTENTE;

    public boolean isModeEdicaoNovo(){
        return this == EDITANDO_NOVO;
    }

    public boolean isModeEdicaoExistente(){
        return this == EDITANDO_EXISTENTE;
    }
}
