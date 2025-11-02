package com.sistema.mvsistema.util;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.function.Supplier;

public class ScreenUtils {
    /**
     * Define o conteúdo central do layout, criando-o apenas uma vez.
     *
     * @param layout       O BorderPane principal da aplicação
     * @param telaCacheada O pane atual (pode ser null)
     * @param criador      Função que cria a tela (ex: this::createCadastroProdutoPane)
     * @return O pane criado (ou o já existente)
     */
    public static Pane setTelaCentral(BorderPane layout, Pane telaCacheada, Supplier<Pane> criador) {
        if (telaCacheada == null) {
            telaCacheada = criador.get();
        }
        layout.setCenter(telaCacheada);
        return telaCacheada;
    }
}
