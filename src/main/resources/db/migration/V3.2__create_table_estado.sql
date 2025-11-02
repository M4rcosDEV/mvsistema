CREATE TABLE public.estado (
    id           BIGSERIAL   PRIMARY KEY,
    codigo_uf    INTEGER     NOT NULL,
    nome         VARCHAR(50) NOT NULL,
    sigla        VARCHAR(2)  NOT NULL,
    regiao_id    INTEGER	 NOT NULL,
    FOREIGN KEY (regiao_id) REFERENCES regiao(id) ON DELETE CASCADE
);

INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (12, 'Acre', 'AC', 1);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (27, 'Alagoas', 'AL', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (16, 'Amapá', 'AP', 1);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (13, 'Amazonas', 'AM', 1);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (29, 'Bahia', 'BA', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (23, 'Ceará', 'CE', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (53, 'Distrito Federal', 'DF', 5);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (32, 'Espírito Santo', 'ES', 3);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (52, 'Goiás', 'GO', 5);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (21, 'Maranhão', 'MA', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (51, 'Mato Grosso', 'MT', 5);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (50, 'Mato Grosso do Sul', 'MS', 5);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (31, 'Minas Gerais', 'MG', 3);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (15, 'Pará', 'PA', 1);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (25, 'Paraíba', 'PB', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (41, 'Paraná', 'PR', 4);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (26, 'Pernambuco', 'PE', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (22, 'Piauí', 'PI', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (33, 'Rio de Janeiro', 'RJ', 3);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (24, 'Rio Grande do Norte', 'RN', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (43, 'Rio Grande do Sul', 'RS', 4);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (11, 'Rondônia', 'RO', 1);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (14, 'Roraima', 'RR', 1);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (42, 'Santa Catarina', 'SC', 4);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (35, 'São Paulo', 'SP', 3);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (28, 'Sergipe', 'SE', 2);
INSERT INTO estado (codigo_uf, nome, sigla, regiao_id) VALUES (17, 'Tocantins', 'TO', 1);