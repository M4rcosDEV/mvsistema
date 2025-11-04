package com.sistema.mvsistema.view;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.sistema.mvsistema.dto.ClienteBusca;
import com.sistema.mvsistema.dto.EnderecoDto;
import com.sistema.mvsistema.model.Cliente;
import com.sistema.mvsistema.model.Endereco;
import com.sistema.mvsistema.model.Estado;
import com.sistema.mvsistema.model.Municipio;
import com.sistema.mvsistema.repository.ClienteRepository;
import com.sistema.mvsistema.service.CacheService;
import com.sistema.mvsistema.service.ClienteService;
import com.sistema.mvsistema.util.AutoCompleteTextField;
import com.sistema.mvsistema.util.DocumentoUtil;
import com.sistema.mvsistema.util.Utils;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static com.sistema.mvsistema.util.NotificationUtil.exibirErro;

@Component
public class ClienteView {

    private Cliente clienteAtual;

    //Botoes
    private Button btnNovoCliente;
    private Button btnNovoEndereco = new Button();
    private Button btnSalvarEndereco = new Button();


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

    // ======== CAMPOS DA JANELA DE BUSCA DE CLIENTE ========

    // Filtros de busca
    private TextField campoBuscaNome = new TextField();
    private TextField campoBuscaCpfCnpj = new TextField();
    private ComboBox<String> campoBuscaTipoPessoa = new ComboBox<>();
    private TextField campoBuscaTelefone = new TextField();
    private ComboBox<String> campoBuscaClassificacao = new ComboBox<>();
    private TextField campoBuscaMunicipio = new TextField();

    private AutoCompleteTextField<Municipio> autoCompleteListaMunicipios;

    private AutoCompleteTextField<Estado> autoCompleteListaEstados;

    private Pane layoutAbaEndereco;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CacheService cacheService;

    // Por padrão, é uma ação que não faz nada, para evitar NullPointer
    private Consumer<Cliente> onClienteSelecionadoCallback = (cliente) -> {};
    /**
     * Define uma ação a ser executada quando um cliente for
     * selecionado e carregado na tela.
     */
    public void setOnClienteSelecionado(Consumer<Cliente> callback) {
        // Se o callback for nulo, redefinimos para a ação vazia
        this.onClienteSelecionadoCallback = (callback != null) ? callback : (c) -> {};
    }

    private Runnable onCloseCallback = () -> {};
    public void setOnCloseRequest(Runnable callback) {
        this.onCloseCallback = (callback != null) ? callback : () -> {};
    }

