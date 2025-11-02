package com.sistema.mvsistema.util;

import org.mindrot.jbcrypt.BCrypt;

public class CriptografiaUtil {
    private static final int HASH_SALT = 12;

    /**
     * Gerar o hash da senha
     * @param senha
     * @return senha criptografada com o hast
     */
    public static String gerarHash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(HASH_SALT));
    }

    /**
     * Verifica se a senha corresponde ao hash armazenado.
     * @param senha A senha informada.
     * @param senhaHash O hash que está salvo no banco de dados.
     * @return true se as senhas coincidirem, false se não.
     */
    public static boolean verificarSenha(String senha, String senhaHash) {
        return BCrypt.checkpw(senha, senhaHash);
    }
}
