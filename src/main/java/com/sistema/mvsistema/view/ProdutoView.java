package com.sistema.mvsistema.view;

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
    public Pane createAbaProduto() {
    Button btnNovoCliente = new Button(
            "Novo produto", new FontIcon (Feather.PLUS)
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

    Button btnFechar = new Button(
            "Fechar", new FontIcon (Feather.X)
    );
    btnFechar.getStyleClass().add(Styles.DANGER);
    btnFechar.setMnemonicParsing(false);
    btnFechar.setFocusTraversable(false);

    HBox botoesAcaoNovoClienteAndBuscaCliente = new HBox();
    botoesAcaoNovoClienteAndBuscaCliente.getChildren().addAll(btnNovoCliente, btnBuscar, btnFechar);
    botoesAcaoNovoClienteAndBuscaCliente.setAlignment(Pos.CENTER_LEFT);
    botoesAcaoNovoClienteAndBuscaCliente.setSpacing(15);

    // CAMPOS DE TEXTO E SELEÇÃO

    // Informações Principais
    Label labelDescricao = new Label("Descrição*");
    TextField campoDescricao = new TextField();
    campoDescricao.setPromptText("Nome do produto ou serviço");
    Platform.runLater(campoDescricao::requestFocus);
    campoDescricao.getStyleClass().add("text-field-campo");

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

    // Classificação
    Label labelTipoProduto = new Label("Tipo de Item");
    ComboBox<String> campoTipoProduto = new ComboBox<>();
    campoTipoProduto.getItems().addAll("Produto", "Serviço", "Kit");
    campoTipoProduto.setPromptText("Selecione");

    Label labelUnidadeMedida = new Label("Unidade de Medida (UN)");
    ComboBox<String> campoUnidadeMedida = new ComboBox<>();
    campoUnidadeMedida.getItems().addAll("UN - Unidade", "KG - Kilograma", "PC - Peça", "CX - Caixa", "M - Metro", "L - Litro");
    campoUnidadeMedida.setPromptText("UN de controle");

    Label labelCategoria = new Label("Categoria");
    ComboBox<String> campoCategoria = new ComboBox<>();
    campoCategoria.getItems().addAll("Geral", "Matéria-Prima", "Revenda", "Consumo Interno");
    campoCategoria.setPromptText("Selecione");

    //Grupos
    Label labelGrupo = new Label("Grupo");
    ComboBox<String> campoGrupo = new ComboBox<>();
    campoGrupo.setPromptText("Selecione o grupo");

    Label labelSubgrupo = new Label("Subgrupo");
    ComboBox<String> campoSubgrupo = new ComboBox<>();
    campoSubgrupo.setPromptText("Selecione o subgrupo");

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

    // Observações
    Label labelObservacoes = new Label("Detalhes / Observações Técnicas");
    TextArea campoObservacoes = new TextArea();
    campoObservacoes.setPromptText("Informações técnicas, composição, notas, etc.");
    campoObservacoes.setPrefRowCount(3);

    Tooltip tooltipGerarCodInt = new Tooltip("Gerar código interno");

    //  BOTÕES DE ADIÇÃO RÁPIDA
    Button btnGerarCodInterno = new Button(null, new FontIcon(Feather.SETTINGS));
    btnGerarCodInterno.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);
    btnGerarCodInterno.setTooltip(tooltipGerarCodInt);

    Button btnNovaMarca = new Button(null, new FontIcon(Feather.PLUS));
    btnNovaMarca.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);

    Button btnNovoGrupo = new Button(null, new FontIcon(Feather.PLUS));
    btnNovoGrupo.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);

    Button btnNovoSubgrupo = new Button(null, new FontIcon(Feather.PLUS));
    btnNovoSubgrupo.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT);

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

    HBox hBoxMarca = new HBox(5, campoMarca, btnNovaMarca);
    HBox.setHgrow(campoMarca, Priority.ALWAYS);
    hBoxMarca.setAlignment(Pos.CENTER_LEFT);

    HBox hBoxGrupo = new HBox(5, campoGrupo, btnNovoGrupo);
    HBox.setHgrow(campoGrupo, Priority.ALWAYS);
    hBoxGrupo.setAlignment(Pos.CENTER_LEFT);

    HBox hBoxSubgrupo = new HBox(5, campoSubgrupo, btnNovoSubgrupo);
    HBox.setHgrow(campoSubgrupo, Priority.ALWAYS);
    hBoxSubgrupo.setAlignment(Pos.CENTER_LEFT);

    HBox hBoxUnidadeMedida = new HBox(5, campoUnidadeMedida, btnNovaUnidadeMedida);
    HBox.setHgrow(campoUnidadeMedida, Priority.ALWAYS);
    hBoxUnidadeMedida.setAlignment(Pos.CENTER_LEFT);


    // LINHA DE INFORMAÇÕES PRINCIPAIS Descrição e Código
    VBox linhaDescricao = new VBox(5, labelDescricao, campoDescricao);
    VBox linhaCodigoInterno = new VBox(5, labelCodigoInterno, hBoxGerarCodInterno);
    VBox.setVgrow(campoDescricao, Priority.NEVER);

    GridPane containerInfoPrincipal = new GridPane();
    containerInfoPrincipal.setHgap(20);
    containerInfoPrincipal.setVgap(10);
    containerInfoPrincipal.setPadding(new Insets(10, 0, 0, 0));

    ColumnConstraints ccDesc = new ColumnConstraints();
    ccDesc.setHgrow(Priority.ALWAYS);
    ccDesc.setPercentWidth(65);
    ColumnConstraints ccCod = new ColumnConstraints();
    ccCod.setHgrow(Priority.ALWAYS);
    ccCod.setPercentWidth(35);

    containerInfoPrincipal.getColumnConstraints().addAll(ccDesc, ccCod);
    containerInfoPrincipal.add(linhaDescricao, 0, 0);
    containerInfoPrincipal.add(linhaCodigoInterno, 1, 0);


    // LINHA DE DETALHES Grid de 4 colunas

    GridPane linhaDetalhes = new GridPane();
    linhaDetalhes.setHgap(20);
    linhaDetalhes.setVgap(10);
    linhaDetalhes.setPadding(new Insets(10, 0, 0, 0));

    ColumnConstraints cc1 = new ColumnConstraints();
    cc1.setHgrow(Priority.ALWAYS);
    linhaDetalhes.getColumnConstraints().addAll(cc1, cc1, cc1, cc1);

    // Linha 0: Rótulos Identificação
    linhaDetalhes.add(labelEAN, 0, 0);
    linhaDetalhes.add(labelMarca, 1, 0);
    linhaDetalhes.add(labelTipoProduto, 2, 0);
    linhaDetalhes.add(labelUnidadeMedida, 3, 0);

    // Linha 1: Campos Identificação
    linhaDetalhes.add(campoEAN, 0, 1);
    linhaDetalhes.add(hBoxMarca, 1, 1);
    linhaDetalhes.add(campoTipoProduto, 2, 1);
    linhaDetalhes.add(hBoxUnidadeMedida, 3, 1);

    // Linha 2: Rótulos Classificação
    linhaDetalhes.add(labelGrupo, 0, 2);
    linhaDetalhes.add(labelSubgrupo, 1, 2);
    linhaDetalhes.add(labelCategoria, 2, 2);
    linhaDetalhes.add(labelUnidadeVenda, 3, 2);

    // Linha 3: Campos Classificação
    linhaDetalhes.add(hBoxGrupo, 0, 3);
    linhaDetalhes.add(hBoxSubgrupo, 1, 3);
    linhaDetalhes.add(campoCategoria, 2, 3);
    linhaDetalhes.add(campoUnidadeVenda, 3, 3);

    // Linha 4: Rótulos Valores e Compra
    linhaDetalhes.add(labelPrecoCusto, 0, 4);
    linhaDetalhes.add(labelMarkup, 1, 4);
    linhaDetalhes.add(labelPrecoVenda, 2, 4);
    linhaDetalhes.add(labelUnidadeCompra, 3, 4);

    // Linha 5: Campos Valores e Compra
    linhaDetalhes.add(campoPrecoCusto, 0, 5);
    linhaDetalhes.add(campoMarkup, 1, 5);
    linhaDetalhes.add(campoPrecoVenda, 2, 5);
    linhaDetalhes.add(campoUnidadeCompra, 3, 5);

    // Linha 6: Rótulos Estoque
    linhaDetalhes.add(labelEstoqueAtual, 0, 6);
    linhaDetalhes.add(labelEstoqueMinimo, 1, 6);
    linhaDetalhes.add(labelLocalizacao, 2, 6);
    GridPane.setColumnSpan(labelLocalizacao, 2);

    // Linha 7: Campos Estoque
    linhaDetalhes.add(campoEstoqueAtual, 0, 7);
    linhaDetalhes.add(campoEstoqueMinimo, 1, 7);
    linhaDetalhes.add(campoLocalizacao, 2, 7);
    GridPane.setColumnSpan(campoLocalizacao, 2);


    // Preenchimento automático de largura (para os campos e HBoxes)
    GridPane.setFillWidth(campoEAN, true);
    GridPane.setFillWidth(hBoxMarca, true);
    GridPane.setFillWidth(campoTipoProduto, true);
    GridPane.setFillWidth(hBoxUnidadeMedida, true);
    GridPane.setFillWidth(hBoxGrupo, true);
    GridPane.setFillWidth(hBoxSubgrupo, true);
    GridPane.setFillWidth(campoCategoria, true);
    GridPane.setFillWidth(campoUnidadeVenda, true);
    GridPane.setFillWidth(campoPrecoCusto, true);
    GridPane.setFillWidth(campoMarkup, true);
    GridPane.setFillWidth(campoPrecoVenda, true);
    GridPane.setFillWidth(campoUnidadeCompra, true);
    GridPane.setFillWidth(campoEstoqueAtual, true);
    GridPane.setFillWidth(campoEstoqueMinimo, true);
    GridPane.setFillWidth(campoLocalizacao, true);

    campoTipoProduto.setOnAction(e -> {
        String selecionado = campoTipoProduto.getValue();
        boolean isServico = Objects.equals(selecionado, "Serviço");

        // Desabilita campos que não serve para serviço
        labelUnidadeMedida.setDisable(isServico);
        hBoxUnidadeMedida.setDisable(isServico);
        labelUnidadeVenda.setDisable(isServico);
        campoUnidadeVenda.setDisable(isServico);

        labelUnidadeCompra.setDisable(isServico);
        campoUnidadeCompra.setDisable(isServico);

        labelEstoqueAtual.setDisable(isServico);
        campoEstoqueAtual.setDisable(isServico);
        labelEstoqueMinimo.setDisable(isServico);
        campoEstoqueMinimo.setDisable(isServico);
        labelLocalizacao.setDisable(isServico);
        campoLocalizacao.setDisable(isServico);

        // Limpa os campos se for desabilitados
        if (isServico) {
            campoUnidadeMedida.setValue(null);
            campoUnidadeVenda.setValue(null);
            campoUnidadeCompra.setValue(null);
            campoEstoqueAtual.clear();
            campoEstoqueMinimo.clear();
            campoLocalizacao.clear();
        }
    });

    // OBSERVAÇÕES
    VBox linhaObservacoes = new VBox(5, labelObservacoes, campoObservacoes);
    VBox.setVgrow(campoObservacoes, Priority.ALWAYS); // Faz a área de texto crescer


    // LAYOUT PRINCIPAL DO CONTEÚDO
    VBox layoutGeral = new VBox(20, containerInfoPrincipal, linhaDetalhes, linhaObservacoes);
    layoutGeral.setPadding(new Insets(20));

    VBox.setVgrow(linhaObservacoes, Priority.ALWAYS);

    VBox blocoBrancoEstilizado = new VBox();
    blocoBrancoEstilizado.getStyleClass().add("bloco-branco");


    blocoBrancoEstilizado.getChildren().add(layoutGeral);


    // LAYOUT PRINCIPAL DA ABA INTEIRA
    VBox layoutPrincipal = new VBox(15, botoesAcaoNovoClienteAndBuscaCliente, blocoBrancoEstilizado);

    VBox.setVgrow(blocoBrancoEstilizado, Priority.ALWAYS);


    VBox.setVgrow(layoutGeral, Priority.ALWAYS);

    layoutPrincipal.setMaxWidth(Double.MAX_VALUE);
    layoutPrincipal.setPadding(new Insets(20));
    layoutPrincipal.getStyleClass().add("layout-geral-produto-cadastro");
    return layoutPrincipal;
}
}