    public Pane createCadastroClientePane(BorderPane layout) {

        btnNovoCliente = new Button(
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
        btnBuscar.setOnAction(e -> abrirJanelaBuscarCliente());

        Button btnFechar = new Button(
                "Fechar", new FontIcon (Feather.X)
        );
        btnFechar.getStyleClass().add(Styles.DANGER);
        btnFechar.setMnemonicParsing(false);
        btnFechar.setFocusTraversable(false);

        btnFechar.setOnAction(e -> {
            onCloseCallback.run();
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

        //EVENTOS
        btnNovoCliente.setOnAction(e -> {
            System.out.println("Executou");

            // ESTADO 2: O usuário está clicando em "SALVAR"
            if (btnNovoCliente.getText().equals("Salvar cliente")) {
                System.out.println("Verificando validações...");
                if (verificarCamposObrigatorios()) { // Removi o endereço por enquanto
                    System.out.println("Entrou no IF (Salvando)");

                    // Sucesso na validação
                    // limparEBloquearFormulario();
                    // limparFormEndereco();
                    if(btnNovoEndereco.isDisable()){
                        System.out.println("Endereço em edicao");
                        exibirErro(btnSalvarEndereco, "Endereço em edição, por favor finalize o cadastro do endereço");
                    }else{
                        Cliente clienteNovo =  getClienteFormulario();
                        System.out.println("Cliente INFO" + clienteNovo.toString());
                        clienteRepository.save(clienteNovo); // Você precisa montar o clienteAtual aqui


                        // Reseta o botão para o estado inicial
                        btnNovoCliente.setText("Novo cliente");
                        btnNovoCliente.setGraphic(new FontIcon(Feather.PLUS));
                        btnNovoCliente.getStyleClass().remove(Styles.SUCCESS);
                    }


                    // TODO: Exibir notificação de SUCESSO
                } else {
                    System.out.println("Validação falhou.");
                    // Não faz nada, o método exibirErro() dentro da validação já mostrou o toast.
                }

            } else {
                btnNovoEndereco.setDisable(false);

                // ESTADO 1: O usuário está clicando em "NOVO CLIENTE"
                System.out.println("Entrou no ELSE (Preparando para novo)");

                // Define o texto para o PRÓXIMO estado
                btnNovoCliente.setText("Salvar cliente");
                btnNovoCliente.setGraphic(new FontIcon(Feather.CHECK));
                btnNovoCliente.getStyleClass().add(Styles.SUCCESS);

                // Apenas prepara o formulário para um novo cliente
                 prepararNovoCliente(); // Descomente seus métodos de preparação
            }
        });

        // --- 6. LAYOUT PRINCIPAL ---
        VBox layoutPrincipal = new VBox(15, botoesAcaoNovoClienteAndBuscaCliente, blocoBrancoEstilizado);

        layoutPrincipal.getStyleClass().add("layout-principal-cad-cliente");

        layoutPrincipal.setPadding(new Insets(20));

        //Garante que o bloco branco ocupe o restante da altura do layout principal.
        VBox.setVgrow(blocoBrancoEstilizado, Priority.ALWAYS);
        return layoutPrincipal;
    }

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
        listaObservavelEnderecos = FXCollections.observableArrayList();

//        btnNovoEndereco = new Button(
//                "Novo endereço", new FontIcon (Feather.PLUS)
//        );
        btnNovoEndereco.setText("Novo endereço");
        btnNovoEndereco.setGraphic(new FontIcon(Feather.PLUS));
        btnNovoEndereco.getStyleClass().add(Styles.ACCENT);
        btnNovoEndereco.setMnemonicParsing(false);
        btnNovoEndereco.setFocusTraversable(false);
        btnNovoEndereco.setDisable(true);

        btnSalvarEndereco.setText("Salvar");
        btnSalvarEndereco.setGraphic(new FontIcon(Feather.SAVE));
        btnSalvarEndereco.getStyleClass().add(Styles.ACCENT);
        btnSalvarEndereco.setMnemonicParsing(false);
        btnSalvarEndereco.setFocusTraversable(false);
        btnSalvarEndereco.setDisable(true);

        HBox botoesAcaoNovoEnderecoAndEditarEndereco = new HBox();
        botoesAcaoNovoEnderecoAndEditarEndereco.getChildren().addAll(btnNovoEndereco, btnSalvarEndereco);
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
        tabelaEnderecos.getColumns().setAll(colNome, colTipoEndereco, colCep, colMunicipio);
        tabelaEnderecos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabelaEnderecos.setPrefHeight(350);

        // --- 3. LAYOUT PRINCIPAL ---
        VBox layoutEndereco = new VBox(20,botoesAcaoNovoEnderecoAndEditarEndereco, gridEndereco, tabelaEnderecos); // 20 de espaçamento
        layoutEndereco.setPadding(new Insets(20)); // 20 de padding

        // Para o caso de esta aba precisar crescer (como a de observações)
        VBox.setVgrow(gridEndereco, Priority.ALWAYS);

        //EVENTOS


//        if (btnNovoCliente.getText().equals("Salvar cliente")) {
//            System.out.println("Verificando validações...");
//            if (verificarCamposEndereco()) {
//                System.out.println("Entrou no IF (Salvando)");
//            }
//        }else {
//            btnNovoEndereco.setDisable(false);
//
//            limparFormEndereco();
//
//            btnNovoEndereco.setOnAction(e -> {
//                btnSalvarEndereco.setDisable(false);
//                btnNovoEndereco.setDisable(true);
//
//                Endereco endereco = new Endereco();
//                preencherFormEndereco(endereco);
//                if(verificarCamposEndereco()){
//                    clienteAtual.addEndereco(endereco);
//                    listaObservavelEnderecos.add(endereco);
//                }
//            });
//
//            limparFormEndereco();
//        }

        btnNovoEndereco.setOnAction(e -> {
            btnSalvarEndereco.setDisable(false);
            btnNovoEndereco.setDisable(true);

            limparFormEndereco();
            habilitarCamposEndereco(true);
            campoNomeEndereco.requestFocus();



        });

        btnSalvarEndereco.setOnAction(e ->{
            Endereco endereco = new Endereco();

            preencherFormularioNovoEndereco(endereco);

            if(verificarCamposEndereco()){
                clienteAtual.addEndereco(endereco);
                listaObservavelEnderecos.add(endereco);
                tabelaEnderecos.setItems(listaObservavelEnderecos);
                limparFormEndereco();

                btnSalvarEndereco.setDisable(true);
                btnNovoEndereco.setDisable(false);

                habilitarCamposEndereco(false);
            }
        });

        executarEventos();
        
        this.layoutAbaEndereco = layoutEndereco;
        return layoutEndereco;
    }

    public void abrirJanelaBuscarCliente() {
        Stage janelaBuscarClienteStage  = new Stage();
        //janelaBuscarClienteStage.setResizable(false);
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

        //Chamei a função só pra garantir que todos os campos fiquem limpos no inicio
        limparEBloquearFormulario();

        //EVENTOS
        btnSelecionar.setOnAction(e -> {
            ClienteBusca clienteSelecionado = tabela.getSelectionModel().getSelectedItem();
            Optional<Cliente> clienteOptional = clienteRepository.findByIdWithEnderecos(clienteSelecionado.getId());
            if (clienteOptional.isPresent()) {
                janelaBuscarClienteStage.close();

                clienteAtual = clienteOptional.get();

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
                campoObservacoes.setDisable(false);

                campoNomeCliente.setText(clienteAtual.getNome());
                campoNomeFantasiaAndApelido.setText(clienteAtual.getSobrenome());
                campoTelefone.setText(clienteAtual.getTelefone());
                campoEmail.setText(clienteAtual.getEmail());
                campoCPF.setText(clienteAtual.getCpfCnpj());
                campoCNPJ.setText(clienteAtual.getCpfCnpj());
                campoRG.setText(clienteAtual.getRegistro());
                campoDataNascimento.setValue(clienteAtual.getDataNascimento());
                campoEstadoCivil.setValue(clienteAtual.getEstadoCivil());
                campoGenero.setValue(clienteAtual.getGenero());
                campoTipoPessoa.setValue(Utils.converterTipoPessoaCharParaString(clienteAtual.getTipoPessoa()));

                limparFormEndereco();

                if(clienteAtual.getEnderecos() != null && !clienteAtual.getEnderecos().isEmpty()){
                    Endereco primeiroEndereco = clienteAtual.getEnderecos().getFirst();
                    habilitarCamposEndereco(true);

                    preencherFormEndereco(primeiroEndereco);
                }else {
                    habilitarCamposEndereco(false);
                }

                listaObservavelEnderecos = FXCollections.observableArrayList(
                        clienteAtual.getEnderecos()
                );

                tabelaEnderecos.setItems(listaObservavelEnderecos);
                onClienteSelecionadoCallback.accept(clienteAtual);
            }
        });

        Scene cena = new Scene(layout, 900, 730);
        janelaBuscarClienteStage.setScene(cena);
        janelaBuscarClienteStage.showAndWait();
    }

    private Cliente getClienteFormulario(){
        if(clienteAtual == null){
            clienteAtual = new Cliente();
        }

        clienteAtual.setNome(campoNomeCliente.getText());
        clienteAtual.setSobrenome(campoNomeFantasiaAndApelido.getText());
        clienteAtual.setTelefone(campoTelefone.getText());
        clienteAtual.setEmail(campoEmail.getText());

        String tipoPessoa = campoTipoPessoa.getValue();
        if ("Jurídico".equals(tipoPessoa) || "Instituição".equals(tipoPessoa)) {
            clienteAtual.setCpfCnpj(campoCNPJ.getText());
        } else if ("Física".equals(tipoPessoa) || "Produtor".equals(tipoPessoa)) {
            clienteAtual.setCpfCnpj(campoCPF.getText());
        } else {
            clienteAtual.setCpfCnpj(null);
        }

        clienteAtual.setRegistro(campoRG.getText());
        clienteAtual.setDataNascimento(campoDataNascimento.getValue());
        clienteAtual.setEstadoCivil(campoEstadoCivil.getValue());
        clienteAtual.setGenero(campoGenero.getValue());
        clienteAtual.setTipoPessoa(Utils.converterTipoPessoaStringParaChar(campoTipoPessoa.getValue()));


        return clienteAtual;
    }

    public void limparEBloquearFormulario() {
        clienteAtual = null; // Limpa o cliente em memória

        campoNomeCliente.setText("");
        campoNomeFantasiaAndApelido.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoCPF.setText("");
        campoCNPJ.setText("");
        campoRG.setText("");
        campoDataNascimento.setValue(null);
        campoEstadoCivil.setValue(null);
        campoTipoPessoa.setValue(null);
        campoGenero.setValue(null);
        campoObservacoes.setText("");

        campoNomeCliente.setDisable(true);
        campoNomeFantasiaAndApelido.setDisable(true);
        campoTelefone.setDisable(true);
        campoEmail.setDisable(true);
        campoCPF.setDisable(true);
        campoCNPJ.setDisable(true);
        campoRG.setDisable(true);
        campoDataNascimento.setDisable(true);
        campoEstadoCivil.setDisable(true);
        campoGenero.setDisable(true);
        campoTipoPessoa.setDisable(true);
        campoObservacoes.setDisable(true);
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
        autoCompleteListaMunicipios.setTextSilently(endereco.getMunicipio().getNome());
        campoPais.setText(endereco.getPais());
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

    public void limparFormEndereco() {
        tabelaEnderecos.getSelectionModel().clearSelection();
        campoNomeEndereco.clear();
        campoTipoEndereco.setValue(null);
        campoCEP.clear();
        campoRua.clear();
        campoNumero.clear();
        campoComplemento.clear();
        campoBairro.clear();
        autoCompleteListaMunicipios.setSelectedItemSilently(null);
        autoCompleteListaEstados.setSelectedItemSilently(null);
        campoPais.setText("Brasil");
    }

    /**
     * Metodo que habilita todos os campos do formulario de cliente para novo cadastro
     */
    public void prepararNovoCliente() {
        limparEBloquearFormulario();

        clienteAtual = new Cliente();

        habilitarCamposCliente(true);
        //habilitarCamposEndereco(true);
        listaObservavelEnderecos.clear();

        campoNomeCliente.requestFocus();
    }

    /**
     * Habilita ou desabilita os campos do formulário de cliente.
     * @param habilitar true para habilitar, false para desabilitar.
     */
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

    /**
     * Habilita ou desabilita os campos do formulário de endereço.
     * @param habilitar true para habilitar, false para desabilitar.
     */
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

    public void executarEventos(){
        tabelaEnderecos.setRowFactory(tv -> {
            TableRow<Endereco> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Endereco enderecoSelecionado = row.getItem();
                    preencherFormEndereco(enderecoSelecionado);
                }
            });

            return row;
        });
    }

