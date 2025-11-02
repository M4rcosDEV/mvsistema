package com.sistema.mvsistema.util;

import com.sistema.mvsistema.Main;
import javafx.scene.Scene;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class EstilosGlobal {
    /**
     * O caminho ABSOLUTO para o seu arquivo CSS, a partir da raiz
     * do seu 'resources' ou 'classpath'.
     * * O seu Main.java estava em 'com.sistema.mvsistema' e usava "styles.css",
     * então o caminho absoluto é este:
     */
    private static final String CSS_PATH = "/static/css/styles.css";

    // Vamos guardar o caminho em cache para não ter que buscar toda hora
    private static String cachedCssExternalForm;

    /**
     * Busca o caminho completo do CSS e o guarda em cache.
     */
    private static String getCssPath() {
        if (cachedCssExternalForm == null) {
            try {
                // Usamos .class de qualquer classe do projeto (Main) para pegar o ClassLoader
                URL cssUrl = Main.class.getResource(CSS_PATH);
                if (cssUrl == null) {
                    System.err.println("ERRO CRÍTICO: Não foi possível encontrar o CSS: " + CSS_PATH);
                    cachedCssExternalForm = ""; // Marcar como "não encontrado"
                } else {
                    cachedCssExternalForm = cssUrl.toExternalForm();
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar o CSS: " + e.getMessage());
                cachedCssExternalForm = ""; // Marcar como "falha"
            }
        }
        return cachedCssExternalForm;
    }

    /**
     * O único método que você precisará chamar.
     * Ele aplica o CSS global a qualquer cena.
     * * @param scene A cena que deve receber os estilos.
     */
    public static void applyGlobalCss(Scene scene) {
        String cssPath = getCssPath();

        // Verifica se a cena é válida e se o caminho foi encontrado
        if (scene != null && cssPath != null && !cssPath.isEmpty()) {
            scene.getStylesheets().add(cssPath);
        }
    }
}
