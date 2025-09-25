# Tarefa 1: Configuração Inicial do Projeto

**ID**: 01
**Related ADRs**: ADR-001, ADR-002

**Descrição**: O ponto de partida para todo o desenvolvimento. Esta tarefa consiste em inicializar um projeto Spring Boot usando Gradle, configurar as dependências essenciais e criar a estrutura de pacotes que irá acomodar as camadas da Clean Architecture.

---

### Passos da Implementação

1.  **Gerar o Projeto**: 
    *   Utilize o **Spring Initializr** (start.spring.io) com as seguintes configurações:
        *   **Project**: Gradle - Kotlin
        *   **Language**: Kotlin
        *   **Spring Boot**: 3.x.x
        *   **Packaging**: Jar
        *   **Java**: 21
        *   **Dependencies**: `Spring Web`, `Spring Data JPA`, `MySQL Driver`.

2.  **Estruturar os Pacotes**: 
    *   Dentro do pacote principal (ex: `com.garagesimulator`), crie a seguinte estrutura de diretórios para refletir as camadas da arquitetura:
        ```
        com.garagesimulator
        ├── domain
        │   └── model
        ├── application
        │   ├── port
        │   └── usecase
        └── infrastructure
            ├── configuration
            ├── controller
            │   └── dto
            └── persistence
                ├── entity
                └── repository
        ```
    *   **Princípio Aplicado**: Esta estrutura promove a **Separação de Responsabilidades (SRP)**, isolando fisicamente os componentes de cada camada.

3.  **Configurar o `application.properties`**:
    *   Adicione as configurações básicas para a conexão com o banco de dados. Os valores exatos serão usados pelo Docker Compose posteriormente, mas é bom ter os placeholders.
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/garage_db
        spring.datasource.username=root
        spring.datasource.password=root
        spring.jpa.hibernate.ddl-auto=update
        ```

### Critérios de Aceitação
*   O projeto é gerado e pode ser importado em uma IDE sem erros.
*   A estrutura de pacotes (`domain`, `application`, `infrastructure`) está criada.
*   O comando `./gradlew build` executa com sucesso.