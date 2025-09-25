# ADR-001: Adoção do Padrão Clean Architecture

**Status**: Aceito

**Contexto**: O PRD descreve um sistema com regras de negócio bem definidas (cálculo de preço, gestão de lotação) que são o coração do produto. Para garantir a longevidade, testabilidade e manutenibilidade do software, é crucial que essas regras de negócio sejam isoladas de detalhes de infraestrutura como banco de dados, frameworks web e outros agentes externos. Uma arquitetura em camadas simples poderia levar a um acoplamento indesejado entre a lógica de negócio e o framework (ex: anotações JPA em entidades de domínio).

**Complexidade Estimada**: Média. A implementação inicial exige uma configuração mais cuidadosa dos módulos e do fluxo de dependências, além da criação de mapeadores entre as camadas.

**Decisão Tomada**: Adotar a **Clean Architecture**. A estrutura do código será organizada em camadas concêntricas, com a regra de dependência apontando sempre para o centro:
1.  **Domain**: Contém as entidades de negócio puras e a lógica de domínio mais crítica. Não possui dependências de nenhuma outra camada.
2.  **Application**: Contém os Casos de Uso (Use Cases) que orquestram a lógica de aplicação e as interfaces (Ports) que definem os contratos com o mundo exterior (ex: repositórios).
3.  **Infrastructure**: Contém as implementações concretas (Adapters) das interfaces da camada de aplicação. Isso inclui Controllers, Repositórios JPA, Gateways para outros serviços, etc.
4.  **Configuration**: Ponto de entrada da aplicação, configuração do Spring e do `docker-compose.yml`.

**Consequências**:
*   **Positivas**:
    *   **Alta Testabilidade**: A lógica de negócio e os casos de uso podem ser testados em isolamento, sem a necessidade de iniciar um banco de dados ou um servidor web.
    *   **Independência de Framework**: A camada de domínio é completamente agnóstica ao Spring, JPA ou qualquer outra biblioteca, podendo ser reutilizada ou migrada com facilidade.
    *   **Manutenibilidade**: A separação clara de responsabilidades torna o código mais fácil de entender, modificar e estender.
*   **Negativas**:
    *   **Maior Boilerplate**: É necessário criar classes e interfaces adicionais (DTOs, mappers, ports) para mediar a comunicação entre as camadas, o que pode aumentar a quantidade de código para funcionalidades simples.
    *   **Curva de Aprendizagem**: A equipe pode precisar de um tempo para se familiarizar com o fluxo de controle e a inversão de dependência propostos pela arquitetura.

**Dependências**: Nenhuma.

**Referência ao PRD**: Seção 5 (Requisitos Técnicos), Seção 4 (Regras de Negócio). A arquitetura foi escolhida para suportar a implementação robusta e testável dessas seções.
