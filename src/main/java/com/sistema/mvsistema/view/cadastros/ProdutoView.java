package com.sistema.mvsistema.view.cadastros;

import atlantafx.base.theme.Styles;
import com.sistema.mvsistema.service.ProdutoService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProdutoView {

    private Button btnNovoProduto = new Button();
    private Button btnBuscarProduto = new Button();
    private Button btnFecharTelaProd = new Button();

    private Runnable onCloseCallback = () -> {};

    public void setOnCloseRequest(Runnable callback) {
        this.onCloseCallback = (callback != null) ? callback : () -> {};
    }

    public Pane criarCadastroProdutoPane() {

        TabPane opcoesTab = new TabPane (
                new Tab ("Informações Gerais", createAbaProduto()),
                new Tab ("Detalhes"),
                new Tab ("Fornecedores"),
                new Tab ("Historico do produto")
        );

        opcoesTab.setTabClosingPolicy (TabPane.TabClosingPolicy.UNAVAILABLE);

        opcoesTab.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox containerInformacoes = new VBox(20, opcoesTab);
        containerInformacoes.setPadding(new Insets(20));
        containerInformacoes.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox blocoBrancoEstilizado = new VBox();
        blocoBrancoEstilizado.getStyleClass().add("bloco-branco");

        blocoBrancoEstilizado.getChildren().add(containerInformacoes);

        VBox.setVgrow(containerInformacoes, Priority.ALWAYS);
        containerInformacoes.setMaxWidth(Double.MAX_VALUE);

        // --- 6. LAYOUT PRINCIPAL ---
        VBox layoutPrincipal = new VBox(15, botoesAcao(), blocoBrancoEstilizado);

        layoutPrincipal.getStyleClass().add("layout-principal-cad-cliente");

        layoutPrincipal.setPadding(new Insets(20));

        //Garante que o bloco branco ocupe o restante da altura do layout principal.
        VBox.setVgrow(blocoBrancoEstilizado, Priority.ALWAYS);


        StackPane rootPane = new StackPane(layoutPrincipal);
        rootPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);


        // --- 6. LAYOUT PRINCIPAL ---


        layoutPrincipal.setPadding(new Insets(20));

        return rootPane;
    }

    public Pane createAbaDetalhes(){
        Label labelCodigoInterno = new Label("Código Interno");
        TextField campoCodigoInterno = new TextField();
        campoCodigoInterno.setPromptText("Ex: 001.002.003");

        // Detalhes e Códigos
        Label labelEAN = new Label("Código de Barras (EAN)");
        TextField campoEAN = new TextField();
        campoEAN.setPromptText("Ex: 7890000000000");

        //Marca
        Label labelMarca = new Label("Marca / Fabricante");
        ComboBox<String> campoMarca = new ComboBox<>();
        campoMarca.setPromptText("Selecione a marca");

        Label labelLocalizacao = new Label("Localização no Estoque");
        TextField campoLocalizacao = new TextField();
        campoLocalizacao.setPromptText("Ex: Corredor A, Prateleira 3");

        Label labelUnidadeMedida = new Label("Unidade de Medida (UN)");
        ComboBox<String> campoUnidadeMedida = new ComboBox<>();
        campoUnidadeMedida.getItems().addAll("UN - Unidade", "KG - Kilograma", "PC - Peça", "CX - Caixa", "M - Metro", "L - Litro");
        campoUnidadeMedida.setPromptText("UN de controle");

        Label labelUnidadeVenda = new Label("Unidade de Venda");
        ComboBox<String> campoUnidadeVenda = new ComboBox<>();
        campoUnidadeVenda.getItems().addAll("UN - Unidade", "KG - Kilograma", "CX - Caixa", "Kit");
        campoUnidadeVenda.setPromptText("Como é vendido");

        Label labelUnidadeCompra = new Label("Unidade de Compra");
        ComboBox<String> campoUnidadeCompra = new ComboBox<>();
        campoUnidadeCompra.getItems().addAll("UN - Unidade", "KG - Kilograma", "CX - Caixa", "Fardo");
        campoUnidadeCompra.setPromptText("Como é comprado");


        // Valores e Estoque
        Label labelPrecoCusto = new Label("Preço de Custo (R$)");
        TextField campoPrecoCusto = new TextField();
        campoPrecoCusto.setPromptText("0,00");

        Label labelMarkup = new Label("Markup (%)");
        TextField campoMarkup = new TextField();
        campoMarkup.setPromptText("0,00");

        Label labelPrecoVenda = new Label("Preço de Venda (R$)");
        TextField campoPrecoVenda = new TextField();
        campoPrecoVenda.setPromptText("0,00");

        Label labelEstoqueAtual = new Label("Estoque Atual");
        TextField campoEstoqueAtual = new TextField();
        campoEstoqueAtual.setPromptText("0");

        Label labelEstoqueMinimo = new Label("Estoque Mínimo");
        TextField campoEstoqueMinimo = new TextField();
        campoEstoqueMinimo.setPromptText("0");

        //  BOTÕES DE ADIÇÃO RÁPIDA
        Button btnGerarCodInterno = new Button(null, new FontIcon(Feather.SETTINGS));
        Tooltip tooltipGerarCodInt = new Tooltip("Gerar código interno");
        btnGerarCodInterno.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);
        btnGerarCodInterno.setTooltip(tooltipGerarCodInt);

        Button btnNovaMarca = new Button(null, new FontIcon(Feather.PLUS));
        btnNovaMarca.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);

        Button btnNovaUnidadeMedida = new Button(null, new FontIcon(Feather.PLUS));
        btnNovaUnidadeMedida.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);

        btnGerarCodInterno.setOnAction(e -> {
            String codigoGerado = ProdutoService.gerarCodigoInterno();
            campoCodigoInterno.setText(codigoGerado);
        });


        // LAYOUTS COM BOTÕES
        HBox hBoxGerarCodInterno = new HBox(5, campoCodigoInterno, btnGerarCodInterno);
        HBox.setHgrow(campoCodigoInterno, Priority.ALWAYS);
        hBoxGerarCodInterno.setAlignment(Pos.CENTER_LEFT);


        HBox hBoxUnidadeMedida = new HBox(5, campoUnidadeMedida, btnNovaUnidadeMedida);
        HBox.setHgrow(campoUnidadeMedida, Priority.ALWAYS);
        hBoxUnidadeMedida.setAlignment(Pos.CENTER_LEFT);


        HBox hBoxMarca = new HBox(5, campoMarca, btnNovaMarca);
        HBox.setHgrow(campoMarca, Priority.ALWAYS);
        hBoxMarca.setAlignment(Pos.CENTER_LEFT);

        VBox linhaCodigoInterno = new VBox(5, labelCodigoInterno, hBoxGerarCodInterno);

        return null;
    }

    public Pane createAbaProduto() {
        // 1. CAMPOS MANTIDOS (Identificação e Classificação)
        Label labelDescricao = new Label("Descrição*");
        TextField campoDescricao = new TextField();
        campoDescricao.setPromptText("Nome do produto ou serviço");
        Platform.runLater(campoDescricao::requestFocus);
        campoDescricao.getStyleClass().add("text-field-campo");

        Label labelTipoProduto = new Label("Tipo de Item");
        ComboBox<String> campoTipoProduto = new ComboBox<>();
        campoTipoProduto.getItems().addAll("Produto", "Serviço", "Kit");
        campoTipoProduto.setPromptText("Selecione");

        Label labelCategoria = new Label("Categoria");
        ComboBox<String> campoCategoria = new ComboBox<>();
        campoCategoria.getItems().addAll("Geral", "Matéria-Prima", "Revenda", "Consumo Interno");
        campoCategoria.setPromptText("Selecione");

        Label labelGrupo = new Label("Grupo");
        ComboBox<String> campoGrupo = new ComboBox<>();
        campoGrupo.setPromptText("Selecione o grupo");

        Label labelSubgrupo = new Label("Subgrupo");
        ComboBox<String> campoSubgrupo = new ComboBox<>();
        campoSubgrupo.setPromptText("Selecione o subgrupo");

        Label labelObservacoes = new Label("Detalhes / Observações Técnicas");
        TextArea campoObservacoes = new TextArea();
        campoObservacoes.setPromptText("Informações técnicas, composição, notas, etc.");
        campoObservacoes.setPrefRowCount(10);

        // 2. BOTÕES E HBOXES (Mantidos)
        Button btnNovoGrupo = new Button(null, new FontIcon(Feather.PLUS));
        btnNovoGrupo.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.ACCENT);

        Button btnNovoSubgrupo = new Button(null, new FontIcon(Feather.PLUS));
        btnNovoSubgrupo.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.ACCENT);

        HBox hBoxGrupo = new HBox(5, campoGrupo, btnNovoGrupo);
        HBox.setHgrow(campoGrupo, Priority.ALWAYS);
        hBoxGrupo.setAlignment(Pos.CENTER_LEFT);

        HBox hBoxSubgrupo = new HBox(5, campoSubgrupo, btnNovoSubgrupo);
        HBox.setHgrow(campoSubgrupo, Priority.ALWAYS);
        hBoxSubgrupo.setAlignment(Pos.CENTER_LEFT);

        // 3. LAYOUT CONTAINERINFO (Ajustado)
        // Apenas a descrição, ocupando 100%
        VBox linhaDescricao = new VBox(5, labelDescricao, campoDescricao);

        //VBox linhaTipoProduto = new VBox(5, labelTipoProduto, campoTipoProduto);

        GridPane gridLine1 = new GridPane();
        gridLine1.setHgap(50);
        gridLine1.setVgap(10);
        gridLine1.setPadding(new Insets(10, 0, 0, 0));

        ColumnConstraints col1 = new ColumnConstraints();

        col1.setPercentWidth(40);
        gridLine1.getColumnConstraints().addAll(col1);
        gridLine1.add(linhaDescricao, 0, 0);
        //gridLine1.add(linhaTipoProduto, 1, 0);

        // 4. LAYOUT LINHA DETALHES
        GridPane gridLine2 = new GridPane();
        gridLine2.setHgap(20);
        gridLine2.setVgap(10);
        gridLine2.setPadding(new Insets(10, 0, 0, 0));

        //ColumnConstraints cc1 = new ColumnConstraints();
        //cc1.setHgrow(Priority.ALWAYS);
        //cc1.setPercentWidth(25); // 4 colunas de 25%
        //gridLine2.getColumnConstraints().addAll(cc1, cc1, cc1, cc1);

        // Linha 0: Rótulos (Campos restantes)
        gridLine2.add(labelTipoProduto, 0, 0);
        gridLine2.add(campoTipoProduto, 0, 1);

        gridLine2.add(labelGrupo, 1, 0);
        gridLine2.add(hBoxGrupo, 1, 1);

        gridLine2.add(labelSubgrupo, 1, 2);
        gridLine2.add(hBoxSubgrupo, 1, 3);

        gridLine2.add(labelCategoria, 0, 2);
        gridLine2.add(campoCategoria, 0, 3);

        // Preenchimento automático de largura
        GridPane.setFillWidth(campoTipoProduto, true);
        GridPane.setFillWidth(campoCategoria, true);
        GridPane.setFillWidth(hBoxGrupo, true);
        GridPane.setFillWidth(hBoxSubgrupo, true);

        campoTipoProduto.setOnAction(e -> {
            String selecionado = campoTipoProduto.getValue();
            boolean isServico = Objects.equals(selecionado, "Serviço");

            // Esta lógica agora está "morta" (não faz nada),
            // mas está pronta para o futuro se você quiser
            // desabilitar, por exemplo, "Grupos" caso seja um "Serviço".
            if (isServico) {
                // Ex: campoGrupo.setDisable(true);
            } else {
                // Ex: campoGrupo.setDisable(false);
            }
        });

        // 6. OBSERVAÇÕES E LAYOUT FINAL (Mantido)
        VBox linhaObservacoes = new VBox(5,labelObservacoes, campoObservacoes);

        // LAYOUT PRINCIPAL DO CONTEÚDO
        VBox layoutGeral = new VBox(20,gridLine1,  gridLine2, linhaObservacoes);
        layoutGeral.setPadding(new Insets(20));
        layoutGeral.setFillWidth(true);
        VBox.setVgrow(linhaObservacoes, Priority.ALWAYS); // Observações ocupam o resto do espaço

        VBox.setVgrow(layoutGeral, Priority.ALWAYS);

        return layoutGeral;
    }

    public HBox botoesAcao(){
        Button btnNovoProduto = new Button(
                "Novo produto", new FontIcon (Feather.PLUS)
        );
        btnNovoProduto.getStyleClass().add(Styles.ACCENT);
        btnNovoProduto.setMnemonicParsing(false);
        btnNovoProduto.setFocusTraversable(false);

        btnNovoProduto.getStyleClass().add(Styles.ACCENT);

        btnBuscarProduto = new Button(
                "Buscar", new FontIcon (Feather.SEARCH)
        );
        btnBuscarProduto.getStyleClass().add(Styles.ACCENT);
        btnBuscarProduto.setMnemonicParsing(false);
        btnBuscarProduto.setFocusTraversable(false);

        btnFecharTelaProd = new Button(
                "Fechar", new FontIcon (Feather.X)
        );
        btnFecharTelaProd.getStyleClass().add(Styles.DANGER);
        btnFecharTelaProd.setMnemonicParsing(false);
        btnFecharTelaProd.setFocusTraversable(false);

        HBox hBoxbotoesContainer = new HBox(btnNovoProduto, btnBuscarProduto, btnFecharTelaProd);
        hBoxbotoesContainer.setAlignment(Pos.CENTER_LEFT);
        hBoxbotoesContainer.setSpacing(15);

        return hBoxbotoesContainer;
    }
}
