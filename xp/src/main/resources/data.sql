-- =============================================================================
-- XP Corridas — dados iniciais (idempotentes)
-- Executado após o Hibernate criar/atualizar o schema (ver application.properties)
-- =============================================================================

-- Corridas -------------------------------------------------------------------
INSERT INTO corrida (nome, tempo_limite_segundos)
SELECT 'Grande Prêmio XP', 60
WHERE NOT EXISTS (SELECT 1 FROM corrida WHERE nome = 'Grande Prêmio XP');

INSERT INTO corrida (nome, tempo_limite_segundos)
SELECT 'Sprint IFPB', 60
WHERE NOT EXISTS (SELECT 1 FROM corrida WHERE nome = 'Sprint IFPB');

UPDATE corrida SET tempo_limite_segundos = 60 WHERE nome = 'Grande Prêmio XP';
UPDATE corrida SET tempo_limite_segundos = 60 WHERE nome = 'Sprint IFPB';

-- Participantes --------------------------------------------------------------
INSERT INTO participante (nome, admin)
SELECT 'Admin', TRUE
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Admin');

INSERT INTO participante (nome, admin)
SELECT 'João', FALSE
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'João');

INSERT INTO participante (nome, admin)
SELECT 'Maria', FALSE
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Maria');

UPDATE participante
SET senha = '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE nome IN ('Admin', 'Joao', 'Maria')
  AND (senha IS NULL OR senha = '');

-- Participante x Corrida (ManyToMany) ----------------------------------------
INSERT INTO participante_corridas (participante_id, corridas_id)
SELECT p.id, c.id
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Admin'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (
    SELECT 1 FROM participante_corridas pc
    WHERE pc.participante_id = p.id AND pc.corridas_id = c.id
  );

INSERT INTO participante_corridas (participante_id, corridas_id)
SELECT p.id, c.id
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'João'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (
    SELECT 1 FROM participante_corridas pc
    WHERE pc.participante_id = p.id AND pc.corridas_id = c.id
  );

INSERT INTO participante_corridas (participante_id, corridas_id)
SELECT p.id, c.id
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Maria'
  AND c.nome = 'Sprint IFPB'
  AND NOT EXISTS (
    SELECT 1 FROM participante_corridas pc
    WHERE pc.participante_id = p.id AND pc.corridas_id = c.id
  );

-- Perguntas — Grande Prêmio XP (5) -------------------------------------------
INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'Qual linguagem roda na JVM?', 1, c.id
FROM corrida c
WHERE c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM pergunta p WHERE p.enunciado = 'Qual linguagem roda na JVM?');

INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'Qual anotação marca uma classe como controller no Spring?', 0, c.id
FROM corrida c
WHERE c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM pergunta p WHERE p.enunciado = 'Qual anotação marca uma classe como controller no Spring?');

INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'Qual protocolo o navegador usa para acessar páginas web?', 0, c.id
FROM corrida c
WHERE c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM pergunta p WHERE p.enunciado = 'Qual protocolo o navegador usa para acessar páginas web?');

INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'O que significa JPA no contexto Java?', 1, c.id
FROM corrida c
WHERE c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM pergunta p WHERE p.enunciado = 'O que significa JPA no contexto Java?');

INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'Qual banco relacional o projeto XP utiliza?', 2, c.id
FROM corrida c
WHERE c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM pergunta p WHERE p.enunciado = 'Qual banco relacional o projeto XP utiliza?');

-- Perguntas — Sprint IFPB (5) ------------------------------------------------
INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'Qual framework facilita injeção de dependência no Java?', 2, c.id
FROM corrida c
WHERE c.nome = 'Sprint IFPB'
  AND NOT EXISTS (
    SELECT 1 FROM pergunta p
    WHERE p.enunciado = 'Qual framework facilita injeção de dependência no Java?'
  );

INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'Qual template engine é usada neste projeto?', 1, c.id
FROM corrida c
WHERE c.nome = 'Sprint IFPB'
  AND NOT EXISTS (SELECT 1 FROM pergunta p WHERE p.enunciado = 'Qual template engine é usada neste projeto?');

INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'Qual é a porta padrão do PostgreSQL?', 2, c.id
FROM corrida c
WHERE c.nome = 'Sprint IFPB'
  AND NOT EXISTS (SELECT 1 FROM pergunta p WHERE p.enunciado = 'Qual é a porta padrão do PostgreSQL?');

INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'Em Spring MVC, qual anotação mapeia requisições HTTP GET?', 0, c.id
FROM corrida c
WHERE c.nome = 'Sprint IFPB'
  AND NOT EXISTS (
    SELECT 1 FROM pergunta p
    WHERE p.enunciado = 'Em Spring MVC, qual anotação mapeia requisições HTTP GET?'
  );

INSERT INTO pergunta (enunciado, resposta_correta, corrida_id)
SELECT 'O que guarda dados do usuário entre requisições no servidor?', 1, c.id
FROM corrida c
WHERE c.nome = 'Sprint IFPB'
  AND NOT EXISTS (
    SELECT 1 FROM pergunta p
    WHERE p.enunciado = 'O que guarda dados do usuário entre requisições no servidor?'
  );

-- Alternativas — Grande Prêmio XP --------------------------------------------
-- Qual linguagem roda na JVM?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Python' FROM pergunta p WHERE p.enunciado = 'Qual linguagem roda na JVM?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Python');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Java' FROM pergunta p WHERE p.enunciado = 'Qual linguagem roda na JVM?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Java');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'JavaScript' FROM pergunta p WHERE p.enunciado = 'Qual linguagem roda na JVM?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'JavaScript');

-- Qual anotação marca uma classe como controller no Spring?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '@Controller' FROM pergunta p WHERE p.enunciado = 'Qual anotação marca uma classe como controller no Spring?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '@Controller');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '@Entity' FROM pergunta p WHERE p.enunciado = 'Qual anotação marca uma classe como controller no Spring?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '@Entity');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '@Table' FROM pergunta p WHERE p.enunciado = 'Qual anotação marca uma classe como controller no Spring?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '@Table');

-- Qual protocolo o navegador usa para acessar páginas web?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'HTTP' FROM pergunta p WHERE p.enunciado = 'Qual protocolo o navegador usa para acessar páginas web?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'HTTP');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'FTP' FROM pergunta p WHERE p.enunciado = 'Qual protocolo o navegador usa para acessar páginas web?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'FTP');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'SMTP' FROM pergunta p WHERE p.enunciado = 'Qual protocolo o navegador usa para acessar páginas web?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'SMTP');

-- O que significa JPA no contexto Java?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Java Program Archive' FROM pergunta p WHERE p.enunciado = 'O que significa JPA no contexto Java?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Java Program Archive');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Java Persistence API' FROM pergunta p WHERE p.enunciado = 'O que significa JPA no contexto Java?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Java Persistence API');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'JSON Page Application' FROM pergunta p WHERE p.enunciado = 'O que significa JPA no contexto Java?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'JSON Page Application');

-- Qual banco relacional o projeto XP utiliza?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'MySQL' FROM pergunta p WHERE p.enunciado = 'Qual banco relacional o projeto XP utiliza?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'MySQL');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'SQLite' FROM pergunta p WHERE p.enunciado = 'Qual banco relacional o projeto XP utiliza?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'SQLite');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'PostgreSQL' FROM pergunta p WHERE p.enunciado = 'Qual banco relacional o projeto XP utiliza?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'PostgreSQL');

-- Alternativas — Sprint IFPB -------------------------------------------------
-- Qual framework facilita injeção de dependência no Java?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Django' FROM pergunta p WHERE p.enunciado = 'Qual framework facilita injeção de dependência no Java?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Django');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Flask' FROM pergunta p WHERE p.enunciado = 'Qual framework facilita injeção de dependência no Java?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Flask');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Spring' FROM pergunta p WHERE p.enunciado = 'Qual framework facilita injeção de dependência no Java?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Spring');

-- Qual template engine é usada neste projeto?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'JSP' FROM pergunta p WHERE p.enunciado = 'Qual template engine é usada neste projeto?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'JSP');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Thymeleaf' FROM pergunta p WHERE p.enunciado = 'Qual template engine é usada neste projeto?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Thymeleaf');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'FreeMarker' FROM pergunta p WHERE p.enunciado = 'Qual template engine é usada neste projeto?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'FreeMarker');

