# XP Corridas

## Introdução

O **XP Corridas** é um sistema web inspirado visualmente no **Windows XP**, desenvolvido para gerenciar corridas de perguntas e respostas com limite de tempo.

O projeto foi desenvolvido utilizando **Spring Boot**, **Thymeleaf**, **Hibernate/JPA** e **PostgreSQL**, aplicando conceitos de arquitetura MVC, persistência de dados e controle de sessões.

A proposta do sistema é permitir que administradores criem corridas e perguntas, enquanto os participantes realizam desafios em sequência, respondendo perguntas antes do tempo acabar.

---

# Fluxo Principal da Aplicação

1. O administrador acessa o sistema.
2. O administrador cadastra uma corrida.
3. O administrador cadastra perguntas relacionadas à corrida.
4. O participante acessa a tela inicial/login.
5. O participante inicia uma corrida.
6. O sistema registra o horário de início da corrida na sessão.
7. As perguntas são exibidas uma por vez.
8. O participante envia suas respostas.
9. O sistema valida as respostas enviadas.
10. O sistema controla o tempo restante da corrida.
11. Ao finalizar todas as perguntas ou estourar o tempo:

    * o participante é redirecionado para a tela de resultado.
12. O sistema exibe mensagens utilizando Flash Attributes.

---

# Requisitos Funcionais

## UC01 – Usuário admin cadastra corrida

O administrador pode cadastrar uma nova corrida contendo suas informações principais.

## UC02 – Usuário admin exclui corrida

O administrador pode remover corridas cadastradas.

## UC03 – Usuário admin modifica corrida

O administrador pode editar os dados de uma corrida existente.

## UC04 – Usuário admin cadastra pergunta

O administrador pode cadastrar perguntas vinculadas a uma corrida.

## UC05 – Usuário admin exclui pergunta

O administrador pode remover perguntas cadastradas.

## UC06 – Usuário admin lista perguntas cadastradas

O administrador pode visualizar todas as perguntas registradas no sistema.

## UC07 – Participante inicia corrida

O participante pode iniciar uma corrida disponível.

## UC08 – Sistema registra tempo de início

Ao iniciar uma corrida, o sistema salva o horário de início utilizando sessão HTTP.

## UC09 – Sistema exibe perguntas sequencialmente

As perguntas são apresentadas uma por vez ao participante.

## UC10 – Sistema valida respostas

O sistema verifica se a resposta enviada pelo participante está correta.

## UC11 – Sistema controla tempo restante

O sistema acompanha o tempo disponível durante a corrida.

## UC12 – Sistema finaliza corrida

Ao terminar as perguntas ou exceder o tempo limite, o participante é redirecionado para a tela de resultado.

## UC13 – Sistema exibe mensagens de feedback

O sistema utiliza Flash Attributes para exibir mensagens temporárias ao usuário.

---

# Requisitos Não Funcionais

| ID    | Requisito                                        |
| ----- | ------------------------------------------------ |
| RNF01 | Utilizar Spring Boot na implementação            |
| RNF02 | Utilizar Thymeleaf para renderização das páginas |
| RNF03 | Utilizar PostgreSQL como banco de dados          |
| RNF04 | Utilizar Hibernate/JPA para persistência         |
| RNF05 | Utilizar arquitetura MVC                         |
| RNF06 | Utilizar sessões HTTP para controle da corrida   |
| RNF07 | Utilizar CSS customizado inspirado no Windows XP |
| RNF08 | Utilizar Git/GitHub para versionamento           |
| RNF09 | Utilizar padrão Post/Redirect/Get                |
| RNF10 | Exibir mensagens utilizando Flash Attributes     |