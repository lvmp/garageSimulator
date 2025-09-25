# ADR-003: Estratégia de Persistência de Dados

**Status**: Aceito

**Contexto**: O sistema precisa manter o estado da garagem, incluindo a ocupação das vagas e o histórico de sessões de estacionamento para cálculo de receita. O PRD especifica o uso de um banco de dados relacional (MySQL). É necessário definir como a aplicação irá interagir com o banco de dados de forma consistente com a Clean Architecture.

**Complexidade Estimada**: Baixa.

**Decisão Tomada**:
1.  **Banco de Dados**: Utilizar **MySQL 8.x**, conforme especificado no PRD.
2.  **Framework de Acesso a Dados**: Utilizar **Spring Data JPA** para abstrair as operações de banco de dados.
3.  **Separação de Modelos**: Em linha com a Clean Architecture, haverá uma separação estrita entre as entidades de domínio e as entidades de persistência:
    *   **Entidades de Domínio**: Classes puras em Kotlin na camada `domain`, sem anotações JPA.
    *   **Entidades de Persistência**: Classes em Kotlin na camada `infrastructure`, anotadas com `@Entity` do JPA, que representam a estrutura das tabelas do banco.
    *   **Mapeamento**: A camada de repositório (Adapter) será responsável por mapear as entidades de domínio para as entidades de persistência antes de salvar, e vice-versa ao buscar dados.

**Consequências**:
*   **Positivas**:
    *   **Desacoplamento do Domínio**: A lógica de negócio permanece completamente ignorante sobre como os dados são persistidos, permitindo que a estratégia de persistência seja trocada no futuro com impacto mínimo.
    *   **Produtividade**: Spring Data JPA simplifica drasticamente a implementação de operações CRUD, gerando queries automaticamente a partir de interfaces de repositório.
    *   **Consistência Transacional**: O Spring facilita o gerenciamento de transações (`@Transactional`), garantindo a atomicidade das operações e a consistência dos dados, um ponto crítico para evitar condições de corrida na alocação de vagas.
*   **Negativas**:
    *   **Complexidade de Mapeamento**: A necessidade de mapear entre dois modelos (domínio e persistência) adiciona uma camada de complexidade e código boilerplate. Ferramentas como MapStruct podem ser consideradas se o mapeamento se tornar complexo.

**Dependências**: Nenhuma.

**Referência ao PRD**: Seção 5 (Requisitos Técnicos), Seção 2.1 (Gestão de vagas). A decisão suporta a necessidade de persistir o estado da garagem de forma robusta.
