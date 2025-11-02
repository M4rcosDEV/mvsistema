package com.sistema.mvsistema.view;

import atlantafx.base.theme.Styles;
import com.sistema.mvsistema.dto.ClienteBusca;
import com.sistema.mvsistema.dto.EnderecoDto;
import com.sistema.mvsistema.model.Cliente;
import com.sistema.mvsistema.model.Municipio;
import com.sistema.mvsistema.repository.ClienteRepository;
import com.sistema.mvsistema.service.ClienteService;
import com.sistema.mvsistema.util.DocumentoUtil;
import com.sistema.mvsistema.util.Utils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
public class ClienteView {

    // Identificação
    private TextField campoNomeCliente = new TextField();
    private TextField campoNomeFantasiaAndApelido = new TextField();
    private TextField campoTelefone = new TextField();
    private TextField campoCPF = new TextField();
    private TextField campoRG = new TextField();
    private TextField campoCNPJ = new TextField();
    private DatePicker campoDataNascimento = new DatePicker();
    private TextField campoEmail = new TextField();
    private ComboBox<String> campoEstadoCivil = new ComboBox<>();
    private ComboBox<String> campoGenero = new ComboBox<>();
    private ComboBox<String> campoTipoPessoa = new ComboBox<>();

    // Endereço
    private TextField campoNomeEndereco = new TextField();
    private ComboBox<String> campoTipoEndereco = new ComboBox<>();
    private TextField campoCEP = new TextField();
    private TextField campoRua = new TextField();
    private TextField campoNumero = new TextField();
    private TextField campoComplemento = new TextField();
    private TextField campoBairro = new TextField();
    private TextField campoMunicipioEndereco = new TextField();
    private TextField campoEstado = new TextField();
    private TextField campoPais = new TextField();

    // ======== CAMPOS DA JANELA DE BUSCA DE CLIENTE ========

    // Filtros de busca
    private TextField campoBuscaNome = new TextField();
    private TextField campoBuscaCpfCnpj = new TextField();
    private ComboBox<String> campoBuscaTipoPessoa = new ComboBox<>();
    private TextField campoBuscaTelefone = new TextField();
    private ComboBox<String> campoBuscaClassificacao = new ComboBox<>();
    private TextField campoBuscaMunicipio = new TextField();

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    public Pane createAbaGeral() {

        // --- 1. CAMPOS DE TEXTO E SELEÇÃO ---

        // Nome completo
        Label labelNome = new Label("Nome*");
        campoNomeCliente.setPromptText("Nome do cliente");
        Platform.runLater(campoNomeCliente::requestFocus);
        campoNomeCliente.setDisable(true);

        // Nome fantasia / apelido
        Label labelNomeFantasiaAndApelido = new Label("Sobrenome");
        campoNomeFantasiaAndApelido.setPromptText("Sobrenome");
        campoNomeFantasiaAndApelido.setDisable(true);

        // Documentos e dados pessoais
        Label labelCPF = new Label("CPF");
        campoCPF.setPromptText("000.000.000-00");
        DocumentoUtil.aplicarMascaraCpf(campoCPF);
        campoCPF.setDisable(true);

        Label labelRG = new Label("RG / Inscrição Estadual");
        campoRG.setDisable(true);

        Label labelCNPJ = new Label("CNPJ");
        campoCNPJ.setPromptText("00.000.000/0000-00");
        DocumentoUtil.aplicarMascaraCnpj(campoCNPJ);
        campoCNPJ.setDisable(true);

        Label labelDataNascimento = new Label("Data Nascimento");
        campoDataNascimento.setPromptText("dd/mm/aaaa");
        campoDataNascimento.setDisable(true);

        // Contato
        Label labelTelefone = new Label("Telefone");
        campoTelefone.setPromptText("(DDD) 9XXXX-XXXX");
        DocumentoUtil.aplicarMascaraTelefone(campoTelefone);
        campoTelefone.setDisable(true);

        Label labelEmail = new Label("E-mail");
        campoEmail.setPromptText("contato@exemplo.com");
        campoEmail.setDisable(true);

        // Seleções
        Label labelGenero = new Label("Gênero");
        campoGenero.getItems().addAll("Masculino", "Feminino", "Outro", "Não informar");
        campoGenero.setPromptText("Selecione");
        campoGenero.setDisable(true);

        Label labelTipoPessoa = new Label("Tipo de pessoa");
        campoTipoPessoa.getItems().addAll("Física", "Jurídica", "Produtor", "Instituição");
        campoTipoPessoa.setPromptText("Selecione o tipo de pessoa");
        campoTipoPessoa.setDisable(true);

        Label labelEstadoCivil = new Label("Estado Civil");
        campoEstadoCivil.getItems().addAll(
                "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "União Estável"
        );
        campoEstadoCivil.setPromptText("Selecione");
        campoEstadoCivil.setDisable(true);

        // Observações
        Label labelObservacoes = new Label("Observações");
        TextArea campoObservacoes = new TextArea();
        campoObservacoes.setPromptText("Informações adicionais, notas, etc.");
        campoObservacoes.setPrefRowCount(3);
        campoObservacoes.setDisable(true);

        // --- 2. LINHA DE NOMES ---

        VBox linhaNome = new VBox(5, labelNome, campoNomeCliente);
        VBox linhaNomeFantasiaAndApelido = new VBox(5, labelNomeFantasiaAndApelido, campoNomeFantasiaAndApelido);
        VBox.setVgrow(campoNomeCliente, Priority.NEVER);

        GridPane containerPrimeiroNomeAndSegundo = new GridPane();
        containerPrimeiroNomeAndSegundo.setHgap(20);
        containerPrimeiroNomeAndSegundo.setVgap(10);
        containerPrimeiroNomeAndSegundo.setPadding(new Insets(10, 0, 0, 0));

        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);

