# Sistema Backend de Gerenciamento de Estacionamento

Este projeto implementa um sistema backend para gerenciar um estacionamento, controlando vagas, fluxo de veículos e calculando a receita com base em regras de negócio específicas.

## Tecnologias Utilizadas

*   **Linguagem:** Kotlin 2.1.x
*   **Framework:** Spring Boot 3.5.6
*   **Banco de Dados:** MySQL 8.0
*   **Build Tool:** Gradle (Kotlin DSL)
*   **Containerização:** Docker, Docker Compose
*   **Arquitetura:** Clean Architecture (Camadas: Domínio, Aplicação, Infraestrutura)

## Arquitetura

O projeto segue os princípios da **Clean Architecture**, com uma clara separação de responsabilidades em camadas:

*   **Domínio:** Contém as entidades de negócio puras e a lógica de negócio central.
*   **Aplicação:** Define os Casos de Uso (Use Cases) e as interfaces (Ports) para interagir com o Domínio e a Infraestrutura.
*   **Infraestrutura:** Contém as implementações concretas (Adapters/Gateways) dos Ports, incluindo a persistência de dados (JPA, MySQL) e os adaptadores web (REST Controllers).

## Como Instalar e Executar

O projeto utiliza Docker Compose para orquestrar a aplicação e o banco de dados.

### Pré-requisitos

*   Docker e Docker Compose instalados.
*   Java 21 (para desenvolvimento local sem Docker).

### Passos para Execução

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/lvmp/garageSimulator
    cd garageSimulator
    ```
2.  **Inicie o ambiente com Docker Compose:**
    ```bash
    docker-compose up --build
    ```
    Este comando irá construir a imagem da aplicação e iniciar os serviços do banco de dados MySQL e da aplicação backend. O `healthcheck` garante que a aplicação só inicie após o banco de dados estar pronto.

3.  **Verifique os serviços:**
    Após a execução, você deverá ver os logs dos serviços `mysql-db` e `garage-app` no terminal. A aplicação estará disponível na porta `3003`.

### Observação sobre o Simulador

O serviço `garage-simulator` foi temporariamente removido do `docker-compose.yml` devido à indisponibilidade da imagem no Docker Hub. A simulação de eventos de entrada e saída de veículos não será possível até que uma imagem alternativa seja fornecida ou construída.

## Endpoints da API

A aplicação expõe os seguintes endpoints REST:

### 1. Webhook de Eventos de Veículos

*   **Endpoint:** `POST http://localhost:3003/webhook`
*   **Descrição:** Recebe eventos de `ENTRY` e `EXIT` de veículos.
*   **Corpo da Requisição (Exemplo ENTRY):**
    ```json
    {
      "license_plate": "ZUL0001",
      "entry_time": "2025-01-01T12:00:00.000Z",
      "event_type": "ENTRY"
    }
    ```
*   **Corpo da Requisição (Exemplo EXIT):**
    ```json
    {
      "license_plate": "ZUL0001",
      "exit_time": "2025-01-01T12:00:00.000Z",
      "event_type": "EXIT"
    }
    ```
*   **Respostas:** `200 OK` para sucesso, `403 Forbidden` (garagem cheia), `404 Not Found` (sessão não encontrada), `409 Conflict` (vaga não disponível), `400 Bad Request` (dados inválidos), `500 Internal Server Error`.

### 2. Consulta de Receita

*   **Endpoint:** `GET http://localhost:3003/revenue`
*   **Descrição:** Retorna o faturamento total por setor e data.
*   **Corpo da Requisição:**
    ```json
    {
      "date": "2025-01-01",
      "sector": "A"
    }
    ```
*   **Corpo da Resposta (Exemplo):**
    ```json
    {
      "amount": 123.45,
      "currency": "BRL",
      "timestamp": "2025-01-01T12:00:00.000Z"
    }
    ```

## Documentação Adicional

*   **ADRs (Architecture Decision Records):** Consulte a pasta `Docs/ADR` para entender as decisões arquiteturais tomadas no projeto.
*   **Tarefas de Desenvolvimento:** A pasta `Docs/Tasks` contém o detalhamento das tarefas de implementação, incluindo padrões de design e princípios SOLID aplicados.

## Suporte

Para dúvidas ou para se conectar, você pode me encontrar no GitHub ([lvmp/garageSimulator](https://github.com/lvmp/garageSimulator)) ou no LinkedIn ([Luiz Vinicius](https://www.linkedin.com/in/luizvinicius/)). Ficarei feliz em me conectar!

---

> This is a challenge by [Coodesh](https://coodesh.com/)