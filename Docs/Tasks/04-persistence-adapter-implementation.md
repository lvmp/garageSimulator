# Tarefa 4: Implementação do Adaptador de Persistência

**ID**: 04
**Related ADRs**: ADR-003

**Descrição**: Conectar a camada de aplicação ao banco de dados, implementando os `RepositoryPort` com Spring Data JPA.

---

### Sub-tarefa 4.1: Criação das Entidades JPA e Mappers

*   **Descrição**: Criar as classes que serão mapeadas para as tabelas do banco e os conversores entre os modelos.
*   **Patterns**: **Entity** (JPA), **Data Mapper**.
*   **Implementação**:
    1.  Crie as classes `...Entity` no pacote `infrastructure.persistence.entity`, anotadas com `@Entity`.
    2.  Crie um objeto ou classe `EntityMapper` para converter os modelos de domínio para as entidades JPA e vice-versa.
*   **Testes**: Crie testes unitários para o `EntityMapper`.

### Sub-tarefa 4.2: Implementação dos Adaptadores de Repositório

*   **Descrição**: Criar as classes que implementam as interfaces `...RepositoryPort`.
*   **Patterns**: **Adapter**.
*   **Implementação**:
    1.  Crie as interfaces Spring Data que estendem `JpaRepository`.
    2.  Crie as classes `...RepositoryAdapter` que implementam os `...RepositoryPort`. Elas injetam as interfaces Spring Data e o `EntityMapper` para realizar as operações.
*   **Testes de Integração**:
    *   Crie testes para os `...RepositoryAdapter` usando `@DataJpaTest` e um banco de dados em memória (H2) para validar a interação com o banco.

### Critérios de Aceitação
*   As entidades JPA e os mappers estão implementados e testados.
*   Os adaptadores de repositório implementam corretamente os ports da aplicação.
*   Os testes de integração com `@DataJpaTest` passam com sucesso.