package com.sistema.mvsistema.model.enums;

public enum EstadoFormulario {
    VISUALIZACAO,
    EDICAO,
    NOVO;

    public boolean isModeEdicao(){
        return this == EDICAO;
    }
}
