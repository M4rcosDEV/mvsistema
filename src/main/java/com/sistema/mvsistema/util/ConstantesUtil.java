package com.sistema.mvsistema.util;

public class ConstantesUtil {
    // Gênero - VARCHAR(1)
    public static final String GENERO_MASCULINO = "M";
    public static final String GENERO_FEMININO = "F";
    public static final String GENERO_OUTRO = "O";
    public static final String GENERO_NAO_INFORMAR = "N";

    // Tipo de Pessoa - VARCHAR(1)
    public static final String TIPO_PESSOA_FISICA = "F";
    public static final String TIPO_PESSOA_JURIDICA = "J";
    public static final String TIPO_PESSOA_PRODUTOR = "P";
    public static final String TIPO_PESSOA_INSTITUICAO = "I";

    // Estado Civil - VARCHAR(1)
    public static final String ESTADO_CIVIL_SOLTEIRO = "S";
    public static final String ESTADO_CIVIL_CASADO = "C";
    public static final String ESTADO_CIVIL_DIVORCIADO = "D";
    public static final String ESTADO_CIVIL_VIUVO = "V";
    public static final String ESTADO_CIVIL_UNIAO_ESTAVEL = "U";
    public static final String ESTADO_CIVIL_NAO_INFORMAR = "N";

    public static final int VISUALIZACAO = 0;
    public static final int EDICAO = 1;
    public static final int NOVO = 2;

    // Métodos para obter o valor do banco para exibição
    public static String getGeneroTela(String valor) {
        if (valor == null) {
            return null;
        }

        return switch (valor) {
            case GENERO_MASCULINO -> "Masculino";
            case GENERO_FEMININO -> "Feminino";
            case GENERO_OUTRO -> "Outro";
            case GENERO_NAO_INFORMAR -> "Não informar";
            default -> "Selecione";
        };
    }

    public static String getTipoPessoaTela(String valor) {
        if (valor == null) {
            return null;
        }

        return switch (valor) {
            case TIPO_PESSOA_FISICA -> "Física";
            case TIPO_PESSOA_JURIDICA -> "Jurídica";
            case TIPO_PESSOA_PRODUTOR -> "Produtor";
            case TIPO_PESSOA_INSTITUICAO -> "Instituição";
            default -> "Selecione o tipo de pessoa";
        };
    }

    public static String getEstadoCivilTela(String valor) {
        if (valor == null) {
            return null;
        }

        return switch (valor) {
            case ESTADO_CIVIL_SOLTEIRO -> "Solteiro(a)";
            case ESTADO_CIVIL_CASADO -> "Casado(a)";
            case ESTADO_CIVIL_DIVORCIADO -> "Divorciado(a)";
            case ESTADO_CIVIL_VIUVO -> "Viúvo(a)";
            case ESTADO_CIVIL_UNIAO_ESTAVEL -> "União Estável";
            case ESTADO_CIVIL_NAO_INFORMAR -> "Não informar";
            default -> "Selecione";
        };
    }

    // Métodos para obter o valor do banco a partir do display
    public static String getGeneroBanco(String valorTela) {
        if (valorTela == null) {
            return null;
        }
        return switch (valorTela) {
            case "Masculino" -> GENERO_MASCULINO;
            case "Feminino" -> GENERO_FEMININO;
            case "Outro" -> GENERO_OUTRO;
            case "Não informar" -> GENERO_NAO_INFORMAR;
            default -> null;
        };
    }

    public static String getTipoPessoaBanco(String valorTela) {
        if (valorTela == null) {
            return null;
        }
        return switch (valorTela) {
            case "Física" -> TIPO_PESSOA_FISICA;
            case "Jurídica" -> TIPO_PESSOA_JURIDICA;
            case "Produtor" -> TIPO_PESSOA_PRODUTOR;
            case "Instituição" -> TIPO_PESSOA_INSTITUICAO;
            default -> null;
        };
    }

    public static String getEstadoCivilBanco(String valorTela) {
        if (valorTela == null) {
            return null;
        }

        return switch (valorTela) {
            case "Solteiro(a)" -> ESTADO_CIVIL_SOLTEIRO;
            case "Casado(a)" -> ESTADO_CIVIL_CASADO;
            case "Divorciado(a)" -> ESTADO_CIVIL_DIVORCIADO;
            case "Viúvo(a)" -> ESTADO_CIVIL_VIUVO;
            case "União Estável" -> ESTADO_CIVIL_UNIAO_ESTAVEL;
            case "Não informar" -> ESTADO_CIVIL_NAO_INFORMAR;
            default -> null;
        };
    }
}
