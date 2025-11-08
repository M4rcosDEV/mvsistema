package com.sistema.mvsistema;

import atlantafx.base.theme.PrimerLight;
import com.sistema.mvsistema.util.EstilosGlobal;
import com.sistema.mvsistema.view.cadastros.ClienteView;
import com.sistema.mvsistema.view.TelaLogin;
import com.sistema.mvsistema.view.TelaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import com.sistema.mvsistema.MainApplication.StageReadyEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StageInicial implements ApplicationListener<StageReadyEvent> {
    private final TelaLogin telaLogin;
    private final TelaPrincipal telaPrincipal;
    private final EstilosGlobal estilosGlobal;
    private final ClienteView clienteView;

    private Stage primaryStage;
    private Scene loginScene;

    public StageInicial(TelaLogin telaLogin, TelaPrincipal telaPrincipal, EstilosGlobal estilosGlobal, ClienteView clienteView) {
        this.telaLogin = telaLogin;
        this.telaPrincipal = telaPrincipal;
        this.estilosGlobal = estilosGlobal;
        this.clienteView = clienteView;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {

        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        VBox root = new VBox();
        Stage stage = event.getStage();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

        this.primaryStage = event.getStage();
        this.primaryStage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/static/imagens/logo.png")
        )));

        this.loginScene = telaLogin.createScene(this::mostrarTelaPrincipal, this.primaryStage);

        mostrarTelaLogin();
    }

    private void mostrarTelaLogin() {
        primaryStage.setTitle("MVS - Sistema Gerencial");
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void mostrarTelaPrincipal() {
        Scene mainScene = telaPrincipal.createScene(this::mostrarTelaLogin);
        estilosGlobal.applyGlobalCss(mainScene);
        primaryStage.setTitle("MVS - Sistema Gerencial - Tela principal");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);

        primaryStage.show();
    }
}
