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

O projeto utiliza Docker Compose para orquestrar a aplicação, o banco de dados e o simulador de eventos.

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
    Este comando irá construir a imagem da aplicação e iniciar os serviços do banco de dados MySQL, da aplicação backend e do simulador de eventos. O `healthcheck` garante que a aplicação só inicie após o banco de dados estar pronto.

3.  **Verifique os serviços:**
    Após a execução, você deverá ver os logs dos serviços `mysql-db`, `garage-app` e `garage-simulator` no terminal. A aplicação estará disponível na porta `3003`.

## Desenvolvimento Local (Spring Boot + Docker)

Para um ciclo de desenvolvimento mais rápido, você pode rodar a aplicação Spring Boot diretamente da sua IDE, enquanto o MySQL e o simulador continuam no Docker.

### Passos para Desenvolvimento Local

1.  **Inicie apenas os serviços de infraestrutura com Docker Compose:**
    ```bash
    docker-compose up mysql-db garage-simulator
    ```
    Isso iniciará o banco de dados MySQL e o simulador de eventos.

2.  **Execute a aplicação Spring Boot:**
    *   Abra o projeto na sua IDE (IntelliJ IDEA, VS Code, etc.).
    *   Certifique-se de que o Java 21 está configurado.
    *   Execute a classe principal `GarageSimulatorApplication.kt` (ou use a tarefa `bootRun` do Gradle: `./gradlew bootRun`).

    **Observação sobre a Porta:** Por padrão, o Spring Boot inicia na porta `8080`. No entanto, o simulador espera enviar eventos para a porta `3003`. Para testar com o simulador, certifique-se de que sua aplicação local esteja configurada para rodar na porta `3003` (por exemplo, adicionando `server.port=3003` ao seu `application.properties` ou `application.yaml` local, ou via variável de ambiente).

    **Observação sobre o Cliente HTTP:** O cliente HTTP (`SimulatorHttpClient`) utiliza `RestClient` e sua `baseUrl` é configurada no `RestClientConfig.kt`.

    A aplicação se conectará automaticamente ao `mysql-db` (via `localhost:3306`) e o simulador enviará eventos para `localhost:3003`.

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
