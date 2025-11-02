package com.sistema.mvsistema.service;

import java.security.SecureRandom;

public class ProdutoService {
    private static final SecureRandom random = new SecureRandom();

    public static String gerarCodigoInterno(){

        StringBuilder stringBuilder = new StringBuilder();
        int fistNumber = random.nextInt(9) + 1;
        stringBuilder.append(fistNumber);

        for (int i = 0; i < 9; i++) {
            int numerosAleatorios = random.nextInt(10);
            stringBuilder.append(numerosAleatorios);
        }

        System.out.println(stringBuilder);
        return stringBuilder.toString();
    }
}
