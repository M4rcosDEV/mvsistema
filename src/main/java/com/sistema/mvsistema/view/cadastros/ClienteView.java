package com.sistema.mvsistema.view.cadastros;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import com.sistema.mvsistema.dto.EnderecoDTO;
import com.sistema.mvsistema.entity.Cliente;
import com.sistema.mvsistema.entity.Endereco;
import com.sistema.mvsistema.entity.Estado;
import com.sistema.mvsistema.entity.Municipio;
import com.sistema.mvsistema.entity.enums.EstadoFormulario;
import com.sistema.mvsistema.repository.ClienteRepository;
import com.sistema.mvsistema.service.CacheService;
import com.sistema.mvsistema.service.CepService;
import com.sistema.mvsistema.service.ClienteService;
import com.sistema.mvsistema.util.*;
import com.sistema.mvsistema.view.components.JanelaBuscarCliente;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.sistema.mvsistema.view.components.NotificacaoComponent.*;

@Component
public class ClienteView {
    private static final Logger log = LoggerFactory.getLogger(ClienteView.class);
    private Cliente clienteAtual;
    EstadoFormulario estado = EstadoFormulario.NOVO;

    //Botoes
    private Button btnNovoCliente;
    private Button btnCancelaOpeCliente = new Button();
    private Button btnNovoEndereco = new Button();
    private Button btnSalvarEndereco = new Button();
    private Button btnConsultarCep = new Button();
    private Button btnAlterarEndereco = new Button();
    private Button btnCancelaOpeEndereco = new Button();

    // Identificação
    private TextField campoCodCliente = new TextField();
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
    private TextArea campoObservacoes = new TextArea();

    //Componentes da Aba Endereço
    private TableView<Endereco> tabelaEnderecos;
    private ObservableList<Endereco> listaObservavelEnderecos;

    // Endereço
    private TextField campoNomeEndereco = new TextField();
    private ComboBox<String> campoTipoEndereco = new ComboBox<>();
    private TextField campoCEP = new TextField();
    private TextField campoRua = new TextField();
    private TextField campoNumero = new TextField();
    private TextField campoComplemento = new TextField();
    private TextField campoBairro = new TextField();
    private CustomTextField campoMunicipioEndereco = new CustomTextField();
    private CustomTextField campoEstado = new CustomTextField();
    private TextField campoPais = new TextField();

    LoadingIndicadorUtil loading = new LoadingIndicadorUtil();

    private Endereco enderecoEmEdicao;
    private boolean editandoEnderecoExistente = false;

    private AutoCompleteTextField<Municipio> autoCompleteListaMunicipios;

    private AutoCompleteTextField<Estado> autoCompleteListaEstados;

    AtomicReference<DocumentoUtil.TipoDocumento> tipoAtual = new AtomicReference<>(DocumentoUtil.TipoDocumento.CPF);

    @Autowired
    private JanelaBuscarCliente janelaBuscarCliente;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CacheService cacheService;

    @Autowired
    CepService cepService;

    private Runnable onCloseCallback = () -> {};

    public void setOnCloseRequest(Runnable callback) {
        this.onCloseCallback = (callback != null) ? callback : () -> {};
    }

    public Pane criarCadastroClientePane() {
        Tooltip cancelarOperacaoTlp = new Tooltip("Cancelar operação");
        Tooltip btnFecharTelaTlp = new Tooltip("Fechar tela de cadastro");

        btnNovoCliente = new Button(
                "Novo cliente", new FontIcon (Feather.PLUS)
        );

        btnNovoCliente.getStyleClass().add(Styles.ACCENT);

        btnNovoCliente.setStyle(
                "-fx-background-radius: 6 0 0 6;"
        );

        btnCancelaOpeCliente = new Button(null, new FontIcon(Feather.X_OCTAGON));
        btnCancelaOpeCliente.setTooltip(cancelarOperacaoTlp);
        btnCancelaOpeCliente.setDisable(true);
        btnCancelaOpeCliente.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.DANGER
        );
        btnCancelaOpeCliente.setStyle(
                "-fx-background-radius: 0 6 6 0;"
        );
        btnCancelaOpeCliente.setMnemonicParsing(false);
        btnCancelaOpeCliente.setFocusTraversable(false);

        HBox containerBotoesAcao = new HBox();
        containerBotoesAcao.getChildren().addAll(btnNovoCliente, btnCancelaOpeCliente);
        containerBotoesAcao.setAlignment(Pos.CENTER);
        btnNovoCliente.setPrefHeight(36);
        btnCancelaOpeCliente.setPrefHeight(36);
        containerBotoesAcao.setSpacing(0);

        Button btnBuscar = new Button(
                "Buscar", new FontIcon (Feather.SEARCH)
        );

        btnBuscar.getStyleClass().add(Styles.ACCENT);
        btnBuscar.setMnemonicParsing(false);
        btnBuscar.setFocusTraversable(false);

        btnBuscar.setOnAction(e -> {
            janelaBuscarCliente.mostrar().ifPresent(cliente ->{
                carregarClienteSelecionado(cliente.getId());
            });
        });

        Button btnFechar = new Button(
                "Fechar", new FontIcon (Feather.X)
        );
        btnFechar.setTooltip(btnFecharTelaTlp);
        btnFechar.getStyleClass().add(Styles.DANGER);
        btnFechar.setMnemonicParsing(false);
        btnFechar.setFocusTraversable(false);

