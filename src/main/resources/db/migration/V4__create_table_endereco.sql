CREATE TABLE public.endereco (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,               -- ID do cliente (chave estrangeira)
    nome VARCHAR(50),                         -- Nome do endereco
    tipo_endereco VARCHAR(50) NOT NULL,       -- Tipo de endereço (ex: "Residencial", "Comercial", "Entrega", etc.)
    cep VARCHAR(10),                          -- CEP
    rua VARCHAR(150),                         -- Nome da rua
    numero VARCHAR(20),                       -- Número
    complemento VARCHAR(100),                 -- Complemento (se houver)
    bairro  VARCHAR(50) NOT NULL,             -- Bairro
    municipio_id INTEGER NOT NULL,            -- Municipio
    estado VARCHAR(2) NOT NULL,               -- Estado
    pais VARCHAR(20),                         -- País
    ibge_code VARCHAR(10),
    FOREIGN KEY (cliente_id)   REFERENCES cliente(id)   ON DELETE CASCADE,
    FOREIGN KEY (municipio_id) REFERENCES municipio(id) ON DELETE CASCADE
);