    public boolean verificarCamposObrigatorios(){
        // 3. Valida o Nome
        if (campoNomeCliente.getText() == null || campoNomeCliente.getText().trim().isEmpty()) {
            String nomeCampo = "Física".equals(campoTipoPessoa.getValue()) ? "Nome" : "Razão Social";
            return exibirErro(campoNomeCliente, "O campo '" + nomeCampo + "' é obrigatório.");
        }

        // 2. Valida o Tipo de Pessoa (essencial para o CPF/CNPJ)
        if (campoTipoPessoa.getValue() == null || campoTipoPessoa.getValue().isEmpty()) {
            return exibirErro(campoTipoPessoa, "O campo 'Tipo de Pessoa' é obrigatório.");
        }

        // 4. Validação condicional de Documento
        String tipoPessoa = campoTipoPessoa.getValue();

        if ("Física".equals(tipoPessoa)) {
            if (campoCPF.getText() == null || campoCPF.getText().trim().isEmpty()) {
                return exibirErro(campoCPF, "O campo 'CPF' é obrigatório para Pessoa Física.");
            }
            // Opcional: Adicionar validação de lógica (DocumentoUtil.validarCPF())
             if (!DocumentoUtil.validarCPF(campoCPF.getText())) {
                return exibirErro(campoCPF, "O CPF informado é inválido.");
             }

        } else if ("Jurídica".equals(tipoPessoa)) {
            if (campoCNPJ.getText() == null || campoCNPJ.getText().trim().isEmpty()) {
                return exibirErro(campoCNPJ, "O campo 'CNPJ' é obrigatório para Pessoa Jurídica.");
            }
            // Opcional: Adicionar validação de lógica (DocumentoUtil.validarCNPJ())
             if (!DocumentoUtil.validarCNPJ(campoCNPJ.getText())) {
                return exibirErro(campoCNPJ, "O CNPJ informado é inválido.");
             }
        }

        return true;
    }

    public boolean verificarCamposEndereco(){
        if (campoNomeEndereco.getText() == null || campoNomeEndereco.getText().trim().isEmpty()) {
            return exibirErro(campoNomeEndereco, "O campo 'Nome do Endereço' é obrigatório (Ex: Casa, Trabalho).");
        }
        if (campoTipoEndereco.getValue() == null || campoTipoEndereco.getValue().isEmpty()) {
            return exibirErro(campoTipoEndereco, "O campo 'Tipo de Endereço' é obrigatório.");
        }
        if (campoCEP.getText() == null || campoCEP.getText().trim().isEmpty()) {
            return exibirErro(campoCEP, "O campo 'CEP' é obrigatório.");
        }

        if (campoMunicipioEndereco.getText() == null || campoMunicipioEndereco.getText().trim().isEmpty()) {
            return exibirErro(campoMunicipioEndereco, "O campo 'Município' é obrigatório.");
        }
        if (campoEstado.getText() == null || campoEstado.getText().trim().isEmpty()) {
            return exibirErro(campoEstado, "O campo 'Estado (UF)' é obrigatório.");
        }

        return true;
    }

}