# Tarefa 7: Carregamento Inicial da Configuração da Garagem

**ID**: T07
**Related ADRs**: ADR-005

**Descrição**: Implementar a funcionalidade de carregamento inicial da configuração da garagem, buscando os dados do simulador via HTTP e persistindo-os no banco de dados. Esta tarefa garante que a aplicação tenha conhecimento da estrutura da garagem (setores e vagas) desde o seu startup.

---

### Sub-tarefa 7.1: Criar DTOs para a Resposta do `/garage`

*   **Descrição**: Definir as classes de DTO que representam a estrutura da resposta JSON do endpoint `GET /garage` do simulador.
*   **Patterns**: **Data Transfer Object (DTO)**.
*   **Implementação**:
    *   Crie `GarageConfigDTO.kt` com `List<SectorDTO>` e `List<SpotDTO>`.
    *   Crie `SectorDTO.kt` com `sector`, `basePrice`, `max_capacity`.
    *   Crie `SpotDTO.kt` com `id`, `sector`, `lat`, `lng`.
*   **Testes**: Não aplicável para DTOs simples.

### Sub-tarefa 7.2: Implementar Cliente HTTP para o Simulador

*   **Descrição**: Criar um componente na camada de infraestrutura responsável por fazer a chamada HTTP `GET` para o simulador.
*   **Patterns**: **Adapter** (para o serviço externo), **Client**.
*   **Princípio SOLID**: **DIP** (o cliente implementará uma interface definida na camada de aplicação, se necessário, ou será injetado diretamente em um serviço de infraestrutura).
*   **Implementação**:
    *   Crie uma interface `SimulatorClientPort` na camada de aplicação (ex: `application.port`).
    *   Crie a implementação `SimulatorHttpClient` na camada de infraestrutura (ex: `infrastructure.client`), utilizando `RestTemplate` ou `WebClient` do Spring para fazer a chamada HTTP.
    *   O método principal será `getGarageConfiguration(): GarageConfigDTO`.
*   **Testes Unitários**:
    *   Crie `SimulatorHttpClientTest.kt`.
    *   Utilize `MockRestServiceServer` (para `RestTemplate`) ou `MockWebServer` (para `WebClient`) para mockar a resposta HTTP do simulador.
    *   Teste o parsing correto da resposta JSON para `GarageConfigDTO`.

### Sub-tarefa 7.3: Implementar Caso de Uso `LoadInitialGarageConfigurationUseCase`

*   **Descrição**: Criar um caso de uso que orquestra a busca da configuração da garagem e sua persistência.
*   **Patterns**: **Command**.
*   **Princípio SOLID**: **SRP**.
*   **Implementação**:
    *   Crie `LoadInitialGarageConfigurationUseCase` na camada de aplicação.
    *   Injete `SimulatorClientPort` e `GarageRepositoryPort`.
    *   O método `execute()` chamará o cliente para obter a configuração e, em seguida, usará o `GarageRepositoryPort` para salvar os `Sector` e `ParkingSpot` no banco de dados.
*   **Testes Unitários**:
    *   Crie `LoadInitialGarageConfigurationUseCaseTest.kt`.
    *   Mocke `SimulatorClientPort` e `GarageRepositoryPort`.
    *   Teste o fluxo completo: chamada ao cliente, mapeamento para entidades de domínio e salvamento no repositório.

### Sub-tarefa 7.4: Integrar Carregamento na Inicialização da Aplicação

*   **Descrição**: Garantir que o caso de uso de carregamento inicial seja executado durante o startup da aplicação.
*   **Implementação**:
    *   Utilize `@EventListener(ApplicationReadyEvent::class)` em um componente Spring (ex: na `ApplicationConfig` ou em um novo `@Component`) para invocar o `LoadInitialGarageConfigurationUseCase` após a aplicação estar pronta.
*   **Testes de Integração**:
    *   Crie um teste de integração que inicie o contexto Spring e verifique se os dados da garagem foram persistidos após o startup.

### Critérios de Aceitação
*   A aplicação inicia e busca a configuração da garagem do simulador.
*   Os dados de setores e vagas são persistidos corretamente no banco de dados.
*   Todos os testes unitários e de integração passam com sucesso.
