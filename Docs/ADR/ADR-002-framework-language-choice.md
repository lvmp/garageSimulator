# ADR-002: Escolha de Linguagem e Framework

**Status**: Aceito

**Contexto**: O PRD estabelece como requisito técnico o uso de Java 21 ou Kotlin 2.1.x, combinado com o framework Spring ou Micronaut. Uma decisão explícita é necessária para padronizar o desenvolvimento, otimizar a produtividade e garantir a consistência do código-fonte.

**Complexidade Estimada**: Baixa. A decisão se baseia em requisitos predefinidos e nas melhores práticas atuais do mercado.

**Decisão Tomada**: A stack de desenvolvimento será **Kotlin 2.1.x** em conjunto com **Spring Boot 3.x** e **Gradle com Kotlin DSL**.
*   **Kotlin**: Escolhido por sua sintaxe concisa, interoperabilidade total com Java, e funcionalidades de segurança como null-safety, que reduzem a probabilidade de `NullPointerExceptions`.
*   **Spring Boot**: Selecionado devido ao seu robusto ecossistema, ampla documentação, comunidade ativa e funcionalidades maduras como Spring Web, Spring Data e um sistema de injeção de dependências poderoso, que se integra perfeitamente com a Clean Architecture.
*   **Gradle com Kotlin DSL**: Escolhido como ferramenta de build por sua flexibilidade e integração superior com projetos Kotlin.

**Consequências**:
*   **Positivas**:
    *   **Produtividade do Desenvolvedor**: A combinação de Kotlin e Spring Boot permite um desenvolvimento rápido e expressivo.
    *   **Ecossistema Maduro**: Acesso a uma vasta gama de bibliotecas e soluções para problemas comuns (segurança, acesso a dados, etc.).
    *   **Código Mais Seguro e Conciso**: As funcionalidades do Kotlin contribuem para um código mais limpo e menos propenso a erros.
*   **Negativas**:
    *   **Overhead do Framework**: Para um projeto simples, o Spring Boot pode introduzir um certo overhead em termos de consumo de memória e tempo de inicialização, embora isso seja mínimo para o escopo atual.

**Dependências**: A equipe de desenvolvimento deve ter conhecimento em Kotlin e no ecossistema Spring.

**Referência ao PRD**: Seção 5 (Requisitos Técnicos). Esta decisão atende diretamente à restrição de tecnologias imposta pelo documento.
