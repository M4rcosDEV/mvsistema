package com.sistema.mvsistema.view.components;

import atlantafx.base.theme.Styles;
import com.sistema.mvsistema.dto.ClienteBusca;
import com.sistema.mvsistema.service.ClienteService;
import com.sistema.mvsistema.util.DocumentoUtil;
import com.sistema.mvsistema.view.cadastros.ClienteView;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class JanelaBuscarCliente {
    @Autowired
    private ClienteService clienteService;

    public Optional<ClienteBusca> mostrar() {
        Stage janela = new Stage();

        // Filtros de busca
        TextField campoBuscaNome = new TextField();
        TextField campoBuscaCpfCnpj = new TextField();
        ComboBox<String> campoBuscaTipoPessoa = new ComboBox<>();
        TextField campoBuscaTelefone = new TextField();
        ComboBox<String> campoBuscaClassificacao = new ComboBox<>();
        TextField campoBuscaMunicipio = new TextField();


        //janelaBuscarClienteStage.setResizable(false);
        janela.getIcons().add(new Image(Objects.requireNonNull(ClienteView.class.getResourceAsStream("/static/imagens/logo.png"))));
        janela.setTitle("Buscar Cliente");
        janela.initModality(Modality.APPLICATION_MODAL);

        // ======== CAMPOS DE FILTRO ========

        Label labelNome = new Label("Nome");
        campoBuscaNome.setPromptText("Digite aqui...");

        VBox containerNome = new VBox(labelNome, campoBuscaNome);

        Label labelCpfCnpj = new Label("CPF/CNPJ");
        campoBuscaCpfCnpj.setPromptText("Digite aqui...");

        DocumentoUtil.aplicarMascaraCpfCnpj(campoBuscaCpfCnpj);

        VBox containerCpfCnpj = new VBox(labelCpfCnpj, campoBuscaCpfCnpj);

        Label labelTipoPessoa = new Label("Tipo de pessoa");
        campoBuscaTipoPessoa.getItems().addAll("Física", "Jurídica", "Produtor", "Instituição");
        campoBuscaTipoPessoa.setPromptText("Selecione...");

        VBox containerTipoPessoa = new VBox(labelTipoPessoa, campoBuscaTipoPessoa);

        HBox linha1 = new HBox(20, containerNome, containerCpfCnpj, containerTipoPessoa);
        linha1.setAlignment(Pos.CENTER_LEFT);

        // Segunda linha
        Label labelTelefone = new Label("Telefone");
        campoBuscaTelefone.setPromptText("(DDD) 9XXXX-XXXX");
        DocumentoUtil.aplicarMascaraTelefone(campoBuscaTelefone);

        VBox containerTelefone = new VBox(labelTelefone, campoBuscaTelefone);

        Label labelClassificacao = new Label("Classificação");
        campoBuscaClassificacao.getItems().addAll("Cliente", "Fornecedor", "Funcionário", "Outros");
        campoBuscaClassificacao.setPromptText("Selecione");
        VBox containerClassificacao = new VBox(labelClassificacao, campoBuscaClassificacao);

        Label labelMunicipio = new Label("Municipio");
        campoBuscaMunicipio.setPromptText("Digite aqui...");
        VBox containerMunicipio = new VBox(labelMunicipio, campoBuscaMunicipio);

        HBox linha2 = new HBox(20, containerTelefone, containerClassificacao, containerMunicipio);
        linha2.setAlignment(Pos.CENTER_LEFT);

        // ======== PAGINAÇÃO ========
        Pagination paginacao = new Pagination();
        paginacao.setVisible(false);
        paginacao.setMaxPageIndicatorCount(5);


        // ======== BOTÃO DE PESQUISA ========
        Button btnPesquisar = new Button("Pesquisar", new FontIcon(Feather.SEARCH));
        btnPesquisar.getStyleClass().add(Styles.ACCENT);

        HBox linhaBotao = new HBox(btnPesquisar);
        linhaBotao.setAlignment(Pos.CENTER_RIGHT);

        VBox filtrosBox = new VBox(20,
                new Label("Filtros de Pesquisa:"),
                linha1,
                linha2,
                linhaBotao
        );

        filtrosBox.getStyleClass().add("filtros-box");

        // ======== TABELA DE RESULTADOS ========
        var colId = new TableColumn<ClienteBusca, String>("Codigo");
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        var colNome = new TableColumn<ClienteBusca, String>("Nome");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));

        var colCpfCnpj = new TableColumn<ClienteBusca, String>("CPF/CNPJ");
        colCpfCnpj.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCpfCnpj()));

        var colTipo = new TableColumn<ClienteBusca, String>("Tipo");
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoPessoa()));

        var colTelefone = new TableColumn<ClienteBusca, String>("Telefone");
        colTelefone.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefone()));

        var tabela = new TableView<ClienteBusca>();
        tabela.getColumns().setAll(colId, colNome, colCpfCnpj, colTipo, colTelefone);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        tabela.setPrefHeight(350);

        final ClienteBusca[] clienteSelecionado = {null};

        //VERIFICADO
        btnPesquisar.setOnAction(e -> {
            int itensPorPagina = 10;

            Pageable pageableInicial = PageRequest.of(0, itensPorPagina);
            Page<ClienteBusca> paginaClientes = clienteService.buscarClientes(campoBuscaNome.getText(), campoBuscaTipoPessoa.getValue(), campoBuscaCpfCnpj.getText(), pageableInicial);

            tabela.getItems().clear();

            tabela.getItems().addAll(paginaClientes.getContent());

            if (paginaClientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado!");
                return;
            }

            //Numero de paginas obtidas
            int totalPaginas = paginaClientes.getTotalPages();

            if (totalPaginas == 1){
                paginacao.setVisible(false);
            }else {
                paginacao.setVisible(true);
            }

            paginacao.setPageCount(totalPaginas);
            paginacao.setCurrentPageIndex(0);

            // Atualiza a tabela quando o usuário troca de página
            paginacao.setPageFactory(pageIndex -> {
                Pageable novaPagina = PageRequest.of(pageIndex, itensPorPagina);
                Page<ClienteBusca> novaPaginaClientes = clienteService.buscarClientes(
                        campoBuscaNome.getText(),
                        campoBuscaTipoPessoa.getValue(),
                        campoBuscaCpfCnpj.getText(),
                        novaPagina
                );

                tabela.getItems().setAll(novaPaginaClientes.getContent());
                return new StackPane();
            });
        });

        // ======== BOTÕES INFERIORES ========
        Button btnSelecionar = new Button("Selecionar");
        Button btnFechar = new Button("Fechar");

        btnSelecionar.getStyleClass().add(Styles.ACCENT);
        btnFechar.getStyleClass().add(Styles.BUTTON_OUTLINED);


        HBox botoesInferiores = new HBox(10, btnSelecionar, btnFechar);
        botoesInferiores.setAlignment(Pos.CENTER_RIGHT);
        botoesInferiores.setPadding(new Insets(10, 0, 0, 0));

        // ======== LAYOUT FINAL ========
        VBox layout = new VBox(15,
                filtrosBox,
                tabela,
                paginacao,
                botoesInferiores
        );

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #ffffff;");


        //EVENTOS

        //Selecionar
        btnSelecionar.setOnAction(e -> {
            clienteSelecionado[0] = tabela.getSelectionModel().getSelectedItem();
            janela.close();
        });

        //Selecionar cliente com double click
        tabela.setRowFactory(e ->{
            TableRow<ClienteBusca> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    clienteSelecionado[0] = tabela.getSelectionModel().getSelectedItem();
                    janela.close();
                }
            });
            return row;
        });

        //Fechar
        btnFechar.setOnAction(e -> janela.close());

        Scene cena = new Scene(layout, 900, 730);
        janela.setScene(cena);
        janela.showAndWait();

        return Optional.ofNullable(clienteSelecionado[0]);
    }
}
