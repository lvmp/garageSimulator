# Tarefa 3: Implementação da Camada de Aplicação

**ID**: 03
**Related ADRs**: ADR-001

**Descrição**: Orquestrar a lógica de domínio através de Casos de Uso e definir os contratos (Ports) para as dependências externas, efetivando a inversão de dependência.

---

### Sub-tarefa 3.1: Definição dos Ports (Interfaces de Repositório)

*   **Descrição**: Criar as interfaces que definem os contratos de persistência.
*   **Patterns**: **Repository Pattern** (contrato), **Port**.
*   **Princípio SOLID**: **DIP**.
*   **Implementação**:
    1.  Crie a interface `ParkingSessionRepositoryPort`.
    2.  Crie a interface `GarageRepositoryPort`.
*   **Testes**: Não aplicável para interfaces.

### Sub-tarefa 3.2: Implementação dos Casos de Uso

*   **Descrição**: Criar as classes de Caso de Uso que implementam as funcionalidades do sistema.
*   **Patterns**: **Command** (`Handle...UseCase`), **Query** (`Get...UseCase`).
*   **Princípio SOLID**: **SRP**.
*   **Implementação**:
    1.  `HandleVehicleEntryUseCase`: Lógica para entrada de veículo, incluindo cálculo de preço dinâmico e verificação de lotação.
    2.  `HandleVehicleExitUseCase`: Lógica para saída de veículo, incluindo cálculo de custo e liberação de vaga.
    3.  `GetRevenueUseCase`: Lógica para consulta de faturamento.
*   **Testes Unitários**:
    *   Para cada Caso de Uso, crie uma classe de teste correspondente (ex: `HandleVehicleEntryUseCaseTest`).
    *   Utilize um framework de mock (MockK, Mockito) para simular o comportamento dos `RepositoryPort`.
    *   Teste todos os cenários de sucesso e de falha (ex: garagem cheia, sessão não encontrada).

### Critérios de Aceitação
*   As interfaces (Ports) para os repositórios estão definidas.
*   Todos os casos de uso estão implementados, injetando as interfaces dos ports.
*   Os testes unitários para os casos de uso cobrem a lógica de negócio e passam com sucesso.