package com.sistema.mvsistema.view;

import atlantafx.base.theme.Styles;
import com.sistema.mvsistema.service.SessaoUsuario;
import com.sistema.mvsistema.util.Versao;
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

    private Pane telaCadastroCliente;

    public Scene createScene(Runnable onSair) {

        //Fonte appNome
        Font appNomeFonte = Font.loadFont(
                getClass().getResourceAsStream("/static/fontes/Archive-Regular.otf"),
                40 // tamanho da fonte
        );

        BorderPane layout = new BorderPane();

        Label appName = new Label("MV Sistema");
        appName.setFont(appNomeFonte);
        appName.getStyleClass().add("app-name");

        Label usuarioAutenticado = new Label(SessaoUsuario.getUsuarioLogado().getNome().toUpperCase());
        usuarioAutenticado.getStyleClass().add("label-user-autenticado");

        // --- Label do relógio ---
        Label clockLabel = new Label();
        clockLabel.setStyle("");
        clockLabel.getStyleClass().add("hora-tela-inicial");
        // Formato da data/hora
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Timeline que atualiza o relógio a cada segundo
        Timeline relogio = new Timeline(
                new KeyFrame(Duration.seconds(1), e ->
                        clockLabel.setText(LocalDateTime.now().format(formato))
                )
        );

        relogio.setCycleCount(Timeline.INDEFINITE);
        clockLabel.setText(LocalDateTime.now().format(formato));
        relogio.play();

        Region spacerHeader = new Region();

        HBox.setHgrow(spacerHeader, Priority.ALWAYS);

        Button btnSair = new Button (null, new FontIcon (Feather.LOG_OUT));
        btnSair.getStyleClass().addAll(Styles.BUTTON_CIRCLE);
        btnSair.setFocusTraversable(false);

        HBox infoDireita = new HBox(15, usuarioAutenticado, new Separator(Orientation.VERTICAL) , clockLabel, new Separator(Orientation.VERTICAL), btnSair);
        infoDireita.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(appName, spacerHeader, infoDireita);
        header.getStyleClass().add("header-tela-principal");
        header.setAlignment(Pos.CENTER_LEFT);

        //MENU
        Menu menuCadastro = new Menu("Cadastros");

        MenuItem cadCliente = new MenuItem("Clientes");
        MenuItem cadProduto = new MenuItem("Produtos");

        menuCadastro.getItems().addAll(cadCliente, cadProduto);

        Menu menuVendas = new Menu("Vendas");

        MenuItem pdv = new MenuItem("PDV");
        MenuItem historicoDeVendas = new MenuItem("Historico de vendas");

        menuVendas.getItems().addAll(pdv, historicoDeVendas);

        Menu menuFinanceiro = new Menu("Financeiro");
        MenuItem contasReceber = new MenuItem("Contas a receber");
        MenuItem contasPagar = new MenuItem("Contas a pagar");
        menuFinanceiro.getItems().addAll(contasReceber, contasPagar);


        Menu menuAjuda = new Menu("Ajuda");
        MenuItem sobre = new MenuItem("Sobre");
        menuAjuda.getItems().add(sobre);

        btnSair.setOnAction(e -> {
            SessaoUsuario.logout();
            onSair.run();
        });

        MenuBar menuBar = new MenuBar(menuCadastro, menuVendas, menuFinanceiro, menuAjuda);

        menuBar.getStyleClass().add("menu-bar");

        // ---- Layout ----

        //Topo
        VBox topContainer = new VBox(header, menuBar);
        layout.setTop(topContainer);

        // Conteúdo central

        // Define o StackPane que agora contém o VBox centralizado como o centro do BorderPane
        layout.setCenter(conteudoPrincipal());

        //Rodapé

        Label LabelFooterVersao = new Label("VERSÃO DO SISTEMA "+Versao.getVersaoApp());
        LabelFooterVersao.getStyleClass().add("versao-app");

        Region spacerFooter = new Region();
        HBox.setHgrow(spacerFooter, Priority.ALWAYS);

        HBox footer = new HBox(LabelFooterVersao, spacerFooter);
        footer.getStyleClass().add("footer-tela-principal");
        layout.setBottom(footer);


        //Eventos
        cadCliente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (telaCadastroCliente == null) {
                    telaCadastroCliente = clienteView.createCadastroClientePane(layout); // cria apenas uma vez
                }
                layout.setCenter(telaCadastroCliente); // reutiliza a mesma instância
            }
        });

/*
        cadProduto.setOnAction(e -> {
            if (telaCadastroProduto == null) {
                ProdutoView produtoView = new ProdutoView();
                telaCadastroProduto = produtoView.createAbaProduto();
            }
            layout.setCenter(telaCadastroProduto);
        });
*/


        clienteView.setOnCloseRequest(() -> {
            clienteView.limparEBloquearFormulario();
            clienteView.limparFormEndereco();
            layout.setCenter(conteudoPrincipal());
        });

        StackPane sceneRoot = new StackPane();
        sceneRoot.getChildren().add(layout);

        //Criação do scene
        Scene scene = new Scene(sceneRoot, 900, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/static/css/tela-principal-styles.css")).toExternalForm()
        );
        return scene;

    }

    private Pane conteudoPrincipal(){

        ImageView tituloMVSistema = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/imagens/tituloLogin.png"))));
        tituloMVSistema.setPreserveRatio(false); // Estica a imagem para preencher


        Font fonteNomeUsuarioLabelPrincipal = Font.loadFont(
                getClass().getResourceAsStream("/static/fontes/Archive-Regular.otf"),
                40 // tamanho da fonte
        );

        Label nomeUsuarioLabelPrincipal = new Label("Bem-vindo " + SessaoUsuario.getUsuarioLogado().getNome() + "!");
        nomeUsuarioLabelPrincipal.setFont(fonteNomeUsuarioLabelPrincipal);
        nomeUsuarioLabelPrincipal.getStyleClass().add("nome-usuario-center-tela-principal");
        VBox.setMargin(nomeUsuarioLabelPrincipal, new Insets(-20, 0, 0, 30));

        VBox vBoxTelaInicial = new VBox(tituloMVSistema, nomeUsuarioLabelPrincipal);

        // Centraliza o conteúdo (imagem e label) DENTRO do VBox
        vBoxTelaInicial.setAlignment(Pos.CENTER);

        // Criei um StackPane para servir como container central
        StackPane painelCentralWrapper = new StackPane();
        painelCentralWrapper.getChildren().add(vBoxTelaInicial);

        // Define o alinhamento do VBox DENTRO do StackPane embora CENTER seja o padrão
        StackPane.setAlignment(vBoxTelaInicial, Pos.CENTER);

        return painelCentralWrapper;
    }

    @SuppressWarnings("unused" )
    private Pane createCadastroProdutoPane() {
        Button btnNovoCliente = new Button(
                "Novo cliente", new FontIcon (Feather.PLUS)
        );
        btnNovoCliente.getStyleClass().add(Styles.ACCENT);
        btnNovoCliente.setMnemonicParsing(false);
        btnNovoCliente.setFocusTraversable(false);

        // --- 6. LAYOUT PRINCIPAL ---
        VBox layoutPrincipal = new VBox(15, btnNovoCliente);

        layoutPrincipal.getStyleClass().add("layout-principal-cad-cliente");

        layoutPrincipal.setPadding(new Insets(20));

        return layoutPrincipal;
    }
}