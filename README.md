Gestão Inteligente para Nail Designers
====================================================

O sistema é um SaaS multi-tenant desenvolvido para transformar 
a gestão de manicures e salões de beleza. Indo além de um simples 
sistema de agendamento, o projeto foca na **retenção** de clientes e manejar 
de maneira inteligente a agenda dos profissionais, podendo priorizar
clientes recorrentes, por exemplo, além de módulos de
**análise de faturamento e clientes**, 
utilizando uma arquitetura moderna e escalável.

🚀 O que mudou? (Últimas Atualizações)
--------------------------------------

*   **Feature de Retenção** Agora o sistema prevê quando um cliente deve retornar e envia um convite de agendamento com antecedência.

*   **Módulo de CRM**: Módulo de auditoria do cliente, com informações de total gasto no salão, faltas, cancelamentos, etc.

* **Lógica de agendamento**: Agora é possível que o tenant priorize cliente leais, aumentando a agenda visível para esses, e diminuindo-a para novos clientes, facilitando o manejo da agenda e priorizando recorrência.

*   **Dashboards de Auditoria:** Implementação de serviços de auditoria para faturamento diário e métricas de comportamento do cliente.

*   **Refatoração DDD:** Evolução da estrutura de pacotes para um Domain-Driven Design pragmático, reduzindo acoplamento e melhorando a evolução contínua do sistema.

*   **Concorrência e Desempenho:** Adição de mecanismos de _locking_ no fluxo de agendamento e adição de arquitetura orientada a eventos.


🛠 Tech Stack
-------------

*   **Java 21 LTS**

*   **Spring Boot 3.4+** 

*   **PostgreSQL**

*   **Spring Security + JWT**

*   **Evolution API**

*   **Docker & Docker Compose**


🌟 Diferenciais de Engenharia
-----------------------------

### 🏗 Arquitetura & Design Patterns

*   **Pragmatic DDD:** Organização por contextos delimitados, separando regras de domínio de detalhes de infraestrutura.

*   **Event-Driven Architecture:** Utilização de eventos para disparar cálculos de métricas e geração de previsões de retenção após a conclusão de atendimentos.

*   **Strategy Pattern:** Processamento dinâmico de webhooks da Evolution API, facilitando a expansão para novos tipos de mensagens sem alterar o código existente (Open/Closed Principle).

*   **Async Processing:** Agendamento de mensagens e tarefas de retenção utilizando executores configurados para não bloquear a thread principal.


### 📈 Inteligência de Negócio

*   **Retention Forecast:** Motor que calcula a data ideal de retorno da cliente e automatiza o lembrete via WhatsApp.

*   **Salon Revenue Auditing:** Monitoramento diário de faturamento.

*   **Multi-tenancy:** Isolamento lógico que permite que o sistema escale como um serviço para múltiplos salões simultaneamente.


⚙️ Como Executar

**Nota:** O projeto 
encontra-se em desenvolvimento backend. 
Atualmente, a interação é feita exclusivamente via API.

----------------

O projeto está totalmente containerizado para facilitar o setup inicial.

### ✅ Pré-requisitos

*   Docker & Docker Compose

*   Git


### 📥 Passo a Passo

1.  git clone https://github.com/RafaelMelo23/nailspro.git

2.  cd nailspro

3.  **Configurar Variáveis de Ambiente** O projeto utiliza variáveis de ambiente para segurança. Certifique-se de configurar o arquivo .env (baseado no example.env).

4.  docker compose up -d