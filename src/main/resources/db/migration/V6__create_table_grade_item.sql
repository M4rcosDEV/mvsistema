CREATE TABLE public.grade_item(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    grade_id INTEGER,
    FOREIGN KEY (grade_id) REFERENCES grade(id) ON DELETE CASCADE
);