        HBox botoesAcaoNovoClienteAndBuscaCliente = new HBox();
        botoesAcaoNovoClienteAndBuscaCliente.getChildren().addAll(containerBotoesAcao, btnBuscar, btnFechar);
        botoesAcaoNovoClienteAndBuscaCliente.setAlignment(Pos.CENTER_LEFT);
        botoesAcaoNovoClienteAndBuscaCliente.setSpacing(15);

        Label labelCliente = new Label("Cliente");

        campoCodCliente.setEditable(false);
        campoCodCliente.setPrefWidth(100);

        VBox campoLayoutCodCliente = new VBox(5, labelCliente, campoCodCliente);
        campoLayoutCodCliente.setFillWidth(false);
        campoCodCliente.setMaxWidth(100);
        campoLayoutCodCliente.setAlignment(Pos.CENTER_LEFT);

        TabPane defaultTabs = new TabPane (
                new Tab ("Informações Gerais", createAbaGeral()),
                new Tab ("Endereço", createAbaEndereco()),
                new Tab ("Financeiro")
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



        StackPane rootPane = new StackPane(layoutPrincipal,loading);
        rootPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);


        //EVENTOS
        btnNovoCliente.setOnAction(e -> {
            switch (estado){
                case NOVO -> {
                    System.out.println("Estado NOVO -> Estado EDIÇÃO" );
                    btnNovoCliente.setText("Salvar cliente");
                    btnNovoEndereco.setDisable(false);
                    btnCancelaOpeCliente.setDisable(false);
                    btnNovoCliente.setGraphic(new FontIcon(Feather.CHECK));
                    btnNovoCliente.getStyleClass().add(Styles.SUCCESS);
                    prepararNovoCliente();
                    estado = EstadoFormulario.EDITANDO_NOVO;
                }
                case EDITANDO_NOVO, EDITANDO_EXISTENTE -> {

                    if(verificarCamposObrigatorios()){
                        if(!listaObservavelEnderecos.isEmpty()){
                            Cliente cliente = getClienteFormulario();

                            Task<Void> task = new Task<>() {
                                @Override
                                protected Void call() throws Exception {
                                    try {
                                        clienteRepository.save(cliente);
                                    }catch (Exception e){
                                        log.error("Erro ao salvar cliente dentro da Task", e);
                                        throw e;
                                    }
                                    return null;
                                }

                            };

                            task.setOnRunning(eventRunning ->{
                                loading.show();
                                btnNovoCliente.setDisable(true);
                                exibirNotificacao(rootPane, "Salvando cliente...", Styles.ACCENT);
                            });

                            task.setOnSucceeded(eventSuccess ->{
                                loading.hide();
                                btnNovoCliente.setDisable(false);
                                btnNovoCliente.setText("Novo cliente");

                                btnNovoCliente.getStyleClass().remove(Styles.SUCCESS);
                                btnNovoCliente.getStyleClass().add(Styles.ACCENT);
                                btnNovoCliente.setGraphic(new FontIcon(Feather.PLUS));
                                limparEBloquearFormulario();
                                limparFormEndereco();
                                listaObservavelEnderecos.clear();
                                estado = EstadoFormulario.NOVO;
                                System.out.println("Estado EDIÇÃO -> Estado NOVO" );
                                habilitarCamposCliente(false);
                                exibirNotificacao(rootPane, "Cliente salvo com sucesso!", Styles.SUCCESS);
                            });

                            task.setOnFailed(eventFailed ->{
                                loading.hide();
                                btnNovoCliente.setDisable(false);
                                Throwable erro = task.getException();
                                exibirNotificacao(rootPane, "Erro ao salvar cliente: "+ erro.getMessage(), Styles.DANGER);
                            });


                            new Thread(task).start();
                        }else{
                            exibirNotificacao(rootPane, "Adicione um endereço", Styles.DANGER, Pos.TOP_LEFT);
                        }
                    }
                }
            }
        });

        btnCancelaOpeCliente.setOnAction(e ->{
            if(exibirNotificacaoConfirmacao(btnCancelaOpeCliente.getScene().getWindow(),"Deseja cancelar operação de cadastro?", "Sim", "Não")){
                btnCancelaOpeCliente.setDisable(true);
                btnAlterarEndereco.setDisable(true);
                btnNovoEndereco.setDisable(true);
                btnConsultarCep.setDisable(true);
                btnSalvarEndereco.setDisable(true);
                btnCancelaOpeEndereco.setDisable(true);
                btnNovoCliente.setText("Novo cliente");
                btnNovoCliente.getStyleClass().remove(Styles.SUCCESS);
                btnNovoCliente.getStyleClass().add(Styles.ACCENT);
                btnNovoCliente.setMnemonicParsing(false);
                btnNovoCliente.setFocusTraversable(false);
                btnNovoCliente.setGraphic(new FontIcon(Feather.PLUS));
                limparEBloquearFormulario();
                limparFormEndereco();
                listaObservavelEnderecos.clear();

                estado = EstadoFormulario.NOVO;
            }
        });

