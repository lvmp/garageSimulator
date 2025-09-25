# ADR-004: Estratégia de Infraestrutura e Ambiente Local

**Status**: Aceito

**Contexto**: Para desenvolver e testar a aplicação, é necessário executar três componentes de forma integrada: a aplicação backend, o banco de dados MySQL e o simulador de eventos de garagem. Gerenciar esses processos manualmente é ineficiente, propenso a erros e dificulta a padronização do ambiente entre os desenvolvedores.

**Complexidade Estimada**: Baixa.

**Decisão Tomada**: Utilizar o **Docker Compose** para definir e gerenciar todo o ambiente de desenvolvimento local. Um único arquivo `docker-compose.yml` na raiz do projeto irá orquestrar os seguintes serviços:
1.  `mysql-db`: Um contêiner executando a imagem oficial do MySQL 8.x.
2.  `garage-app`: O contêiner da nossa aplicação Spring Boot, construído a partir de um `Dockerfile` local.
3.  `garage-simulator`: O contêiner do simulador (`cf0ntes0estapar/garage-sim:1.0.0`), configurado com `network_mode: "host"` para que ele possa se comunicar com a porta `3003` exposta pela aplicação na máquina hospedeira.

**Consequências**:
*   **Positivas**:
    *   **Ambiente Reproduzível**: Garante que todos os desenvolvedores trabalhem com as mesmas versões e configurações de serviços, eliminando o clássico problema "funciona na minha máquina".
    *   **Simplicidade**: A inicialização de todo o ambiente é reduzida a um único comando (`docker-compose up`).
    *   **Isolamento**: Os serviços rodam em contêineres isolados, não interferindo com outras aplicações ou serviços instalados na máquina do desenvolvedor.
*   **Negativas**:
    *   **Consumo de Recursos**: A execução de múltiplos contêineres Docker consome mais RAM e CPU do que executar os processos nativamente.
    *   **Dependência do Docker**: Requer que todos os desenvolvedores tenham o Docker e o Docker Compose instalados e configurados em suas máquinas.

**Dependências**: Docker e Docker Compose devem ser pré-requisitos para o desenvolvimento do projeto.

**Referência ao PRD**: Seção 3.1 (Integração com simulador). A decisão visa facilitar e padronizar a execução do ambiente completo descrito no PRD.
