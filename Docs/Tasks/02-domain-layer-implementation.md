# Tarefa 2: Implementação da Camada de Domínio

**ID**: 02
**Related ADRs**: ADR-001, ADR-003

**Descrição**: Foco na criação das entidades de negócio puras, que contêm a lógica de negócio mais crítica e são o coração da aplicação. As classes aqui criadas não devem ter nenhuma dependência de frameworks.

---

### Sub-tarefa 2.1: Entidades `Vehicle`, `Sector`, e `ParkingSpot`

*   **Descrição**: Criar as classes de domínio que representam os conceitos fundamentais da garagem.
*   **Patterns**: **Value Object** (`Vehicle`), **Entity** (`Sector`, `ParkingSpot`).
*   **Princípio SOLID**: **SRP**. Cada classe tem sua responsabilidade única e bem definida.
*   **Implementação**:
    1.  `data class Vehicle(val licensePlate: String)`
    2.  `data class Sector(val id: Long, val name: String, val basePrice: Double, val maxCapacity: Int)`
    3.  `data class ParkingSpot(val id: Long, val sector: Sector, var isOccupied: Boolean = false)` com métodos `occupy()` e `vacate()`.
*   **Testes Unitários**:
    *   Crie testes para `ParkingSpot` para validar a lógica dos métodos `occupy()` e `vacate()`, incluindo o tratamento de exceção ao tentar ocupar uma vaga já ocupada.

### Sub-tarefa 2.2: Entidade Agregadora `ParkingSession`

*   **Descrição**: Criar a entidade `ParkingSession`, que funciona como a raiz do agregado, gerenciando o ciclo de vida de uma estadia.
*   **Patterns**: **Aggregate Root**.
*   **Princípio SOLID**: **SRP** e **OCP** (no método de cálculo de custo).
*   **Implementação**:
    *   Crie a `data class ParkingSession` contendo `id`, `vehicle`, `parkingSpot`, `entryTime`, `exitTime`, `dynamicPricePercentage`, e `finalCost`.
    *   Implemente o método `calculateCost()`, que contém a lógica de negócio para a tarifação (30 min de gratuidade, hora cheia, preço dinâmico).
*   **Testes Unitários**:
    *   Crie `ParkingSessionTest.kt` com múltiplos cenários para o método `calculateCost()`:
        *   Duração menor que 30 minutos (custo zero).
        *   Duração de 31 minutos (custo de 1 hora).
        *   Duração de 2h15m (custo de 3 horas).
        *   Cenários com desconto e acréscimo do preço dinâmico.
        *   Tentativa de calcular o custo antes da saída do veículo.

### Critérios de Aceitação
*   Todas as entidades de domínio estão criadas como classes puras em Kotlin.
*   A lógica de negócio crítica está implementada dentro das entidades.
*   Todos os testes unitários para a camada de domínio passam com sucesso.