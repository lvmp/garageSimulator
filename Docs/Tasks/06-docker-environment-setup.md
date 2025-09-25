# Tarefa 6: Configuração do Ambiente Docker

**ID**: 06
**Related ADRs**: ADR-004

**Descrição**: Finalizar a configuração do projeto criando os artefatos Docker para garantir um ambiente de desenvolvimento e execução consistente e automatizado.

---

### Sub-tarefa 6.1: Criação do `Dockerfile`

*   **Descrição**: Criar um `Dockerfile` multi-stage para construir uma imagem otimizada da aplicação.
*   **Patterns**: **Containerization**, **Multi-stage Build**.
*   **Implementação**: Siga a estrutura de dois estágios (Build e Runtime) para criar uma imagem final enxuta.
*   **Testes**: Validar com o comando `docker build .`.

### Sub-tarefa 6.2: Criação do `docker-compose.yml`

*   **Descrição**: Orquestrar a aplicação, o banco de dados e o simulador.
*   **Patterns**: **Infrastructure as Code (IaC)**.
*   **Implementação**: Defina os três serviços (`mysql-db`, `garage-app`, `garage-simulator`) no arquivo `docker-compose.yml`, configurando as redes, volumes, portas e variáveis de ambiente necessárias.
*   **Testes**: Validar com o comando `docker-compose up --build`, verificando se todos os contêineres iniciam e se comunicam corretamente.

### Critérios de Aceitação
*   O `Dockerfile` constrói a imagem da aplicação com sucesso.
*   O `docker-compose.yml` sobe todo o ambiente sem erros.
*   A aplicação se conecta ao banco de dados e o simulador envia eventos para a aplicação.