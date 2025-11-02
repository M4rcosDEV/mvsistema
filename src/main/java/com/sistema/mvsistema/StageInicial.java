package com.sistema.mvsistema;

import atlantafx.base.theme.PrimerLight;
import com.sistema.mvsistema.model.Cliente;
import com.sistema.mvsistema.model.Endereco;
import com.sistema.mvsistema.model.Municipio;
import com.sistema.mvsistema.repository.ClienteRepository;
import com.sistema.mvsistema.service.ClienteService;
import com.sistema.mvsistema.util.DocumentoUtil;
import com.sistema.mvsistema.util.EstilosGlobal;
import com.sistema.mvsistema.view.ClienteView;
import com.sistema.mvsistema.view.TelaLogin;
import com.sistema.mvsistema.view.TelaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import com.sistema.mvsistema.MainApplication.StageReadyEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StageInicial implements ApplicationListener<StageReadyEvent> {
    @Autowired
    ClienteService clienteService;

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

        Cliente cliente = new Cliente();
        cliente.setNome("Marcos");
        cliente.setSobrenome("Pereira");
        cliente.setTipoPessoa("F");
        cliente.setCpfCnpj("123.456.789-00");
        cliente.setEmail("marcos@email.com");
        cliente.setTelefone("(11) 99999-0000");
        cliente.setAtivo(true);

        Endereco endereco1 = new Endereco();
        endereco1.setNome("Residencial");
        endereco1.setTipoEndereco("Residencial");
        endereco1.setCep("01000-000");
        endereco1.setRua("Rua das Flores");
        endereco1.setNumero("123");
        endereco1.setBairro("Centro");
        endereco1.setEstado("SP");
        endereco1.setPais("Brasil");
        Municipio municipio = new Municipio();
        municipio.setId(1);
        endereco1.setMunicipio(municipio);

        Endereco endereco2 = new Endereco();
        endereco2.setNome("Comercial");
        endereco2.setTipoEndereco("Comercial");
        endereco2.setCep("02000-000");
        endereco2.setRua("Av. Paulista");
        endereco2.setNumero("1500");
        endereco2.setBairro("Bela Vista");
        endereco2.setEstado("SP");
        endereco2.setPais("Brasil");
        Municipio municipio2 = new Municipio();
        municipio2.setId(1);
        endereco2.setMunicipio(municipio2);

        cliente.addEndereco(endereco1);
        cliente.addEndereco(endereco2);

        clienteService.salvar(cliente);

        VBox root = new VBox();
        Stage stage = event.getStage();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

        this.primaryStage = event.getStage();
        this.primaryStage.getIcons().add(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/static/imagens/logo.png")
        )));

        this.loginScene = telaLogin.createScene(this::mostrarTelaPrincipal);

        mostrarTelaLogin();
    }

    private void mostrarTelaLogin() {
        primaryStage.setTitle("Tela login");
        primaryStage.setMaximized(false);
        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void mostrarTelaPrincipal() {
        Scene mainScene = telaPrincipal.createScene(this::mostrarTelaLogin);

        EstilosGlobal.applyGlobalCss(mainScene);
        primaryStage.setTitle("Tela principal");
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
