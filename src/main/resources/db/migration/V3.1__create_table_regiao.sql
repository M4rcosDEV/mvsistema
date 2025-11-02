CREATE TABLE public.regiao (
  id           BIGSERIAL PRIMARY KEY,
  nome         VARCHAR(50) NOT NULL
);

INSERT INTO regiao (id, nome) VALUES (1, 'Norte');
INSERT INTO regiao (id, nome) VALUES (2, 'Nordeste');
INSERT INTO regiao (id, nome) VALUES (3, 'Sudeste');
INSERT INTO regiao (id, nome) VALUES (4, 'Sul');
INSERT INTO regiao (id, nome) VALUES (5, 'Centro-Oeste');