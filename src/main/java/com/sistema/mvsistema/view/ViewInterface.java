package com.sistema.mvsistema.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public interface ViewInterface {
    Pane createView(BorderPane borderPane);
    void setOnCloseRequest(Runnable callback);
    void limparFormulario();
}
