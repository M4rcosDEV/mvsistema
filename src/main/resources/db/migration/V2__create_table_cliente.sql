CREATE TABLE public.cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL,
    sobrenome VARCHAR(30),
    nome_fantasia VARCHAR(150),
    tipo_pessoa VARCHAR(1) NOT NULL,
    cpf_cnpj VARCHAR(20),
    email VARCHAR(50),
    telefone VARCHAR(20),
    estado_civil VARCHAR(1),
    genero VARCHAR(1),
    registro VARCHAR(20),
    iscricao_est VARCHAR(20),
    data_nascimento DATE,
    observacao TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);
