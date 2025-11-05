package com.sistema.mvsistema.util;

import javafx.scene.control.ProgressIndicator;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;


public class LoadingIndicadorUtil extends StackPane{
    private final ProgressIndicator indicator;

    public LoadingIndicadorUtil() {
        indicator = new ProgressIndicator();
        indicator.setMinSize(40, 40);
        indicator.setVisible(false);

        setAlignment(Pos.CENTER);
        getChildren().add(indicator);
        setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        setVisible(false);
    }

    public void show() {
        setVisible(true);
        indicator.setVisible(true);
    }

    public void hide() {
        setVisible(false);
        indicator.setVisible(false);
    }

}