        btnFechar.setOnAction(e -> {
            if(estado.isModeEdicaoNovo()){
                if(exibirNotificacaoConfirmacao(btnFechar.getScene().getWindow(),"Deseja realmente sair sem salvar as alterações do cliente?")){
                    btnNovoCliente.setText("Novo cliente");
                    btnNovoCliente.getStyleClass().remove(Styles.SUCCESS);
                    btnNovoCliente.getStyleClass().add(Styles.ACCENT);
                    btnNovoCliente.setMnemonicParsing(false);
                    btnNovoCliente.setFocusTraversable(false);
                    btnNovoCliente.setGraphic(new FontIcon(Feather.PLUS));
                    estado = EstadoFormulario.NOVO;
                    limparEBloquearFormulario();
                    limparFormEndereco();
                    listaObservavelEnderecos.clear();
                    onCloseCallback.run();
                }
            }else{
                limparEBloquearFormulario();
                limparFormEndereco();
                btnNovoEndereco.setDisable(true);
                listaObservavelEnderecos.clear();
                estado = EstadoFormulario.NOVO;
                onCloseCallback.run();
            }
        });

        return rootPane;
    }

    public Pane createAbaGeral() {

        // --- 1. CAMPOS DE TEXTO E SELEÇÃO ---

        // Nome completo
        Label labelNome = new Label("Nome");
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
        //DocumentoUtil.aplicarMascaraCpf(campoCPF);
        campoCPF.setDisable(true);

        Label labelRG = new Label("RG / Inscrição Estadual");
        campoRG.setPromptText("Informe seu RG ou IE");
        campoRG.setDisable(true);

        Label labelDataNascimento = new Label("Data Nascimento");
        campoDataNascimento.setPromptText("dd/mm/aaaa");
        DocumentoUtil.aplicarMarcaraData(campoDataNascimento);
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
        campoTipoPessoa.getSelectionModel().selectFirst();
        campoTipoPessoa.setDisable(true);

        Label labelEstadoCivil = new Label("Estado Civil");
        campoEstadoCivil.getItems().addAll(
                "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "União Estável", "Não informar");
        campoEstadoCivil.setPromptText("Selecione");
        campoEstadoCivil.setDisable(true);

        // Observações
        Label labelObservacoes = new Label("Observações");
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
        linhaDetalhes.add(labelTipoPessoa, 0, 0);
        linhaDetalhes.add(labelRG, 1, 0);
        linhaDetalhes.add(labelCPF, 2, 0);
        linhaDetalhes.add(labelDataNascimento, 3, 0);

        // Linha 1: Campos
        linhaDetalhes.add(campoTipoPessoa, 0, 1);
        linhaDetalhes.add(campoRG, 1, 1);
        linhaDetalhes.add(campoCPF, 2, 1);
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

        campoCPF.setMaxWidth(300);
        DocumentoUtil.configurarCampoDocumentoAdaptavel(campoCPF, tipoAtual);
        campoTipoPessoa.setOnAction(e -> {
            String selecionado = campoTipoPessoa.getValue();

            if(Objects.equals(selecionado, "Física") || Objects.equals(selecionado, "Produtor")){
                tipoAtual.set(DocumentoUtil.TipoDocumento.CPF);
                labelNome.setText("Nome");
                labelNomeFantasiaAndApelido.setText("Sobrenome");
                campoNomeCliente.setPromptText("Nome do cliente");
                campoNomeFantasiaAndApelido.setPromptText("Sobrenome");

                campoCPF.clear();
                labelCPF.setText("CPF");
                campoCPF.setPromptText("000.000.000-00");



            } else {
                tipoAtual.set(DocumentoUtil.TipoDocumento.CNPJ);
                labelNome.setText("Razão social");
                labelNomeFantasiaAndApelido.setText("Nome Fantasia");
                campoNomeCliente.setPromptText("Razão social");
                campoNomeFantasiaAndApelido.setPromptText("Nome Fantasia");

                campoCPF.clear();
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
        listaObservavelEnderecos = FXCollections.observableArrayList();

        btnNovoEndereco.setText("Novo endereço");
        btnNovoEndereco.setGraphic(new FontIcon(Feather.PLUS));
        btnNovoEndereco.getStyleClass().add(Styles.ACCENT);
        btnNovoEndereco.setMnemonicParsing(false);
        btnNovoEndereco.setFocusTraversable(false);
        btnNovoEndereco.setDisable(true);

        btnSalvarEndereco.setText("Salvar");
        btnSalvarEndereco.setGraphic(new FontIcon(Feather.SAVE));
        btnSalvarEndereco.getStyleClass().add(Styles.SUCCESS);
        btnSalvarEndereco.setMnemonicParsing(false);
        btnSalvarEndereco.setFocusTraversable(false);
        btnSalvarEndereco.setDisable(true);

        btnAlterarEndereco.setText("Alterar");
        btnAlterarEndereco.setGraphic(new FontIcon(Feather.EDIT));
        btnAlterarEndereco.getStyleClass().add(Styles.WARNING);
        btnAlterarEndereco.setMnemonicParsing(false);
        btnAlterarEndereco.setFocusTraversable(false);
        btnAlterarEndereco.setDisable(true);

        btnCancelaOpeEndereco.setText("Cancelar");
        btnCancelaOpeEndereco.setGraphic(new FontIcon(Feather.X));
        btnCancelaOpeEndereco.getStyleClass().add(Styles.DANGER);
        btnCancelaOpeEndereco.setMnemonicParsing(false);
        btnCancelaOpeEndereco.setFocusTraversable(false);
        btnCancelaOpeEndereco.setDisable(true);

        btnConsultarCep.setText("Consultar CEP");
        btnConsultarCep.setGraphic(new FontIcon(Feather.SEARCH));
        btnConsultarCep.getStyleClass().add(Styles.ACCENT);
        btnConsultarCep.setMnemonicParsing(false);
        btnConsultarCep.setFocusTraversable(false);
        btnConsultarCep.setDisable(true);

        TextField campoConsultaCep = new TextField();
        campoConsultaCep.setPromptText("Informe o CEP...");
        campoConsultaCep.setVisible(false);

        HBox botoesAcaoNovoEnderecoAndEditarEndereco = new HBox();
        botoesAcaoNovoEnderecoAndEditarEndereco.getChildren().addAll(btnNovoEndereco, btnSalvarEndereco, btnAlterarEndereco, btnCancelaOpeEndereco, btnConsultarCep, campoConsultaCep);
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


        Label labelEstado = new Label("Estado");

        ObservableList<Estado> listaEstados = FXCollections.observableArrayList(cacheService.getEstadosCache());
        autoCompleteListaEstados = new AutoCompleteTextField<>(
                listaEstados,
                estado -> estado.getSigla() + " - " + estado.getNome()
        );

        campoEstado = autoCompleteListaEstados.getTextField();
        campoEstado.setDisable(true);
        ObservableList<Municipio> listaMunicipios = FXCollections.observableArrayList(cacheService.getMunicipiosCache());

        autoCompleteListaMunicipios = new AutoCompleteTextField<>(
                listaMunicipios,
                municipio -> municipio.getNome() + " - " + municipio.getUf()
        );

        campoMunicipioEndereco = autoCompleteListaMunicipios.getTextField();
        campoMunicipioEndereco.setDisable(true);

        campoMunicipioEndereco.setRight(new FontIcon(Feather.CHEVRON_DOWN));
        campoEstado.setRight(new FontIcon(Feather.CHEVRON_DOWN));
        campoEstado.setPromptText("UF (Ex: SP, BA)");
        campoMunicipioEndereco.setPromptText("Escolha ou pesquise...");

        autoCompleteListaMunicipios.getTextField().textProperty().addListener((obs, oldText, newText) -> {
            Municipio municipioSelecionado = autoCompleteListaMunicipios.getSelectedItem();

            if (municipioSelecionado != null) {
                // Verifica se o município selecionado existe no cache
                boolean existeNoCache = cacheService.getMunicipiosCache().stream()
                        .anyMatch(m -> m.getNome().equalsIgnoreCase(municipioSelecionado.getNome()));

                if (existeNoCache) {
                    // Se existe no cache, seta silenciosamente o estado
                    autoCompleteListaEstados.setTextSilently(municipioSelecionado.getUf());
                }
            }
        });

        btnNovoEndereco.setOnAction(e->{
            System.out.println(autoCompleteListaMunicipios.getSelectedItem().getNome() + autoCompleteListaMunicipios.getSelectedItem().getId());
        });

        Label labelPais = new Label("País");
        campoPais.setPromptText("País");
        campoPais.setText("Brasil");
        campoPais.setDisable(true);

        // --- 2. LAYOUT EM GRADE ---
        GridPane gridEndereco = new GridPane();
        gridEndereco.setHgap(20);
        gridEndereco.setVgap(10);
        gridEndereco.setPadding(new Insets(10, 0, 0, 0));

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(30); // Nome, Rua, Bairro

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25); // Tipo, Número

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25); // CEP, Município

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20); // País, Complemento, Estado

        col0.setHgrow(Priority.SOMETIMES);
        col1.setHgrow(Priority.SOMETIMES);
        col2.setHgrow(Priority.SOMETIMES);
        col3.setHgrow(Priority.SOMETIMES);

        gridEndereco.getColumnConstraints().addAll(col0, col1, col2, col3);


        gridEndereco.add(labelNome, 0, 0);
        gridEndereco.add(labelTipoEndereco, 1, 0);
        gridEndereco.add(labelCEP, 2, 0);
        gridEndereco.add(labelPais, 3, 0);


        gridEndereco.add(campoNomeEndereco, 0, 1);
        gridEndereco.add(campoTipoEndereco, 1, 1);
        gridEndereco.add(campoCEP, 2, 1);
        gridEndereco.add(campoPais, 3, 1);


        gridEndereco.add(labelRua, 0, 2);
        gridEndereco.add(labelNumero, 1, 2);
        gridEndereco.add(labelComplemento, 2, 2);
        //gridEndereco.add(labelBairro, 3, 2);


        gridEndereco.add(campoRua, 0, 3);
        //GridPane.setColumnSpan(campoRua, 2); // Rua ocupa 2 colunas (mais espaço)
        gridEndereco.add(campoNumero, 1, 3);
        gridEndereco.add(campoComplemento, 2, 3);


        gridEndereco.add(labelBairro, 0, 4);
        gridEndereco.add(labelMunicipio, 1, 4);
        gridEndereco.add(labelEstado, 2, 4);


        gridEndereco.add(campoBairro, 0, 5);
        //GridPane.setColumnSpan(campoBairro, 2); // Bairro ocupa 2 colunas
        gridEndereco.add(campoMunicipioEndereco, 1, 5); // Município (ComboBox)
        gridEndereco.add(campoEstado, 2, 5); // Estado

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

        // ----------------------
        // Controle individual de tamanho
        // ----------------------

        // Campo Município (ComboBox) menor
        campoMunicipioEndereco.setMaxWidth(300);
        GridPane.setHgrow(campoMunicipioEndereco, Priority.NEVER);

        // Campo Estado também fixo (opcional)
        campoEstado.setMaxWidth(150);

        GridPane.setHgrow(campoEstado, Priority.NEVER);

        // Campo Número pode ser menor
        campoNumero.setPrefWidth(100);
        campoNumero.setMaxWidth(100);
        GridPane.setHgrow(campoNumero, Priority.NEVER);

        // Campos longos continuam responsivos
        GridPane.setHgrow(campoRua, Priority.ALWAYS);
        GridPane.setHgrow(campoNomeEndereco, Priority.ALWAYS);
        GridPane.setHgrow(campoBairro, Priority.ALWAYS);
        //nome, tipo_endereco, cep, municipio

        // ======== TABELA DE ENDEREÇOS ========

        TableColumn<Endereco, String> colNome = new TableColumn<Endereco, String>("Nome");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));

        TableColumn<Endereco, String> colTipoEndereco = new TableColumn<Endereco, String>("Tipo endereço");
        colTipoEndereco.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoEndereco()));

        TableColumn<Endereco, String> colCep = new TableColumn<Endereco, String>("Cep");
        colCep.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCep()));

        TableColumn<Endereco, String> colMunicipio = new TableColumn<Endereco, String>("Municipio");
        colMunicipio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMunicipio().getNome()));
        tabelaEnderecos = new TableView<Endereco>();
        tabelaEnderecos.setItems(listaObservavelEnderecos);
        tabelaEnderecos.getColumns().setAll(colNome, colTipoEndereco, colCep, colMunicipio);
        tabelaEnderecos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabelaEnderecos.setPrefHeight(350);

        // --- 3. LAYOUT PRINCIPAL ---
        VBox layoutEndereco = new VBox(20,botoesAcaoNovoEnderecoAndEditarEndereco, gridEndereco, tabelaEnderecos); // 20 de espaçamento
        layoutEndereco.setPadding(new Insets(20)); // 20 de padding

        // Para o caso de esta aba precisar crescer (como a de observações)
        VBox.setVgrow(gridEndereco, Priority.ALWAYS);

        //EVENTOS

        //Novo endereço
        btnNovoEndereco.setOnAction(e -> {
            btnSalvarEndereco.setDisable(false);
            btnCancelaOpeEndereco.setDisable(false);
            btnNovoEndereco.setDisable(true);
            btnAlterarEndereco.setDisable(true);
            tabelaEnderecos.setDisable(true);
            btnConsultarCep.setDisable(false);

            editandoEnderecoExistente = false;
            enderecoEmEdicao = null;

            limparFormEndereco();
            habilitarCamposEndereco(true);
            campoNomeEndereco.requestFocus();
        });

        //Salvar endereço
        btnSalvarEndereco.setOnAction(e ->{
            if(verificarCamposEndereco(listaMunicipios, listaEstados)){
                if(editandoEnderecoExistente){
                    preencherFormularioNovoEndereco(enderecoEmEdicao);
                    tabelaEnderecos.refresh();
                    exibirNotificacao(btnSalvarEndereco, "Endereço alterado com sucesso!", Styles.SUCCESS);
                }else{
                    Endereco novoEndereco = new Endereco();
                    preencherFormularioNovoEndereco(novoEndereco);
                    clienteAtual.addEndereco(novoEndereco);
                    listaObservavelEnderecos.add(novoEndereco);
                    exibirNotificacao(btnSalvarEndereco, "Endereço adicionado com sucesso!", Styles.SUCCESS);
                }
                limparFormEndereco();
                btnSalvarEndereco.setDisable(true);
                btnNovoEndereco.setDisable(false);
                btnCancelaOpeEndereco.setDisable(true);
                tabelaEnderecos.setDisable(false);
                btnConsultarCep.setDisable(true);
                habilitarCamposEndereco(false);
            }
        });

        //Cancelar operação de cadastro
        btnCancelaOpeEndereco.setOnAction(e ->{
            limparFormEndereco();
            btnCancelaOpeEndereco.setDisable(true);
            btnSalvarEndereco.setDisable(true);
            btnNovoEndereco.setDisable(false);
            btnConsultarCep.setDisable(true);
            tabelaEnderecos.setDisable(false);
            habilitarCamposEndereco(false);

            editandoEnderecoExistente = false;
            enderecoEmEdicao = null;
        });

        //Altera endereço
        btnAlterarEndereco.setOnAction(e -> {
            Endereco enderecoSelecionado = tabelaEnderecos.getSelectionModel().getSelectedItem();

            if (enderecoSelecionado == null) {
                exibirNotificacao(tabelaEnderecos, "Selecione um endereço para alterar!", Styles.BG_WARNING_EMPHASIS);
                return;
            }
            enderecoEmEdicao = enderecoSelecionado;
            editandoEnderecoExistente = true;

            // Preenche o formulário com os dados do endereço selecionado
            preencherFormEndereco(enderecoSelecionado);

            btnAlterarEndereco.setDisable(true);
            btnNovoEndereco.setDisable(true);
            btnSalvarEndereco.setDisable(false);
            btnConsultarCep.setDisable(true);
            btnCancelaOpeEndereco.setDisable(false);

            habilitarCamposEndereco(true);
            campoNomeEndereco.requestFocus();
        });

        //Consultar endereço por CEP
        btnConsultarCep.setOnAction(e->{
            Pair<Boolean, String> resultado = exibirNotificacaoInput(
                    btnNovoCliente.getScene().getWindow(),
                    "Digite o CEP do cliente:",
                    "Confirmar",
                    "Cancelar"
            );
            boolean confirmou = resultado.getKey();
            String cep = resultado.getValue();

            if (confirmou) {
                Task<EnderecoDTO> task = new Task() {
                    @Override
                    protected EnderecoDTO call() throws Exception {
                        try {
                            return cepService.buscarEnderecoPorCep(cep);
                        }catch (Exception e){
                            log.error("Erro ao buscar o CEP", e);
                            throw e;
                        }
                    }
                };

                task.setOnRunning(eventRunnig -> {
                    loading.show();
                });

                task.setOnSucceeded(eventSucces ->{
                    loading.hide();
                    EnderecoDTO endereco = task.getValue(); // pega o retorno do call()
                    preencherFormEndereco(endereco);        // <- agora é seguro atualizar a UI
                    exibirNotificacao(layoutEndereco, "CEP localizado com sucesso!", Styles.SUCCESS);
                });

                task.setOnFailed(eventFailed ->{
                    loading.hide();
                    exibirNotificacao(layoutEndereco, "CEP não localizado", Styles.DANGER);
                });

                new Thread(task).start();
                System.out.println("Usuário confirmou!");
            } else {
                System.out.println("Usuário cancelou.");
            }
        });

        //Selecionar endereço na lista
        tabelaEnderecos.setRowFactory(tv -> {
            TableRow<Endereco> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Endereco enderecoSelecionado = row.getItem();
                    preencherFormEndereco(enderecoSelecionado);
                    habilitarCamposEndereco(false);

                    if (!editandoEnderecoExistente) {
                        btnAlterarEndereco.setDisable(false);
                    }
                }
            });

            return row;
        });

        return layoutEndereco;
    }

    private Cliente getClienteFormulario(){
        if(clienteAtual == null){
            clienteAtual = new Cliente();
        }


        String tipoPessoaBanco = ConstantesUtil.getTipoPessoaBanco(campoTipoPessoa.getValue());
        String genero = ConstantesUtil.getGeneroBanco(campoGenero.getValue());
        String estadoCivil = ConstantesUtil.getEstadoCivilBanco(campoEstadoCivil.getValue());

        clienteAtual.setNome(campoNomeCliente.getText());
        clienteAtual.setSobrenome(campoNomeFantasiaAndApelido.getText());
        clienteAtual.setTelefone(campoTelefone.getText());
        clienteAtual.setCpfCnpj(campoCPF.getText());
        clienteAtual.setEmail(campoEmail.getText());

        clienteAtual.setDataNascimento(campoDataNascimento.getValue());
        clienteAtual.setObservacao(campoObservacoes.getText());

        if(Objects.equals(tipoPessoaBanco, "F") || Objects.equals(tipoPessoaBanco, "P")){
            clienteAtual.setRegistro(campoRG.getText());
        }else{
            clienteAtual.setInscricaoEst(campoRG.getText());
        }

        clienteAtual.setTipoPessoa(tipoPessoaBanco);
        clienteAtual.setGenero(genero);
        clienteAtual.setEstadoCivil(estadoCivil);

        System.out.println("CLIENTE ATUAL LINHA DE BAIXO");
        System.out.println(clienteAtual);
        return clienteAtual;
    }

    public void getEnderecoFormulario(Endereco endereco){
        campoNomeEndereco.setText(endereco.getNome());
        campoTipoEndereco.setValue(endereco.getTipoEndereco());
        campoCEP.setText(endereco.getCep());
        campoRua.setText(endereco.getRua());
        campoNumero.setText(endereco.getNumero());
        campoComplemento.setText(endereco.getComplemento());
        campoBairro.setText(endereco.getBairro());
        autoCompleteListaEstados.setTextSilently(endereco.getEstado());

        Municipio municipioSelecionado = autoCompleteListaMunicipios.getSelectedItem();
        if (municipioSelecionado != null) {
            endereco.setMunicipio(municipioSelecionado);
        }

        campoPais.setText(endereco.getPais());
    }

    public void preencherFormEndereco(Endereco endereco) {
        campoNomeEndereco.setText(endereco.getNome());
        campoTipoEndereco.setValue(endereco.getTipoEndereco());
        campoCEP.setText(endereco.getCep());
        campoRua.setText(endereco.getRua());
        campoNumero.setText(endereco.getNumero());
        campoComplemento.setText(endereco.getComplemento());
        campoBairro.setText(endereco.getBairro());

        autoCompleteListaEstados.setTextSilently(endereco.getEstado());
        autoCompleteListaMunicipios.setTextSilently(endereco.getMunicipio().getNome() + " - " + endereco.getMunicipio().getUf());
        campoPais.setText(endereco.getPais());
    }

    public void preencherFormEndereco(EnderecoDTO endereco) {
        if(endereco == null){
            return;
        }
        campoCEP.setText(endereco.getCep());
        campoRua.setText(endereco.getLogradouro());
        campoBairro.setText(endereco.getBairro());

        autoCompleteListaEstados.setTextSilently(endereco.getUf());
        autoCompleteListaMunicipios.setTextSilently(endereco.getLocalidade() + " - " + endereco.getUf());
    }

    public void preencherFormularioNovoEndereco(Endereco endereco) {
        endereco.setNome(campoNomeEndereco.getText());
        endereco.setTipoEndereco(campoTipoEndereco.getValue());
        endereco.setCep(campoCEP.getText());
        endereco.setRua(campoRua.getText());
        endereco.setNumero(campoNumero.getText());
        endereco.setComplemento(campoComplemento.getText());
        endereco.setBairro(campoBairro.getText());
        endereco.setPais(campoPais.getText());

        // Pega o estado do AutoCompleteTextField
        endereco.setEstado(autoCompleteListaEstados.getTextField().getText());

        // Pega o objeto Municipio selecionado
        Municipio municipioSelecionado = autoCompleteListaMunicipios.getSelectedItem();
        if (municipioSelecionado != null) {
            endereco.setMunicipio(municipioSelecionado);
        }
    }

    public void limparEBloquearFormulario() {
        clienteAtual = null; // Limpa o cliente em memória
        campoCodCliente.setText("");
        campoNomeCliente.setText("");
        campoNomeFantasiaAndApelido.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoCPF.setText("");
        campoCNPJ.setText("");
        campoRG.setText("");
        campoDataNascimento.setValue(null);
        campoTipoPessoa.getSelectionModel().selectFirst();
        campoGenero.setPromptText("Selecione");
        campoEstadoCivil.setPromptText("Selecione");
        campoGenero.getSelectionModel().select("Não informar");
        campoEstadoCivil.getSelectionModel().select("Não informar");
        campoObservacoes.setText("");

        habilitarCamposCliente(false);
    }

    public void limparFormEndereco() {
        tabelaEnderecos.getSelectionModel().clearSelection();
        campoNomeEndereco.clear();
        campoTipoEndereco.getSelectionModel().selectFirst();
        campoCEP.clear();
        campoRua.clear();
        campoNumero.clear();
        campoComplemento.clear();
        campoBairro.clear();
        autoCompleteListaMunicipios.setSelectedItemSilently(null);
        autoCompleteListaEstados.setSelectedItemSilently(null);
        campoPais.setText("Brasil");

        editandoEnderecoExistente = false;
        enderecoEmEdicao = null;
    }

    public void prepararNovoCliente() {
        limparEBloquearFormulario();

        clienteAtual = new Cliente();

        habilitarCamposCliente(true);
        //habilitarCamposEndereco(true);
        listaObservavelEnderecos.clear();

        campoNomeCliente.requestFocus();
    }

    private void habilitarCamposCliente(boolean habilitar) {
        campoNomeCliente.setDisable(!habilitar);
        campoNomeFantasiaAndApelido.setDisable(!habilitar);
        campoTelefone.setDisable(!habilitar);
        campoEmail.setDisable(!habilitar);
        campoCPF.setDisable(!habilitar);
        campoCNPJ.setDisable(!habilitar);
        campoRG.setDisable(!habilitar);
        campoDataNascimento.setDisable(!habilitar);
        campoEstadoCivil.setDisable(!habilitar);
        campoGenero.setDisable(!habilitar);
        campoTipoPessoa.setDisable(!habilitar);
        campoObservacoes.setDisable(!habilitar);
    }

    private void habilitarCamposEndereco(boolean habilitar) {
        campoNomeEndereco.setDisable(!habilitar);
        campoTipoEndereco.setDisable(!habilitar);
        campoCEP.setDisable(!habilitar);
        campoRua.setDisable(!habilitar);
        campoNumero.setDisable(!habilitar);
        campoComplemento.setDisable(!habilitar);
        campoBairro.setDisable(!habilitar);
        campoMunicipioEndereco.setDisable(!habilitar);
        campoEstado.setDisable(!habilitar);
        campoPais.setDisable(!habilitar);
        // Habilita também o botão 'Salvar' da aba endereço, se houver campos
        // (Assumindo que 'btnSalvar' também foi movido para um campo da classe)
        // btnSalvar.setDisable(!habilitar);
    }

    public boolean verificarCamposObrigatorios(){
        // 3. Valida o Nome
        if (campoNomeCliente.getText() == null || campoNomeCliente.getText().trim().isEmpty()) {
            String nomeCampo = "Física".equals(campoTipoPessoa.getValue()) ? "Nome" : "Razão Social";
            return exibirNotificacao(campoNomeCliente, "O campo '" + nomeCampo + "' é obrigatório.");
        }

        // 2. Valida o Tipo de Pessoa (essencial para o CPF/CNPJ)
        if (campoTipoPessoa.getValue() == null || campoTipoPessoa.getValue().isEmpty()) {
            return exibirNotificacao(campoTipoPessoa, "O campo 'Tipo de Pessoa' é obrigatório.");
        }

        // 4. Validação condicional de Documento
        String tipoPessoa = campoTipoPessoa.getValue();

        if ("Física".equals(tipoPessoa)) {
            if (campoCPF.getText() == null || campoCPF.getText().trim().isEmpty()) {
                return exibirNotificacao(campoCPF, "O campo 'CPF' é obrigatório para Pessoa Física.");
            }

             if (!DocumentoUtil.validarCPF(campoCPF.getText())) {
                return exibirNotificacao(campoCPF, "O CPF informado é inválido.");
             }

        } else if ("Jurídica".equals(tipoPessoa)) {
            if (campoCPF.getText() == null || campoCPF.getText().trim().isEmpty()) {
                return exibirNotificacao(campoCPF, "O campo 'CNPJ' é obrigatório para Pessoa Jurídica.");
            }

             if (!DocumentoUtil.validarCNPJ(campoCPF.getText())) {
                return exibirNotificacao(campoCPF, "O CNPJ informado é inválido.");
             }
        }

        return true;
    }

    public boolean verificarCamposEndereco(ObservableList<Municipio> listaMunicipios, ObservableList<Estado> listaEstados){
        if (campoNomeEndereco.getText() == null || campoNomeEndereco.getText().trim().isEmpty()) {
            return exibirNotificacao(campoNomeEndereco, "O campo 'Nome do Endereço' é obrigatório (Ex: Casa, Trabalho).");
        }
        if (campoTipoEndereco.getValue() == null || campoTipoEndereco.getValue().isEmpty()) {
            return exibirNotificacao(campoTipoEndereco, "O campo 'Tipo de Endereço' é obrigatório.");
        }
        if (campoCEP.getText() == null || campoCEP.getText().trim().isEmpty()) {
            return exibirNotificacao(campoCEP, "O campo 'CEP' é obrigatório.");
        }


        if (campoMunicipioEndereco.getText() == null || campoMunicipioEndereco.getText().trim().isEmpty()) {
            return exibirNotificacao(campoMunicipioEndereco, "O campo 'Município' é obrigatório.");
        }

        String nomeMunicipioDigitado = campoMunicipioEndereco.getText().trim();
        String nomeEstadoDigitado = campoEstado.getText().trim();

        boolean municipioValido = listaMunicipios.stream()
                .anyMatch(municipio -> {

                    String nomeFormatado = municipio.getNome() + " - " + municipio.getUf();

                    return nomeFormatado.equalsIgnoreCase(nomeMunicipioDigitado);
                });

        if (!municipioValido) {
            return exibirNotificacao(campoMunicipioEndereco, "Município inválido. Selecione um valor válido.");
        }

        boolean estadoValido = listaEstados.stream()
                .anyMatch(estado -> estado.getSigla().equalsIgnoreCase(nomeEstadoDigitado));

        if (!estadoValido) {
            return exibirNotificacao(campoMunicipioEndereco, "Estado inválido. Selecione um válido, ou apenas informe o municipio.");
        }

        return true;
    }

    private void carregarClienteSelecionado(Long idCliente) {
        limparEBloquearFormulario();
        limparFormEndereco();

        if (idCliente == null) {
            // (Opcional: mostrar uma notificação de "Selecione um cliente")
            return;
        }

        Optional<Cliente> clienteOptional = clienteRepository.findByIdWithEnderecos(idCliente);
        if (clienteOptional.isPresent()) {

            clienteAtual = clienteOptional.get();

            habilitarCamposCliente(true);

            campoCodCliente.setText(clienteAtual.getId().toString());
            campoNomeCliente.setText(clienteAtual.getNome());
            campoNomeFantasiaAndApelido.setText(clienteAtual.getSobrenome());
            campoTelefone.setText(clienteAtual.getTelefone());
            campoEmail.setText(clienteAtual.getEmail());

            if(Objects.equals(clienteAtual.getTipoPessoa(), "F") || Objects.equals(clienteAtual.getTipoPessoa(), "P")){
                DocumentoUtil.removerMascara(campoCPF);
                DocumentoUtil.aplicarMascaraCpf(campoCPF);
                campoCPF.setText(clienteAtual.getCpfCnpj());
            }else{
                DocumentoUtil.removerMascara(campoCPF);
                DocumentoUtil.aplicarMascaraCnpj(campoCPF);
                campoCPF.setText(clienteAtual.getCpfCnpj());
            }

            //campoCNPJ.setText(clienteAtual.getCpfCnpj());
            campoRG.setText(clienteAtual.getRegistro());
            campoDataNascimento.setValue(clienteAtual.getDataNascimento());
            campoEstadoCivil.setValue(ConstantesUtil.getEstadoCivilTela(clienteAtual.getEstadoCivil()));
            campoGenero.setValue(ConstantesUtil.getGeneroTela(clienteAtual.getGenero()));
            campoTipoPessoa.setValue(ConstantesUtil.getTipoPessoaTela(clienteAtual.getTipoPessoa()));

            System.out.println("CLIENTE INFO: " + clienteAtual);
            System.out.println("INFO: " + campoEstadoCivil.getValue());
            System.out.println("INFO: " + campoGenero.getValue());
            System.out.println("INFO: " + campoTipoPessoa.getValue());

            limparFormEndereco();

            if(clienteAtual.getEnderecos() != null && !clienteAtual.getEnderecos().isEmpty()){
                Endereco primeiroEndereco = clienteAtual.getEnderecos().getFirst();
                //habilitarCamposEndereco(true);

                preencherFormEndereco(primeiroEndereco);
            }else {
                habilitarCamposEndereco(false);
            }

            listaObservavelEnderecos = FXCollections.observableArrayList(
                    clienteAtual.getEnderecos()
            );

            tabelaEnderecos.setItems(listaObservavelEnderecos);
            btnNovoCliente.setText("Salvar alterações");
            btnNovoCliente.getStyleClass().remove(Styles.ACCENT);
            btnNovoCliente.getStyleClass().add(Styles.SUCCESS);

            btnNovoCliente.setMnemonicParsing(false);
            btnNovoCliente.setFocusTraversable(false);
            estado = EstadoFormulario.EDITANDO_EXISTENTE;
            System.out.println("Estado após buscar cliente: " + estado);
            btnNovoEndereco.setDisable(false);
            btnNovoEndereco.setDisable(false);
            btnCancelaOpeCliente.setDisable(false);
        }

    }

    private boolean isEnderecoValido(EnderecoDTO endereco) {
        return endereco != null &&
                endereco.getCep() != null &&
                !endereco.getCep().trim().isEmpty();
    }
}