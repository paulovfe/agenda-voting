# agenda-voting
===
--- 
> API REST para votação em pautas
---

Nela é possível:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação fica aberta por um tempo determinado ou 1 minuto por default)
- Receber votos dos associados em pautas ('Sim'/'Não'. Cada associado é identificado pelo cpf e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

## Stack utilizada
- Java 14
- Gradle 6.7.1
- Spring Boot 2.4.1
- MongoDB
- Openapi
- Kafka

## Executando a aplicação localmente

# Pré Requisitos:
Ter instalado localmente:  
- JDK 14
- Docker

1.Inicie os containers (docker) com dependências da aplicação (Mongo e Kafka):
    
     docker-compose up
 
2.Inicie a aplicação:
    
     ./gradlew bootRun

## API

Localmente a API sobe na porta 8080

Documentação disponível em: http://localhost:8080/swagger-ui.html


## Executando testes
Basta executar o comando abaixo para a execução dos testes unitários e de integração.

     ./gradlew test