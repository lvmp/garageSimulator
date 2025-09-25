# Tarefa 5: Implementação do Adaptador Web (API REST)

**ID**: 05
**Related ADRs**: ADR-002

**Descrição**: Expor as funcionalidades do sistema para o mundo externo através de uma API REST, criando os controllers e os objetos de transferência de dados (DTOs).

---

### Sub-tarefa 5.1: Criação dos DTOs

*   **Descrição**: Definir as classes de dados para as requisições e respostas da API.
*   **Patterns**: **Data Transfer Object (DTO)**.
*   **Implementação**: Crie as `data classes` para os eventos do webhook e para a consulta de faturamento.

### Sub-tarefa 5.2: Implementação dos REST Controllers

*   **Descrição**: Criar os endpoints da API.
*   **Patterns**: **Adapter**.
*   **Princípio SOLID**: **SRP**.
*   **Implementação**:
    1.  `WebhookController`: Com um endpoint `@PostMapping("/webhook")` para receber os eventos de entrada e saída de veículos.
    2.  `RevenueController`: Com um endpoint `@GetMapping("/revenue")` para a consulta de faturamento.
    *   Ambos os controllers devem injetar e invocar os Casos de Uso apropriados.
*   **Testes de Controller**:
    *   Crie testes para os controllers usando `@WebMvcTest`.
    *   Utilize `@MockBean` para mockar os Casos de Uso.
    *   Valide o roteamento, a desserialização/serialização de DTOs e os códigos de status HTTP para cenários de sucesso e de erro (Bad Request).

### Critérios de Aceitação
*   Os DTOs para a API estão definidos.
*   Os controllers estão implementados, delegando a lógica para os casos de uso.
*   Os testes com `@WebMvcTest` passam com sucesso.