package com.sistema.mvsistema.view.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import com.sistema.mvsistema.dto.ClienteBusca;
import com.sistema.mvsistema.entity.Grade;
import com.sistema.mvsistema.repository.GradeRepository;
import com.sistema.mvsistema.service.ClienteService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@ComponentScan
public class JanelaGeracaoVariacaoProduto {

    @Autowired
    ClienteService clienteService;


    ListView<String> listViewGrades = new ListView<String>();
    ListView<String> listViewItensGrades = new ListView<String>();
    ListView<String> listViewResultados = new ListView<String>();

    public void mostrarJanela(){
        Stage stage = new Stage();

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        VBox painelGrades = criarPainelGrades();
        VBox painelBotoes = botoesGrade();
        VBox painelItens = criarPainelItensGrade();
        VBox painelResultado = criarPainelResultadoVariacoes();

        HBox.setHgrow(painelGrades, Priority.ALWAYS);
        HBox.setHgrow(painelItens, Priority.ALWAYS);
        HBox.setHgrow(painelResultado, Priority.ALWAYS);

        HBox concatenarListViews = new HBox(20, painelGrades, painelBotoes, painelItens, painelResultado);
        HBox.setHgrow(concatenarListViews, Priority.ALWAYS);

        layout.setCenter(concatenarListViews);
        layout.setBottom(criarRodape());

        Scene scene = new Scene(layout, 1000, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/static/css/tela-geracao-variacao-produtos.css")).toExternalForm()
        );
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/imagens/logo.png"))));

        stage.setTitle("Gerador de Variações");
        stage.show();
    }

    public VBox criarPainelGrades(){
        //Resposta do backend
        ObservableList<String> grades = FXCollections.observableArrayList("TAMANHO", "MODELO", "COR");
        //ObservableList<Grade> grades = FXCollections.observableArrayList(new Grade(1, "TAMANHO", true));

        listViewGrades.setItems(grades);
        listViewGrades.getStyleClass().add("list-view-grade");
        listViewGrades.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        VBox vBox = new VBox(listViewGrades);
        VBox.setVgrow(listViewGrades, Priority.ALWAYS);
        vBox.getStyleClass().add("vbox-list-view-grade");
        vBox.setFillWidth(true);
        vBox.setPrefHeight(Double.MAX_VALUE);

        return vBox;
    }

    public VBox criarPainelItensGrade(){
        //Resposta do backend
        ObservableList<String> itensGrade = FXCollections.observableArrayList("AZUL", "BRANCO", "PRETO", "P", "M", "G");

        Map<String, BooleanProperty> selecionados = new HashMap<>();

        listViewItensGrades.setItems(itensGrade);

        Callback<String, ObservableValue<Boolean>> func = item -> {
            BooleanProperty prop = selecionados.computeIfAbsent(item, i -> new SimpleBooleanProperty(false));

            prop.addListener((obs, oldVal, newVal) -> {
                System.out.println("Item " + item + " agora está " + (newVal ? "selecionado" : "desmarcado"));
            });

            return prop;
        };

        listViewItensGrades.setCellFactory(CheckBoxListCell.forListView(func));

        VBox vBox = new VBox(listViewItensGrades);
        VBox.setVgrow(listViewItensGrades, Priority.ALWAYS);

        vBox.setFillWidth(true);
        vBox.setPrefHeight(Double.MAX_VALUE);

        return vBox;
    }

    public VBox criarPainelResultadoVariacoes(){
        //Resposta do backend
        ObservableList<String> resultadosVariacao= FXCollections.observableArrayList("AZUL - P", "AZUL - M","AZUL - G","BRANCO - P", "BRANCO - M", "BRANCO - G");

        listViewResultados.setItems(resultadosVariacao);
        listViewResultados.getStyleClass().add("list-view-grade");
        listViewResultados.getSelectionModel().selectFirst();

        VBox vBox = new VBox(listViewResultados);
        VBox.setVgrow(listViewResultados, Priority.ALWAYS);

        vBox.setFillWidth(true);
        vBox.setPrefHeight(Double.MAX_VALUE);
        vBox.setPrefWidth(300);
        return vBox;
    }

    public VBox botoesGrade(){
        Button btnCima = new Button(null, new FontIcon(Feather.ARROW_UP));
        btnCima.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.ACCENT);

        Button btnBaixo = new Button(null, new FontIcon(Feather.ARROW_DOWN));
        btnBaixo.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.ACCENT);

        Button btnDireita = new Button(null, new FontIcon(Feather.CHEVRON_RIGHT));
        btnDireita.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.ACCENT);

        VBox botoes = new VBox(20, btnCima, btnBaixo, btnDireita);
        botoes.setAlignment(Pos.CENTER);
        botoes.setFillWidth(true);

        //EVENTOS
        btnCima.setOnAction(e -> acaoMoverCima(listViewGrades));

        btnBaixo.setOnAction(e -> acaoMoverBaixo(listViewGrades));

        //Direita
        btnDireita.setOnAction(e ->{
            //System.out.println(listViewGrades.getItems());

            List<String> t = listViewGrades.getSelectionModel().getSelectedItems();
            List<Integer> selecionados = listViewGrades.getSelectionModel().getSelectedIndices();
            System.out.println("Selecionados: " + selecionados);
        });

        return botoes;
    }

    public HBox criarRodape(){
        Button button = new Button("Botão Rodapé", new FontIcon(Feather.PLUS));
        int itensPorPagina = 10;

        Pageable pageableInicial = PageRequest.of(0, itensPorPagina);


        button.setOnAction(e -> {
            Page<ClienteBusca> paginaClientes = clienteService.buscarClientes("Marcos", "", "", pageableInicial);
            System.out.println(paginaClientes);
        });

        HBox hBoxRodape = new HBox(button);
        hBoxRodape.setPrefHeight(150);
        hBoxRodape.getStyleClass().add("hbox-rodape");
        hBoxRodape.setFillHeight(true);

        return hBoxRodape;
    }

    public void acaoMoverCima(ListView<String> listview){
        int index = listview.getSelectionModel().getSelectedIndex();
        String item = listview.getSelectionModel().getSelectedItem();

        if(index > 0){
            listview.getItems().remove(index);
            listview.getItems().add(index - 1, item);
            listview.getSelectionModel().select(index - 1);
        }
    }

    public void acaoMoverBaixo(ListView<String> listview){
        int index = listview.getSelectionModel().getSelectedIndex();
        String item = listview.getSelectionModel().getSelectedItem();

        if(index >= 0 && index < listview .getItems().size() - 1){
            listview.getItems().remove(index);
            listview.getItems().add(index + 1, item);
            listview.getSelectionModel().select(index + 1);
        }
    }
}
