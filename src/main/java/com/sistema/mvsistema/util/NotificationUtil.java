package com.sistema.mvsistema.util;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import atlantafx.base.controls.Notification;


public class NotificationUtil {
    /**
     * Exibe uma notificação de erro "toast" não-bloqueante, aplica o estilo e foca o campo.
     * @param campo O componente que receberá o estilo
     * @param mensagem A mensagem de erro
     * @param estilo Estilo a ser aplicado (Styles.DANGER, Styles.WARNING, Styles.ACCENT, etc)
     * @return sempre false para ser usado em cadeias de validação
     */
    public static boolean exibirErro(Control campo, String mensagem, String estilo) {
        // 1. Aplica o estilo especificado ao campo
        campo.getStyleClass().add(estilo);

        // 2. Foca o campo para correção
        Platform.runLater(campo::requestFocus);

        // 3. Encontra o painel root
        Pane rootPane;
        try {
            rootPane = (StackPane) campo.getScene().getRoot();
        } catch (Exception e) {
            System.err.println("Erro Crítico de UI: O root da cena não é um StackPane.");
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

    // Versão simplificada (vermelho por padrão)
    public static boolean exibirErro(Control campo, String mensagem) {
        return exibirErro(campo, mensagem, Styles.DANGER);
    }
}