-- Qual é a porta padrão do PostgreSQL?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '3306' FROM pergunta p WHERE p.enunciado = 'Qual é a porta padrão do PostgreSQL?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '3306');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '8080' FROM pergunta p WHERE p.enunciado = 'Qual é a porta padrão do PostgreSQL?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '8080');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '5432' FROM pergunta p WHERE p.enunciado = 'Qual é a porta padrão do PostgreSQL?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '5432');

-- Em Spring MVC, qual anotação mapeia requisições HTTP GET?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '@GetMapping' FROM pergunta p WHERE p.enunciado = 'Em Spring MVC, qual anotação mapeia requisições HTTP GET?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '@GetMapping');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '@PostMapping' FROM pergunta p WHERE p.enunciado = 'Em Spring MVC, qual anotação mapeia requisições HTTP GET?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '@PostMapping');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, '@Entity' FROM pergunta p WHERE p.enunciado = 'Em Spring MVC, qual anotação mapeia requisições HTTP GET?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = '@Entity');

-- O que guarda dados do usuário entre requisições no servidor?
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'Cookie apenas' FROM pergunta p WHERE p.enunciado = 'O que guarda dados do usuário entre requisições no servidor?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'Cookie apenas');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'HttpSession' FROM pergunta p WHERE p.enunciado = 'O que guarda dados do usuário entre requisições no servidor?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'HttpSession');
INSERT INTO pergunta_alternativas (pergunta_id, alternativas)
SELECT p.id, 'HashMap estático' FROM pergunta p WHERE p.enunciado = 'O que guarda dados do usuário entre requisições no servidor?'
  AND NOT EXISTS (SELECT 1 FROM pergunta_alternativas pa WHERE pa.pergunta_id = p.id AND pa.alternativas = 'HashMap estático');

-- Participantes extras para demonstrar paginação no ranking ------------------
INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 01', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 01');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 02', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 02');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 03', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 03');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 04', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 04');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 05', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 05');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 06', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 06');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 07', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 07');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 08', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 08');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 09', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 09');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 10', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 10');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 11', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 11');

INSERT INTO participante (nome, admin, senha)
SELECT 'Aluno 12', FALSE, '$2a$10$104dQY2xr05gZOhPLMgBp.7eGgrVaYU669dDlmGCyMG8NCs4E/cxu'
WHERE NOT EXISTS (SELECT 1 FROM participante WHERE nome = 'Aluno 12');

-- Resultados extras para evidenciar paginação no ranking ---------------------
INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 10, '2026-06-25 10:00:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Admin'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (
    SELECT 1 FROM resultado r
    WHERE r.participante_id = p.id AND r.corrida_id = c.id
  );

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 9, '2026-06-25 10:01:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'João'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (
    SELECT 1 FROM resultado r
    WHERE r.participante_id = p.id AND r.corrida_id = c.id
  );

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 8, '2026-06-25 10:02:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Maria'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (
    SELECT 1 FROM resultado r
    WHERE r.participante_id = p.id AND r.corrida_id = c.id
  );

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 7, '2026-06-25 10:03:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 01'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 6, '2026-06-25 10:04:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 02'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 5, '2026-06-25 10:05:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 03'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 4, '2026-06-25 10:06:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 04'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 3, '2026-06-25 10:07:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 05'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 2, '2026-06-25 10:08:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 06'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 1, '2026-06-25 10:09:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 07'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 0, '2026-06-25 10:10:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 08'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 9, '2026-06-25 10:11:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 09'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 8, '2026-06-25 10:12:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 10'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 7, '2026-06-25 10:13:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 11'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);

INSERT INTO resultado (participante_id, corrida_id, pontuacao, data_hora)
SELECT p.id, c.id, 6, '2026-06-25 10:14:00'
FROM participante p
CROSS JOIN corrida c
WHERE p.nome = 'Aluno 12'
  AND c.nome = 'Grande Prêmio XP'
  AND NOT EXISTS (SELECT 1 FROM resultado r WHERE r.participante_id = p.id AND r.corrida_id = c.id);
