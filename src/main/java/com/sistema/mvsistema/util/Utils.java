package com.sistema.mvsistema.util;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

//bagunça, se for usar toma cuidado - se um metodo for muito bom, tira daqui
public class Utils {

    public static String converterTipoPessoaCharParaString(String tipoBanco) {
        if (tipoBanco == null) return null;
        return switch (tipoBanco.toUpperCase()) {
            case "F" -> "Física";
            case "J" -> "Jurídica";
            case "P" -> "Produtor";
            case "I" -> "Instituição";
            default -> null;
        };
    }

    public static String converterTipoPessoaStringParaChar(String tipoCombo) {
        if (tipoCombo == null) return null;
        return switch (tipoCombo) {
            case "Física" -> "F";
            case "Jurídica" -> "J";
            case "Produtor" -> "P";
            case "Instituição" -> "I";
            default -> null;
        };
    }

    @SuppressWarnings("unused")
    public static void executarComLoading(Stage stage, Runnable tarefa) {
        // Spinner (loading)
        ProgressIndicator indicador = new ProgressIndicator();
        indicador.setMaxSize(80, 80);

        StackPane overlay = new StackPane(indicador);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3)");
        overlay.setVisible(true);

        ((StackPane) stage.getScene().getRoot()).getChildren().add(overlay);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                tarefa.run();
                return null;
            }

            @Override
            protected void succeeded() {
                overlay.setVisible(false);
                ((StackPane) stage.getScene().getRoot()).getChildren().remove(overlay);
            }

            @Override
            protected void failed() {
                overlay.setVisible(false);
                ((StackPane) stage.getScene().getRoot()).getChildren().remove(overlay);
                new Alert(Alert.AlertType.ERROR, "Erro ao carregar dados de cache.").showAndWait();
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Executa uma tarefa em background que retorna um resultado e mostra um indicador de carregamento.
     *
     * @param root         Pane principal onde o overlay será exibido
     * @param task         Código em background que retorna o resultado (ex: busca no banco)
     * @param onFinished   Código executado na UI com o resultado da tarefa
     * @param <T>          Tipo de dado retornado (ex: List<Cliente>)
     */
    public static <T> void runWithLoadingResult(Pane root, Supplier<T> task, Consumer<T> onFinished) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(50, 50);

        StackPane overlay = new StackPane(progressIndicator);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        overlay.setPrefSize(root.getWidth(), root.getHeight());

        root.getChildren().add(overlay);

        new Thread(() -> {
            T result = null;
            try {
                result = task.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            T finalResult = result;
            Platform.runLater(() -> {
                root.getChildren().remove(overlay);
                if (onFinished != null) {
                    onFinished.accept(finalResult);
                }
            });
        }).start();

        //Exemplo de uso
//        btnBuscarClientes.setOnAction(e -> {
//            FXUtils.runWithLoadingResult(root,
//                    // Tarefa em background
//                    () -> clienteService.buscarTodos(),
//
//                    // O que fazer com o resultado na UI
//                    clientes -> {
//                        if (clientes != null) {
//                            tabelaClientes.setItems(FXCollections.observableList(clientes));
//                        } else {
//                            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao buscar clientes.");
//                            alert.showAndWait();
//                        }
//                    }
//            );
//        });
    }

    /**
     * Executa uma ação em background mostrando um indicador de carregamento.
     *
     * @param root      O container principal da tela (geralmente StackPane)
     * @param action    A operação que será executada em background (retorna um resultado)
     * @param onSuccess O que fazer quando a operação terminar (recebe o resultado)
     * @param <T>       Tipo do resultado retornado pela operação
     */
    public static <T> void runWithLoading(Pane root, Supplier<T> action, Consumer<T> onSuccess) {
        // === Cria indicador de carregamento ===
        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(50, 50);

        StackPane overlay = new StackPane(progress);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        overlay.setPrefSize(root.getWidth(), root.getHeight());

        // Adiciona o overlay à tela
        root.getChildren().add(overlay);

        // === Executa a ação em background ===
        CompletableFuture
                .supplyAsync(action)
                .thenAccept(result -> Platform.runLater(() -> {
                    root.getChildren().remove(overlay);
                    onSuccess.accept(result);
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        root.getChildren().remove(overlay);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erro");
                        alert.setHeaderText("Erro ao executar operação");
                        alert.setContentText(ex.getMessage());
                        alert.showAndWait();
                    });
                    return null;
                });
    }
}