        containerPrimeiroNomeAndSegundo.getColumnConstraints().addAll(cc2, cc2);
        containerPrimeiroNomeAndSegundo.add(linhaNome, 0, 0);
        containerPrimeiroNomeAndSegundo.add(linhaNomeFantasiaAndApelido, 1, 0);


        // --- 3. LINHA DE DETALHES ---

        GridPane linhaDetalhes = new GridPane();
        linhaDetalhes.setHgap(20);
        linhaDetalhes.setVgap(10);
        linhaDetalhes.setPadding(new Insets(10, 0, 0, 0));

        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.ALWAYS);
        linhaDetalhes.getColumnConstraints().addAll(cc1, cc1, cc1, cc1);

        // Linha 0: Rótulos
        linhaDetalhes.add(labelCPF, 0, 0);
        linhaDetalhes.add(labelRG, 1, 0);
        linhaDetalhes.add(labelTipoPessoa, 2, 0);
        linhaDetalhes.add(labelDataNascimento, 3, 0);

        // Linha 1: Campos
        linhaDetalhes.add(campoCPF, 0, 1);
        linhaDetalhes.add(campoRG, 1, 1);
        linhaDetalhes.add(campoTipoPessoa, 2, 1);
        linhaDetalhes.add(campoDataNascimento, 3, 1);

        // Linha 2: Rótulos
        linhaDetalhes.add(labelTelefone, 0, 2);
        linhaDetalhes.add(labelEmail, 1, 2);
        linhaDetalhes.add(labelGenero, 2, 2);
        linhaDetalhes.add(labelEstadoCivil, 3, 2);

        // Linha 3: Campos
        linhaDetalhes.add(campoTelefone, 0, 3);
        linhaDetalhes.add(campoEmail, 1, 3);
        linhaDetalhes.add(campoGenero, 2, 3);
        linhaDetalhes.add(campoEstadoCivil, 3, 3);

        // Preenchimento automático de largura
        GridPane.setFillWidth(campoCPF, true);
        GridPane.setFillWidth(campoRG, true);
        GridPane.setFillWidth(campoCNPJ, true);
        GridPane.setFillWidth(campoDataNascimento, true);
        GridPane.setFillWidth(campoTelefone, true);
        GridPane.setFillWidth(campoEmail, true);
        GridPane.setFillWidth(campoGenero, true);
        GridPane.setFillWidth(campoEstadoCivil, true);

        campoTipoPessoa.setOnAction(e -> {
            String selecionado = campoTipoPessoa.getValue();

            if(Objects.equals(selecionado, "Física") || Objects.equals(selecionado, "Produtor")){
                labelNome.setText("Nome");
                labelNomeFantasiaAndApelido.setText("Sobrenome");
                campoNomeCliente.setPromptText("Nome do cliente");
                campoNomeFantasiaAndApelido.setPromptText("Sobrenome");
                labelCPF.setText("CPF");
                campoCPF.setPromptText("000.000.000-00");
            } else {
                labelNome.setText("Razão social");
                labelNomeFantasiaAndApelido.setText("Nome Fantasia");
                campoNomeCliente.setPromptText("Razão social");
                campoNomeFantasiaAndApelido.setPromptText("Nome Fantasia");
                labelCPF.setText("CNPJ");
                campoCPF.setPromptText("00.000.000/0000-00");
            }
        });

        // --- 4. OBSERVAÇÕES ---
        VBox linhaObservacoes = new VBox(5, labelObservacoes, campoObservacoes);
        VBox.setVgrow(campoObservacoes, Priority.ALWAYS);


        // --- 5. LAYOUT PRINCIPAL ---
        VBox layoutGeral = new VBox(20, containerPrimeiroNomeAndSegundo, linhaDetalhes, linhaObservacoes);
        layoutGeral.setPadding(new Insets(20));

        VBox.setVgrow(linhaObservacoes, Priority.ALWAYS);

        return layoutGeral;
    }

    public Pane createAbaEndereco() {
        Button btnNovoEndereco = new Button(
                "Novo endereço", new FontIcon (Feather.PLUS)
        );
        btnNovoEndereco.getStyleClass().add(Styles.ACCENT);
        btnNovoEndereco.setMnemonicParsing(false);
        btnNovoEndereco.setFocusTraversable(false);

        Button btnSalvar = new Button(
                "Salvar", new FontIcon (Feather.SAVE)
        );

        btnSalvar.getStyleClass().add(Styles.ACCENT);
        btnSalvar.setMnemonicParsing(false);
        btnSalvar.setFocusTraversable(false);
        btnSalvar.setDisable(true);

        HBox botoesAcaoNovoEnderecoAndEditarEndereco = new HBox();
        botoesAcaoNovoEnderecoAndEditarEndereco.getChildren().addAll(btnNovoEndereco, btnSalvar);
        botoesAcaoNovoEnderecoAndEditarEndereco.setAlignment(Pos.CENTER_LEFT);
        botoesAcaoNovoEnderecoAndEditarEndereco.setSpacing(15);

        // --- 1. CAMPOS DE TEXTO - DESATIVOS - E SELEÇÃO ---

        // Nome e Tipo
        Label labelNome = new Label("Nome do Endereço");
        campoNomeEndereco.setPromptText("Ex: Casa, Escritório, Sede");
        // Foco no primeiro campo
        Platform.runLater(campoNomeEndereco::requestFocus);
        campoNomeEndereco.setDisable(true);


        Label labelTipoEndereco = new Label("Tipo de Endereço*");
        campoTipoEndereco.getItems().addAll("Residencial", "Comercial", "Entrega", "Cobrança", "Outro");
        campoTipoEndereco.setPromptText("Selecione o tipo");
        campoTipoEndereco.setDisable(true);

        // Localização
        Label labelCEP = new Label("CEP");
        campoCEP.setPromptText("00000-000");
        DocumentoUtil.aplicarMascaraCep(campoCEP);
        campoCEP.setDisable(true);

        Label labelRua = new Label("Rua");
        campoRua.setPromptText("Rua, Avenida, etc.");
        campoRua.setDisable(true);

        Label labelNumero = new Label("Número");
        campoNumero.setPromptText("Nº");
        campoNumero.setDisable(true);

        Label labelComplemento = new Label("Complemento*");
        campoComplemento.setPromptText("Apto, Bloco, Sala");
        campoComplemento.setDisable(true);

        Label labelBairro = new Label("Bairro");
        campoBairro.setPromptText("Bairro");
        campoBairro.setDisable(true);

        Label labelMunicipio = new Label("Municipio");
        campoMunicipioEndereco.setPromptText("Municipio");
        campoMunicipioEndereco.setDisable(true);

        Label labelEstado = new Label("Estado");
        campoEstado.setPromptText("UF (Ex: SP, BA)");
        campoEstado.setDisable(true);

        Label labelPais = new Label("País");
        campoPais.setPromptText("País");
        campoPais.setText("Brasil");
        campoPais.setDisable(true);

        // --- 2. LAYOUT EM GRADE ---
        // Usando a mesma estrutura de grade 4 colunas do seu createAbaGeral()

        GridPane gridEndereco = new GridPane();
        gridEndereco.setHgap(20);
        gridEndereco.setVgap(10);
        gridEndereco.setPadding(new Insets(10, 0, 0, 0));

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        // 4 colunas com crescimento igual
        gridEndereco.getColumnConstraints().addAll(cc, cc, cc, cc);

        // Linha 0: Rótulos
        gridEndereco.add(labelNome, 0, 0);
        gridEndereco.add(labelTipoEndereco, 1, 0);
        gridEndereco.add(labelCEP, 2, 0);
        gridEndereco.add(labelPais, 3, 0);

        // Linha 1: Campos
        gridEndereco.add(campoNomeEndereco, 0, 1);
        gridEndereco.add(campoTipoEndereco, 1, 1);
        gridEndereco.add(campoCEP, 2, 1);
        gridEndereco.add(campoPais, 3, 1);

        // Linha 2: Rótulos
        gridEndereco.add(labelRua, 0, 2);
        gridEndereco.add(labelNumero, 1, 2);
        gridEndereco.add(labelComplemento, 2, 2);
        gridEndereco.add(labelBairro, 3, 2);

        // Linha 3: Campos
        gridEndereco.add(campoRua, 0, 3);
        gridEndereco.add(campoNumero, 1, 3);
        gridEndereco.add(campoComplemento, 2, 3);
        gridEndereco.add(campoBairro, 3, 3);
        // Fazendo a rua ocupar 2 colunas para mais espaço
        GridPane.setColumnSpan(campoRua, 2);
        // Reajustando a linha 3 por causa do span
        gridEndereco.getChildren().removeAll(campoNumero, labelComplemento, campoComplemento, labelBairro, campoBairro); // Limpa
        gridEndereco.add(campoNumero, 2, 3);     // Move Número para col 2
        gridEndereco.add(labelComplemento, 3, 2); // Move Complemento label para col 3
        gridEndereco.add(campoComplemento, 3, 3); // Move Complemento campo para col 3


        // Linha 4: Rótulos
        gridEndereco.add(labelBairro, 0, 4); // Bairro movido para cá
        gridEndereco.add(labelMunicipio, 1, 4);
        gridEndereco.add(labelEstado, 2, 4);

        // Linha 5: Campos
        gridEndereco.add(campoBairro, 0, 5); // Bairro movido para cá
        gridEndereco.add(campoMunicipioEndereco, 1, 5);
        gridEndereco.add(campoEstado, 2, 5);
        // Bairro e Municipio podem ocupar mais espaço
        GridPane.setColumnSpan(campoBairro, 2);
        GridPane.setColumnSpan(campoMunicipioEndereco, 2);
        // Reajuste final
        gridEndereco.getChildren().removeAll(labelMunicipio, campoMunicipioEndereco, labelEstado, campoEstado);
        gridEndereco.add(labelMunicipio, 2, 4); // Municipio label na col 2 (span 2)
        gridEndereco.add(campoMunicipioEndereco, 2, 5); // Municipio campo na col 2 (span 2)
        gridEndereco.add(labelEstado, 0, 6); // Estado na próxima linha
        gridEndereco.add(campoEstado, 0, 7); // Estado na próxima linha


        // Preenchimento automático de largura (igual ao seu)
        GridPane.setFillWidth(campoNomeEndereco, true);
        GridPane.setFillWidth(campoTipoEndereco, true);
        GridPane.setFillWidth(campoCEP, true);
        GridPane.setFillWidth(campoPais, true);
        GridPane.setFillWidth(campoRua, true);
        GridPane.setFillWidth(campoNumero, true);
        GridPane.setFillWidth(campoComplemento, true);
        GridPane.setFillWidth(campoBairro, true);
        GridPane.setFillWidth(campoMunicipioEndereco, true);
        GridPane.setFillWidth(campoEstado, true);

        //nome, tipo_endereco, cep, municipio
        // ======== TABELA DE ENDEREÇOS ========

        var colNome = new TableColumn<EnderecoDto, String>("Nome");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));

        var colTipoEndereco = new TableColumn<EnderecoDto, String>("Tipo endereço");
        colTipoEndereco.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoEndereco()));

        var colCep = new TableColumn<EnderecoDto, String>("Cep");
        colCep.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCep()));

        var colMunicipio = new TableColumn<EnderecoDto, String>("Municipio");
        colMunicipio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMunicipio()));

        var tabela = new TableView<EnderecoDto>();
        tabela.getColumns().setAll(colNome, colTipoEndereco, colCep, colMunicipio);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabela.setPrefHeight(350);

        // --- 3. LAYOUT PRINCIPAL ---
        VBox layoutEndereco = new VBox(20,botoesAcaoNovoEnderecoAndEditarEndereco, gridEndereco, tabela); // 20 de espaçamento
        layoutEndereco.setPadding(new Insets(20)); // 20 de padding

        // Para o caso de esta aba precisar crescer (como a de observações)
        VBox.setVgrow(gridEndereco, Priority.ALWAYS);

        return layoutEndereco;
    }

    public void abrirJanelaBuscarCliente() {
        Stage janelaBuscarClienteStage  = new Stage();
        janelaBuscarClienteStage.getIcons().add(new Image(Objects.requireNonNull(ClienteView.class.getResourceAsStream("/static/imagens/logo.png"))));
        janelaBuscarClienteStage.setTitle("Buscar Cliente");
        janelaBuscarClienteStage.initModality(Modality.APPLICATION_MODAL);

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
        //filtrosBox.setPadding(new Insets(10));
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

        tabela.setOnMouseClicked(e->{
            ClienteBusca selecionado = tabela.getSelectionModel().getSelectedItem();

            System.out.println(selecionado.getId());
        });

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

        btnFechar.setOnAction(e -> janelaBuscarClienteStage.close());

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
        btnSelecionar.setOnAction(e -> {
            ClienteBusca clienteSelecionado = tabela.getSelectionModel().getSelectedItem();

            Optional<Cliente> clienteOptional = clienteRepository.findById(clienteSelecionado.getId());

            if (clienteOptional.isPresent()) {
                janelaBuscarClienteStage.close();

                Cliente cliente = clienteOptional.get();

                campoNomeCliente.setDisable(false);
                campoNomeFantasiaAndApelido.setDisable(false);
                campoTelefone.setDisable(false);
                campoEmail.setDisable(false);
                campoCPF.setDisable(false);
                campoCNPJ.setDisable(false);
                campoRG.setDisable(false);
                campoDataNascimento.setDisable(false);
                campoEstadoCivil.setDisable(false);
                campoGenero.setDisable(false);
                campoTipoPessoa.setDisable(false);

                campoNomeCliente.setText(cliente.getNome());
                campoNomeFantasiaAndApelido.setText(cliente.getSobrenome());
                campoTelefone.setText(cliente.getTelefone());
                campoEmail.setText(cliente.getEmail());
                campoCPF.setText(cliente.getCpfCnpj());
                campoCNPJ.setText(cliente.getCpfCnpj());
                campoRG.setText(cliente.getRegistro());
                campoDataNascimento.setValue(cliente.getDataNascimento());
                campoEstadoCivil.setValue(cliente.getEstadoCivil());
                campoGenero.setValue(cliente.getGenero());
                campoTipoPessoa.setValue(Utils.converterTipoPessoaCharParaString(cliente.getTipoPessoa()));
            }
        });

        Scene cena = new Scene(layout, 900, 730);
        janelaBuscarClienteStage.setScene(cena);
        janelaBuscarClienteStage.showAndWait();
    }
}
