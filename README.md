Tech Stack
-

*   **Java 21 LTS:** Utilizando o que há de mais moderno na linguagem para performance e legibilidade.
    
*   **Framework:** Spring Boot 3.4.1 (Web, Security, JPA).
    
*   **Persistência:** Spring Data JPA / Hibernate para mapeamento objeto-relacional eficiente.
    
*   **Banco de Dados:** PostgreSQL, em função da sua posição atual como melhor opção open-source, enquanto outras opções se tornam produtos comerciais.
    
*   **Segurança:** Spring Security + Java-JWT (Auth0) para autenticação e autorização stateless via tokens.
    
*   **Real-time:** SSE (Server-Sent Events) para monitoramento de agendamentos e atualizações de dashboard em tempo real.
    
*   **Integração:** Evolution API (Automação de WhatsApp via Webhooks) para comunicação direta com clientes.
    
*   **Validation:** Jakarta Validation para garantir a integridade dos dados na camada de entrada.
    
Diferenciais do Projeto
-

*   **Multi-tenancy:** Arquitetura preparada para o isolamento lógico de dados por salão, garantindo que cada cliente tenha seu ambiente de dados protegido e independente.
    
*   **Pragmatic DDD (Domain-Driven Design):** Implementação de uma estrutura de pastas organizada por camadas (Application, Domain, Infrastructure). O foco é manter o domínio isolado de detalhes técnicos, facilitando a manutenção e evolução da regra de negócio sem a complexidade excessiva de implementações puristas, garantindo velocidade no desenvolvimento.
    
*   **Event-Driven & Strategy Pattern:** O sistema utiliza webhooks para reagir a eventos externos da Evolution API. Foi aplicado o padrão Strategy para o processamento dessas mensagens, eliminando o acoplamento excessivo e facilitando a extensão para novos tipos de eventos sem violar o princípio Open/Closed.
    
*   **Clean Code & Robustez:** Código focado em bons princípios, com tratamento de exceções centralizado e mapeamento de dados via DTOs.
    Aqui está a versão em português, profissional e pronta para colocar no README:

🚀 Como Executar a Aplicação
-

Este projeto utiliza Docker Compose para orquestração do ambiente.

✅ Pré-requisitos

Certifique-se de ter instalado:

- Docker
- Docker Compose
- Git

Verifique se está tudo instalado:

* docker --version
* docker compose version

📥 1. Clonar o Repositório

- git clone https://github.com/RafaelMelo23/nailspro.git
- cd nailspro

🐳 2. Executar com Docker Compose

O projeto utiliza o seguinte arquivo:

**docker-compose.gh.yml**

Para subir a aplicação:

** docker compose -f docker-compose.gh.yml up --build**
