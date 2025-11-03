package com.sistema.mvsistema.util;

import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class FontesUtils {
    private static final String PATH_FONTES = "/static/fontes/";
    private static final Map<String, Font> CACHE_FONTES = new HashMap<>();

    /**
     * Carrega e cacheia uma fonte personalizada.
     *
     * @param nomeFonte     nome do arquivo da fonte (sem extensão)
     * @param extensaoFonte extensão da fonte (ex: "otf" ou "ttf")
     * @param tamanhoFonte  tamanho desejado da fonte
     * @return objeto Font já carregado e pronto para uso
     */
    public static Font usarFonte(String nomeFonte, String extensaoFonte, double tamanhoFonte){
        String chave = nomeFonte + "-" + tamanhoFonte;

        // Se a fonte já estiver no cache, retorna ela imediatamente
        if (CACHE_FONTES.containsKey(chave)) {
            return CACHE_FONTES.get(chave);
        }

        // Monta o caminho da fonte
        String path = PATH_FONTES + nomeFonte + "." + extensaoFonte;

        // Tenta carregar a fonte
        Font fonte = Font.loadFont(FontesUtils.class.getResourceAsStream(path), tamanhoFonte);

        if (fonte == null) {
            System.err.println("⚠️ Erro ao carregar fonte: " + path);
            return Font.getDefault(); // fallback para evitar NullPointerException
        }

        // Armazena no cache
        CACHE_FONTES.put(chave, fonte);

        return fonte;
    }
}
