package com.sistema.mvsistema.util;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentoUtil {

    public enum TipoDocumento { CPF, CNPJ }

    public static void configurarCampoDocumentoAdaptavel(TextField campo, AtomicReference<TipoDocumento> tipoAtual) {
        final boolean[] alterando = {false};

        campo.textProperty().addListener((obs, oldValue, newValue) -> {
            if (alterando[0]) return;
            if (newValue == null) return;

            String numeros = newValue.replaceAll("[^0-9]", "");
            int limite = (tipoAtual.get() == TipoDocumento.CPF ? 11 : 14);
            if (numeros.length() > limite) numeros = numeros.substring(0, limite);

            String formatado = (tipoAtual.get() == TipoDocumento.CPF)
                    ? formatarCpf(numeros)
                    : formatarCnpj(numeros);

            if (!formatado.equals(campo.getText())) {
                alterando[0] = true;
                campo.setText(formatado);
                campo.positionCaret(formatado.length());
                alterando[0] = false;
            }
        });
    }

    private static String formatarCpf(String numeros) {
        StringBuilder f = new StringBuilder();
        int len = numeros.length();
        if (len > 0) f.append(numeros, 0, Math.min(3, len));
        if (len > 3) f.append(".").append(numeros, 3, Math.min(6, len));
        if (len > 6) f.append(".").append(numeros, 6, Math.min(9, len));
        if (len > 9) f.append("-").append(numeros, 9, Math.min(11, len));
        return f.toString();
    }

    private static String formatarCnpj(String numeros) {
        StringBuilder f = new StringBuilder();
        int len = numeros.length();
        if (len > 0) f.append(numeros, 0, Math.min(2, len));
        if (len > 2) f.append(".").append(numeros, 2, Math.min(5, len));
        if (len > 5) f.append(".").append(numeros, 5, Math.min(8, len));
        if (len > 8) f.append("/").append(numeros, 8, Math.min(12, len));
        if (len > 12) f.append("-").append(numeros, 12, Math.min(14, len));
        return f.toString();
    }
//    // Metodo para formatar CPF
//    public static String formatarCPF(String cpf) {
//        cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos
//        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
//    }
//
//    // Metodo para formatar CNPJ
//    public static String formatarCNPJ(String cnpj) {
//        cnpj = cnpj.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos
//        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
//    }

    // Metodo para validar CPF
    public static boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false; // CPF deve ter 11 dígitos e não pode ser formado por dígitos repetidos
        }

        // Cálculo do dígito verificador
        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = cpf.charAt(i) - '0';
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += digits[i] * (10 - i);
        }
        int remainder = sum % 11;
        int digit1 = (remainder < 2) ? 0 : (11 - remainder);


        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += digits[i] * (11 - i);
        }
        remainder = sum % 11;
        int digit2 = (remainder < 2) ? 0 : (11 - remainder);


        return (digit1 == digits[9] && digit2 == digits[10]);
    }

    // Metodo para validar CNPJ
    public static boolean validarCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", ""); // Remover caracteres não numéricos

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false; // CNPJ deve ter 14 dígitos e não pode ser formado por dígitos repetidos
        }

        int[] pesosPrimeiroDigito = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesosSegundoDigito = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int[] numeros = new int[14];
        for (int i = 0; i < 14; i++) {
            numeros[i] = cnpj.charAt(i) - '0';
        }

        // === Cálculo do primeiro dígito verificador ===
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += numeros[i] * pesosPrimeiroDigito[i];
        }
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : (11 - resto);

        // === Cálculo do segundo dígito verificador ===
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += numeros[i] * pesosSegundoDigito[i];
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : (11 - resto);

        return (digito1 == numeros[12] && digito2 == numeros[13]);
    }

    // Metodo para formatar CPF enquanto usuario digita
    public static void aplicarMascaraCpf(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            String numeros = newValue.replaceAll("[^0-9]", "");

            if (numeros.length() > 11) {
                numeros = numeros.substring(0, 11);
            }

            StringBuilder formatado = new StringBuilder();
            int len = numeros.length();

            if (len > 0) formatado.append(numeros, 0, Math.min(3, len));
            if (len > 3) formatado.append(".").append(numeros, 3, Math.min(6, len));
            if (len > 6) formatado.append(".").append(numeros, 6, Math.min(9, len));
            if (len > 9) formatado.append("-").append(numeros, 9, Math.min(11, len));

            if (!textField.getText().contentEquals(formatado)) {
                textField.setText(formatado.toString());
                textField.positionCaret(formatado.length());
            }
        });
    }

    // Metodo para formatar CNPJ enquanto usuario digita
    public static void aplicarMascaraCnpj(TextField textField) {

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            String numeros = newValue.replaceAll("[^0-9]", "");

            if (numeros.length() > 14) {
                numeros = numeros.substring(0, 14);
            }

            StringBuilder formatado = new StringBuilder();
            int len = numeros.length();

            if (len > 0) formatado.append(numeros, 0, Math.min(2, len));
            if (len > 2) formatado.append(".").append(numeros, 2, Math.min(5, len));
            if (len > 5) formatado.append(".").append(numeros, 5, Math.min(8, len));
            if (len > 8) formatado.append("/").append(numeros, 8, Math.min(12, len));
            if (len > 12) formatado.append("-").append(numeros, 12, Math.min(14, len));

            if (!textField.getText().contentEquals(formatado)) {
                textField.setText(formatado.toString());
                textField.positionCaret(formatado.length());
            }
        });
    }

    // Metodo para formatar CPF e CNPJ enquanto usuario digita
    public static void aplicarMascaraCpfCnpj(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            // Remove tudo que não for número
            String numeros = newValue.replaceAll("[^0-9]", "");

            // Limita no máximo a 14 dígitos (CNPJ)
            if (numeros.length() > 14) {
                numeros = numeros.substring(0, 14);
            }

            StringBuilder formatado = new StringBuilder();
            int len = numeros.length();

            if (len <= 11) {
                // === CPF ===
                if (len > 0) formatado.append(numeros, 0, Math.min(3, len));
                if (len > 3) formatado.append(".").append(numeros, 3, Math.min(6, len));
                if (len > 6) formatado.append(".").append(numeros, 6, Math.min(9, len));
                if (len > 9) formatado.append("-").append(numeros, 9, Math.min(11, len));
            } else {
                // === CNPJ ===
                formatado.append(numeros, 0, 2);
                formatado.append(".").append(numeros, 2, 5);
                formatado.append(".").append(numeros, 5, 8);
                formatado.append("/").append(numeros, 8, 12);
                if (len > 12) formatado.append("-").append(numeros, 12, Math.min(14, len));
            }

            // Evita ‘loop’ de atualização
            if (!textField.getText().contentEquals(formatado)) {
                textField.setText(formatado.toString());
                textField.positionCaret(formatado.length());
            }
        });
    }

    public static void aplicarMascaraTelefone(TextField textField) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            // Remove tudo que não for número
            String numeros = newValue.replaceAll("[^0-9]", "");

            // Limita a 11 dígitos (formato máximo com DDD + celular)
            if (numeros.length() > 11) {
                numeros = numeros.substring(0, 11);
            }

            StringBuilder formatado = new StringBuilder();
            int len = numeros.length();

            if (len > 0) {
                formatado.append("(").append(numeros, 0, Math.min(2, len)); // DDD
            }

            if (len > 2) {
                formatado.append(") ");
                // Define se é celular (9 dígitos) ou fixo (8 dígitos)
                if (len > 7) { // formato celular
                    formatado.append(numeros, 2, Math.min(7, len));
                    formatado.append("-").append(numeros, 7, len);
                } else { // formato fixo
                    if (len > 6) {
                        formatado.append(numeros, 2, 6);
                        formatado.append("-").append(numeros, 6, len);
                    } else {
                        formatado.append(numeros.substring(2));
                    }
                }
            }

            // Evita loop de atualização
            if (!textField.getText().equals(formatado.toString())) {
                textField.setText(formatado.toString());
                textField.positionCaret(formatado.length());
            }
        });
    }

    // Metodo para formatar CPF enquanto usuario digita
    public static void aplicarMascaraCep(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            String numeros = newValue.replaceAll("[^0-9]", "");

            if (numeros.length() > 8) {
                numeros = numeros.substring(0, 8);
            }

            StringBuilder formatado = new StringBuilder();
            int len = numeros.length();

            if (len > 0) formatado.append(numeros, 0, Math.min(5, len));
            if (len > 5) formatado.append("-").append(numeros, 5, Math.min(8, len));

            if (!textField.getText().contentEquals(formatado)) {
                textField.setText(formatado.toString());
                textField.positionCaret(formatado.length());
            }
        });
    }

    // Metodo para formatar Data enquanto usuario digita
    public static void aplicarMarcaraData(DatePicker datePicker){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TextField editor = datePicker.getEditor();

        editor.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                return;
            }
            String numeros = newText.replaceAll("[^0-9]", "");

            if(numeros.length() > 8){
                numeros = numeros.substring(0, 8);
            }
            StringBuilder formatado = new StringBuilder();

            for(int i=0; i<numeros.length(); i++){
                formatado.append(numeros.charAt(i));

                if(i == 1 || i == 3){
                    formatado.append("/");
                }
            }

            if (!editor.getText().equals(formatado.toString())) {
                editor.setText(formatado.toString());
                editor.positionCaret(formatado.length());
            }

            editor.focusedProperty().addListener((observableValue, oldFocus, newFocus) -> {
                if (!newFocus) {
                    try {
                        LocalDate data = LocalDate.parse(editor.getText(), formatter);
                        datePicker.setValue(data);
                    } catch (DateTimeParseException e) {
                        datePicker.setValue(null);
                        editor.setText("");
                    }
                }
            });
        });
    }

    // Metodo para remover mascara
    public static void removerMascara(TextField campo) {
        String texto = campo.getText();
        if (texto != null) {
            campo.setText(texto.replaceAll("[^0-9]", ""));
        }
    }
}
