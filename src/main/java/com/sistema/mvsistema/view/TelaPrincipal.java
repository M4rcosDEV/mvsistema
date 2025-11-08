package com.sistema.mvsistema.view;

import atlantafx.base.theme.Styles;
import com.sistema.mvsistema.service.SessaoUsuario;
import com.sistema.mvsistema.util.ScreenUtils;
import com.sistema.mvsistema.util.Versao;
import com.sistema.mvsistema.view.cadastros.ClienteView;
import com.sistema.mvsistema.view.cadastros.ProdutoView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class TelaPrincipal {
    @Autowired
    private ClienteView clienteView;

    @Autowired
    private ProdutoView produtoView;

    private Pane telaCadastroCliente;
    private Pane telaCadastroProduto;


    private BorderPane layout;

    public Scene createScene(Runnable onSair) {
        layout = new BorderPane();

        HBox cabecalho = configurarCabecalho(onSair);
        MenuBar menu = configurarMenu();

        //TOPO

        // Agrupar cabecalho e menu no topo
        VBox topoContainer = new VBox(cabecalho, menu);
        layout.setTop(topoContainer);

        // Conteudo central
        StackPane conteudoCentral = configurarConteudoPrincipal();
        layout.setCenter(conteudoCentral);

        //Rodapé
        HBox rodape = configurarRodape();
        layout.setBottom(rodape);

        StackPane sceneRoot = new StackPane();
        sceneRoot.getChildren().add(layout);

        Scene scene = new Scene(sceneRoot, 900, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/static/css/tela-principal-styles.css")).toExternalForm()
        );
        return scene;
    }

    private HBox configurarCabecalho(Runnable onSair) {
        // App Name
        Font appNomeFonte = Font.loadFont(
                getClass().getResourceAsStream("/static/fontes/Archive-Regular.otf"), 40
        );

        Label appName = new Label("MV Sistema");
        appName.setFont(appNomeFonte);
        appName.getStyleClass().add("app-name");

        // Usuário e relógio
        Label usuario = new Label(SessaoUsuario.getUsuarioLogado().getNome().toUpperCase());
        usuario.getStyleClass().add("label-user-autenticado");

        Label relogio = new Label();
        relogio.getStyleClass().add("hora-tela-inicial");

        // Configurar relógio
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e ->
                relogio.setText(LocalDateTime.now().format(formato))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        relogio.setText(LocalDateTime.now().format(formato));
        timeline.play();

        // Botão Sair
        Button btnSair = new Button(null, new FontIcon(Feather.LOG_OUT));
        btnSair.getStyleClass().addAll("button-circle");
        btnSair.setOnAction(e -> {
            SessaoUsuario.logout();
            onSair.run();
        });

        // Layout do header
        HBox infoDireita = new HBox(15, usuario, new Separator(Orientation.VERTICAL), relogio, new Separator(Orientation.VERTICAL), btnSair);
        infoDireita.setAlignment(Pos.CENTER_RIGHT);

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);

        HBox header = new HBox(appName, espaco, infoDireita);
        header.getStyleClass().add("header-tela-principal");
        header.setAlignment(Pos.CENTER_LEFT);

        return header;
    }

    private MenuBar configurarMenu(){
        //CADASTROS
        Menu menuCadastro = new Menu("Cadastros");
        MenuItem cadCliente = new MenuItem("Clientes");
        MenuItem cadProduto = new MenuItem("Produtos");
        menuCadastro.getItems().addAll(cadCliente, cadProduto);

        //VENDAS
        Menu menuVendas = new Menu("Vendas");
        MenuItem pdv = new MenuItem("PDV");
        MenuItem historicoDeVendas = new MenuItem("Historico de vendas");
        menuVendas.getItems().addAll(pdv, historicoDeVendas);

        //FINANCEIRO
        Menu menuFinanceiro = new Menu("Financeiro");
        MenuItem contasReceber = new MenuItem("Contas a receber");
        MenuItem contasPagar = new MenuItem("Contas a pagar");
        menuFinanceiro.getItems().addAll(contasReceber, contasPagar);

        //AJUDA
        Menu menuAjuda = new Menu("Ajuda");
        MenuItem sobre = new MenuItem("Sobre");
        menuAjuda.getItems().add(sobre);

        MenuBar menuBar = new MenuBar(menuCadastro, menuVendas, menuFinanceiro, menuAjuda);
        menuBar.getStyleClass().add("menu-bar");

        //EVENTOS DO MENU

        //CADASTRO DE CLIENTE - ABRIR

        cadCliente.setOnAction(e -> {
            telaCadastroCliente = ScreenUtils.setTelaCentral(layout, telaCadastroCliente,
                () -> clienteView.criarCadastroClientePane()
            );
        });

        //CADASTRO DE CLIENTE - FECHAR
        clienteView.setOnCloseRequest(() -> {
            telaCadastroCliente = null;
            layout.setCenter(configurarConteudoPrincipal());
        });

        //CADASTRO DE PRODUTOS - ABRIR

        cadProduto.setOnAction(e -> {
            telaCadastroProduto = ScreenUtils.setTelaCentral(layout, telaCadastroProduto,
                    () -> produtoView.criarCadastroProdutoPane()
            );
        });

        return menuBar;
    }

    private StackPane configurarConteudoPrincipal(){

        ImageView tituloMVSistema = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/imagens/tituloLogin.png"))));
        tituloMVSistema.setPreserveRatio(false);


        Font fonteNomeUsuarioLabelPrincipal = Font.loadFont(getClass().getResourceAsStream("/static/fontes/Archive-Regular.otf"), 40);

        Label nomeUsuarioLabelPrincipal = new Label("Bem-vindo " + SessaoUsuario.getUsuarioLogado().getNome() + "!");
        nomeUsuarioLabelPrincipal.setFont(fonteNomeUsuarioLabelPrincipal);
        nomeUsuarioLabelPrincipal.getStyleClass().add("nome-usuario-center-tela-principal");
        VBox.setMargin(nomeUsuarioLabelPrincipal, new Insets(-20, 0, 0, 30));

        VBox vBoxTelaInicial = new VBox(tituloMVSistema, nomeUsuarioLabelPrincipal);
        vBoxTelaInicial.setAlignment(Pos.CENTER);


        StackPane painelCentral= new StackPane();
        painelCentral.getChildren().add(vBoxTelaInicial);
        StackPane.setAlignment(vBoxTelaInicial, Pos.CENTER);

        return painelCentral;
    }

    private HBox configurarRodape(){
        Label LabelFooterVersao = new Label("VERSÃO DO SISTEMA "+Versao.getVersaoApp());
        LabelFooterVersao.getStyleClass().add("versao-app");

        Region spacerFooter = new Region();
        HBox.setHgrow(spacerFooter, Priority.ALWAYS);

        HBox rodape = new HBox(LabelFooterVersao, spacerFooter);
        rodape.getStyleClass().add("footer-tela-principal");

        return rodape;
    }

}