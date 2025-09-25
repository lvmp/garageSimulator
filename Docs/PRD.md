# 📄 Product Requirements Document (PRD)
**Produto:** Sistema Backend de Gerenciamento de Estacionamento  
**Responsável (PO):** Luiz Vinicius  
**Versão:** 1.0  
**Data:** Setembro/2025

---

## 1. Objetivo
Desenvolver um sistema backend simples para gerenciar um estacionamento. O sistema deve:
- Controlar **vagas disponíveis**.
- Gerenciar **entrada e saída de veículos**.
- **Calcular receita** com base em regras de negócio definidas.

### Contexto
- A garagem possui **um único grupo de cancelas** na entrada.
- Os **setores são divisões lógicas** (não físicas) para organização de vagas.
- A simulação será feita via um **container Docker** que envia eventos ao backend.

---

## 2. Escopo

### 2.1 Funcionalidades Principais
1. **Carregamento inicial da configuração da garagem**
    - Buscar e armazenar configuração (garagem + vagas) via `GET /garage`.

2. **Webhook de eventos de veículos**
    - Aceitar eventos `ENTRY`, `PARKED` e `EXIT` enviados pelo simulador.

3. **Gestão de vagas**
    - Marcar vagas como ocupadas ao receber evento `ENTRY`.
    - Marcar vagas como disponíveis ao receber evento `EXIT`.
    - Bloquear novas entradas quando a lotação for atingida.

4. **Cálculo de receita**
    - Implementar endpoint `GET /revenue` que retorna o faturamento por setor e data.
    - Considerar tarifas, gratuidades e regras de preços dinâmicos.

---

## 3. Requisitos Funcionais

1. **Integração com simulador**
    - Executar simulador via Docker:
      ```bash
      docker run -d --network="host" cf0ntes0estapar/garage-sim:1.0.0
      ```  
    - O simulador enviará eventos para `http://localhost:3003/webhook`.

2. **API REST**
    - `GET /revenue`
        - Entrada: data e setor.
        - Saída: receita total (`amount`), moeda (`currency` = BRL) e `timestamp`.

3. **Eventos do Webhook**
    - **ENTRY**: marca vaga como ocupada.
    - **PARKED**: indica posição do veículo (lat/lng).
    - **EXIT**: marca vaga como livre e calcula cobrança.

---

## 4. Regras de Negócio

1. **Gestão de vagas**
    - Entrada: só permitida se houver vaga disponível.
    - Saída: libera vaga + calcula cobrança.

2. **Cobrança**
    - Primeiros **30 minutos grátis**.
    - Após 30 minutos, cobrar **tarifa fixa por hora**, arredondando para cima.
    - Usar `basePrice` da garagem.

3. **Preço dinâmico (definido na entrada)**
    - Lotação < 25% → desconto **-10%**.
    - Lotação < 50% → preço **normal**.
    - Lotação < 75% → aumento **+10%**.
    - Lotação < 100% → aumento **+25%**.

4. **Lotação máxima**
    - Quando 100% cheio, **bloquear entradas** até saída de veículo.

---

## 5. Requisitos Técnicos

- **Tecnologias obrigatórias:**
    - Java 21 / Kotlin 2.1.x
    - Spring ou Micronaut
    - MySQL
    - Git para versionamento

- **Infraestrutura:**
    - Microsserviço RESTful
    - Persistência em banco de dados relacional
    - Suporte a execução local com simulador

---

## 6. Detalhes da API

### 6.1 Webhook – Recebimento de Eventos

- **Endpoint:** `POST http://localhost:3003/webhook`
- **Eventos suportados:**

**ENTRY**
```json
{
  "license_plate": "ZUL0001",
  "entry_time": "2025-01-01T12:00:00.000Z",
  "event_type": "ENTRY"
}
```

**PARKED**
```json
{
  "license_plate": "ZUL0001",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```

**EXIT**
```json
{
  "license_plate": "ZUL0001",
  "exit_time": "2025-01-01T12:00:00.000Z",
  "event_type": "EXIT"
}
```

**Resposta esperada**: ```HTTP 200.```

### 6.2 Configuração da Garagem

#### GET /garage
```json
{
  "garage": [
    {
      "sector": "A",
      "basePrice": 10.0,
      "max_capacity": 100
    }
  ],
  "spots": [
    {
      "id": 1,
      "sector": "A",
      "lat": -23.561684,
      "lng": -46.655981
    }
  ]
}
```

### 6.3 API do Projeto – Consulta de Receita
#### GET /revenue
**Request**
```json
{
  "date": "2025-01-01",
  "sector": "A"
}
```

**Response**
```json
{
  "amount": 0.00,
  "currency": "BRL",
  "timestamp": "2025-01-01T12:00:00.000Z"
}
```

## 7. Critérios de Avaliação

- Clareza e estrutura do código.
- Uso correto de REST e banco de dados.
- Implementação correta das regras de negócio.
- Tratamento de eventos e consistência nos cálculos.
- Testes básicos e tratamento de erros.

## 8. Fora do Escopo
- Interface gráfica (frontend).
- Relatórios complexos além de /revenue.
- Integração com meios de pagamento reais.