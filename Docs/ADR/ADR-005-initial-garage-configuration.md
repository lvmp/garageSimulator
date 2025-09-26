# ADR-005: Carregamento Inicial da Configuração da Garagem

**Status**: Proposto

**Contexto**: O PRD especifica que o sistema deve realizar o "Carregamento inicial da configuração da garagem" buscando dados via `GET /garage` do simulador. Esta configuração inclui os setores e as vagas disponíveis. Para que a aplicação possa gerenciar a ocupação e o cálculo de preços dinâmicos, ela precisa ter conhecimento da estrutura da garagem desde o seu início. Este é um passo fundamental para inicializar o estado da aplicação.

**Complexidade Estimada**: Média.

**Decisão Tomada**: A aplicação irá, durante sua inicialização (startup), realizar uma chamada HTTP `GET` para o endpoint `/garage` do simulador. A resposta JSON será parseada e os dados de `Sector` e `ParkingSpot` serão persistidos no banco de dados. Este processo será executado uma única vez na inicialização da aplicação.

**Consequências**:
*   **Positivas**:
    *   A aplicação terá seu estado inicial de garagem configurado automaticamente, conforme o PRD.
    *   Garante que as regras de negócio de ocupação e preço dinâmico possam ser aplicadas desde o primeiro evento.
    *   Reduz a necessidade de configuração manual da garagem.
*   **Negativas**:
    *   A aplicação terá uma dependência externa (o simulador) durante sua inicialização. Se o simulador não estiver disponível ou o endpoint `/garage` falhar, a aplicação não conseguirá iniciar corretamente.
    *   Necessidade de tratamento de erros robusto para a chamada HTTP e o parsing da resposta.

**Dependências**:
*   O simulador deve estar disponível e respondendo ao `GET /garage` durante a inicialização da aplicação.
*   A camada de persistência deve estar funcional para salvar os dados de `Sector` e `ParkingSpot`.

**Referência ao PRD**: Seção 2.1 (Funcionalidades Principais - 1. Carregamento inicial da configuração da garagem), Seção 6.2 (Configuração da Garagem - GET /garage).
