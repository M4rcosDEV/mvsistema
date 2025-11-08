package com.sistema.mvsistema.view.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.Pair;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import atlantafx.base.controls.Notification;

import java.util.Optional;


public class NotificacaoComponent {
    /**
     * Exibe uma notificação de erro "toast" não-bloqueante, aplica o estilo e foca o campo.
     * @param campo O componente que receberá o estilo
     * @param mensagem A mensagem de erro
     * @param estilo Estilo a ser aplicado (Styles.DANGER, Styles.WARNING, Styles.ACCENT, etc)
     * @return sempre false para ser usado em cadeias de validação
     */
    public static boolean exibirNotificacao(Control campo, String mensagem, String estilo) {
        // 1. Aplica o estilo especificado ao campo
        campo.getStyleClass().add(estilo);

        // 2. Foca o campo para correção
        Platform.runLater(campo::requestFocus);

        // 3. Encontra o painel root
        Pane rootPane;
        try {
            rootPane = (StackPane) campo.getScene().getRoot();
        } catch (Exception e) {
            System.err.println("Erro Crítico de UI: O root da cena não é um StackPane. " + e.getMessage());
            return false;
        }

        // 4. Cria a Notificação
        final var msg = new Notification(mensagem, new FontIcon(Material2AL.ERROR_OUTLINE));
        msg.getStyleClass().addAll(estilo, Styles.ELEVATED_1);
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);

        // 5. Posiciona a notificação
        StackPane.setAlignment(msg, Pos.TOP_RIGHT);
        StackPane.setMargin(msg, new Insets(10, 10, 0, 0));

        // 6. Define a animação de saída
        Runnable fecharNotificacao = () -> {
            if (!rootPane.getChildren().contains(msg)) return;
            var out = Animations.slideOutUp(msg, Duration.millis(250));
            out.setOnFinished(f -> rootPane.getChildren().remove(msg));
            out.playFromStart();
        };

        msg.setOnClose(e -> fecharNotificacao.run());

        // 7. Timer para auto-fechar
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> fecharNotificacao.run());

        // 8. Exibe a notificação
        var in = Animations.slideInDown(msg, Duration.millis(250));
        if (!rootPane.getChildren().contains(msg)) {
            rootPane.getChildren().add(msg);
        }
        in.playFromStart();
        delay.play();

        return false;
    }

    public static boolean exibirNotificacao(Control campo, String mensagem) {
        return exibirNotificacao(campo, mensagem, Styles.DANGER);
    }

    public static void exibirNotificacao(Pane rootPane, String mensagem, String estilo) {
        final var msg = new Notification(mensagem, new FontIcon(Material2AL.CHECK_CIRCLE_OUTLINE));
        msg.getStyleClass().addAll(estilo, Styles.ELEVATED_1);
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);

        StackPane.setAlignment(msg, Pos.TOP_RIGHT);
        StackPane.setMargin(msg, new Insets(10, 10, 0, 0));

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> rootPane.getChildren().remove(msg));

        if (!rootPane.getChildren().contains(msg)) {
            rootPane.getChildren().add(msg);
        }

        delay.play();
    }

    public static void exibirNotificacao(Pane rootPane, String mensagem, String estilo, Pos ladoNotificacao) {
        final var msg = new Notification(mensagem, new FontIcon(Material2AL.CHECK_CIRCLE_OUTLINE));
        msg.getStyleClass().addAll(estilo, Styles.ELEVATED_1);
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);

        StackPane.setAlignment(msg, ladoNotificacao);
        StackPane.setMargin(msg, new Insets(10, 10, 0, 0));

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> rootPane.getChildren().remove(msg));

        if (!rootPane.getChildren().contains(msg)) {
            rootPane.getChildren().add(msg);
        }

        delay.play();
    }

    public static boolean exibirNotificacaoConfirmacao(Window owner, String mensagem){
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("custom-alert");

        ButtonType yesBtn = new ButtonType("Sim, Sair", ButtonBar.ButtonData.YES);
        ButtonType noBtn = new ButtonType("Não, Continuar", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesBtn, noBtn);
        alert.initOwner(owner.getScene().getWindow());

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesBtn;
    }

    public static boolean exibirNotificacaoConfirmacao(Window owner, String mensagem, String msgSim, String msgNao){
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("custom-alert");

        ButtonType yesBtn = new ButtonType(msgSim, ButtonBar.ButtonData.YES);
        ButtonType noBtn = new ButtonType(msgNao, ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesBtn, noBtn);
        alert.initOwner(owner.getScene().getWindow());

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesBtn;
    }

    public static Pair<Boolean, String> exibirNotificacaoInput(Window owner, String mensagem, String msgSim, String msgNao) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Consultar CEP");
        dialog.setHeaderText(null);
        dialog.setContentText(mensagem);
        dialog.initOwner(owner);


        ButtonType btnSim = new ButtonType(msgSim, ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNao = new ButtonType(msgNao, ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().setAll(btnSim, btnNao);

        dialog.getDialogPane().getStyleClass().add("custom-notificacao");

        final ButtonType[] clickedButton = new ButtonType[1];

        dialog.setResultConverter(button -> {
            clickedButton[0] = button;
            if (button == btnSim) {
                return dialog.getEditor().getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        boolean confirmou = clickedButton[0] == btnSim;
        String textoDigitado = result.orElse(null);

        return new Pair<>(confirmou, textoDigitado);
    }

}
