package com.sistema.mvsistema.view;

import atlantafx.base.theme.Styles;
import com.sistema.mvsistema.service.SessaoUsuario;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        BorderPane layout = new BorderPane();

        Label appName = new Label("MV Sistema");
        appName.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label usuarioAutenticado = new Label(SessaoUsuario.getUsuarioLogado().getNome().toUpperCase());

        usuarioAutenticado.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 0 10 0 0;");

        // --- Label do relógio ---
        Label clockLabel = new Label();
        clockLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: white; -fx-font-size: 14px;");

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

        Region spacer = new Region();

        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnSair = new Button (null, new FontIcon (Feather.LOG_OUT));
        btnSair.getStyleClass().addAll(Styles.BUTTON_CIRCLE);
        btnSair.setFocusTraversable(false);

        HBox infoDireita = new HBox(15, usuarioAutenticado, new Separator(Orientation.VERTICAL) , clockLabel, new Separator(Orientation.VERTICAL), btnSair);
        infoDireita.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(appName, spacer, infoDireita);
        header.setStyle("-fx-background-color: #2460ff;");
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMinHeight(50);

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
            System.out.println("clicou para sair");
            onSair.run();
        });

        MenuBar menuBar = new MenuBar(menuCadastro, menuVendas, menuFinanceiro, menuAjuda);

        menuBar.getStyleClass().add("menu-bar");


        VBox topContainer = new VBox(header, menuBar);

        // ---- Layout ----

        layout.setTop(topContainer);

        // Conteúdo central
        Label lbl = new Label("Bem-vindo à tela principal " + SessaoUsuario.getUsuarioLogado().getNome() + "!");
        lbl.setStyle("-fx-font-size: 18px; -fx-padding: 20;");
        layout.setCenter(lbl);

        cadCliente.setOnAction(e -> {
            if (telaCadastroCliente == null) {
                telaCadastroCliente = createCadastroClientePane(layout); // cria apenas uma vez
            }
            layout.setCenter(telaCadastroCliente); // reutiliza a mesma instância
        });

//        cadProduto.setOnAction(e -> {
//            if (telaCadastroProduto == null) {
//                ProdutoView produtoView = new ProdutoView();
//                telaCadastroProduto = produtoView.createAbaProduto(); // cria apenas uma vez
//            }
//            layout.setCenter(telaCadastroProduto);
//        });

        Scene scene = new Scene(layout, 900, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/static/css/tela-principal-styles.css")).toExternalForm()
        );
        return scene;

    }

    private Pane createCadastroClientePane(BorderPane layout) {

        Button btnNovoCliente = new Button(
                "Novo cliente", new FontIcon (Feather.PLUS)
        );
        btnNovoCliente.getStyleClass().add(Styles.ACCENT);
        btnNovoCliente.setMnemonicParsing(false);
        btnNovoCliente.setFocusTraversable(false);

        Button btnBuscar = new Button(
                "Buscar", new FontIcon (Feather.SEARCH)
        );

        btnBuscar.getStyleClass().add(Styles.ACCENT);
        btnBuscar.setMnemonicParsing(false);
        btnBuscar.setFocusTraversable(false);
        btnBuscar.setOnAction(e -> clienteView.abrirJanelaBuscarCliente());

        Button btnFechar = new Button(
                "Fechar", new FontIcon (Feather.X)
        );
        btnFechar.getStyleClass().add(Styles.DANGER);
        btnFechar.setMnemonicParsing(false);
        btnFechar.setFocusTraversable(false);

        Label lbl = new Label("Bem-vindo à tela principal " + SessaoUsuario.getUsuarioLogado().getNome() + "!");
        lbl.setStyle("-fx-font-size: 18px; -fx-padding: 20;");
        btnFechar.setOnAction(e -> {
            layout.setCenter(lbl);
        });

        HBox botoesAcaoNovoClienteAndBuscaCliente = new HBox();
        botoesAcaoNovoClienteAndBuscaCliente.getChildren().addAll(btnNovoCliente, btnBuscar, btnFechar);
        botoesAcaoNovoClienteAndBuscaCliente.setAlignment(Pos.CENTER_LEFT);
        botoesAcaoNovoClienteAndBuscaCliente.setSpacing(15);

        Label labelCliente = new Label("Cliente");
        TextField campoCodCliente = new TextField();
        campoCodCliente.setText("1");
        campoCodCliente.setEditable(false);
        campoCodCliente.setPrefWidth(100);

        VBox campoLayoutCodCliente = new VBox(5, labelCliente, campoCodCliente);
        campoLayoutCodCliente.setFillWidth(false);
        campoCodCliente.setMaxWidth(100);
        campoLayoutCodCliente.setAlignment(Pos.CENTER_LEFT);

        TabPane defaultTabs = new TabPane (
                new Tab ("Informações Gerais", clienteView.createAbaGeral()),
                new Tab ("Endereço", clienteView.createAbaEndereco()),
                new Tab ("Financeiro") // Nomes mais descritivos
        );

        defaultTabs.setTabClosingPolicy (TabPane.TabClosingPolicy.UNAVAILABLE);

        defaultTabs.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox containerInformacoes = new VBox(20, campoLayoutCodCliente, defaultTabs);
        containerInformacoes.setPadding(new Insets(20));
        containerInformacoes.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox blocoBrancoEstilizado = new VBox();
        blocoBrancoEstilizado.getStyleClass().add("bloco-branco");


        blocoBrancoEstilizado.getChildren().add(containerInformacoes);

        VBox.setVgrow(containerInformacoes, Priority.ALWAYS);
        containerInformacoes.setMaxWidth(Double.MAX_VALUE);

        // --- 6. LAYOUT PRINCIPAL ---
        VBox layoutPrincipal = new VBox(15, botoesAcaoNovoClienteAndBuscaCliente, blocoBrancoEstilizado);

        layoutPrincipal.getStyleClass().add("layout-principal-cad-cliente");

        layoutPrincipal.setPadding(new Insets(20));

        //Garante que o bloco branco ocupe o restante da altura do layout principal.
        VBox.setVgrow(blocoBrancoEstilizado, Priority.ALWAYS);
        return layoutPrincipal;
    }



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