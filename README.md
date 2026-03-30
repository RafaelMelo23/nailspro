# SaaS de agendamento para salões/manicures

[![Java 21](https://img.shields.io/badge/Java-21-orange?logo=java)](https://jdk.java.net/21/)
[![Spring Boot 3.4](https://img.shields.io/badge/Spring%20Boot-3.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue?logo=docker)](https://www.docker.com/)

Plataforma SaaS (Software as a Service) de alta performance para estúdios de unhas e salões de beleza. 
O projeto resolve a complexidade de gestão de agenda e retenção de clientes através de automação inteligente, 
oferecendo uma solução completa desde o agendamento até o follow-up pós-atendimento.

---

## 🚀 Diferenciais de Engenharia

Este sistema foi construído com foco em escalabilidade e maturidade técnica, aplicando padrões de mercado para sistemas corporativos:

- **Arquitetura Multi-Tenant com Isolamento Físico de Dados**: Utiliza AOP (Aspect Oriented Programming) via `TenantAspect` para garantir que cada salão acesse apenas seus próprios dados, com resolução dinâmica por Header ou Subdomínio.
- **Núcleo Reativo & Orientado a Eventos**: Implementação de listeners transacionais (`@TransactionalEventListener`) para processamento assíncrono de métricas, auditoria e mensageria sem impactar a latência do usuário.
- **Inteligência de Retenção (CRM Predictor)**: Algoritmo que calcula automaticamente a data ideal de retorno com base no "Maintenance Interval" de cada serviço (ex: alongamento de unhas vs. esmaltação simples).
- **Integração Real-time com Evolution API**: Gestão completa de instâncias de WhatsApp, permitindo pareamento via QR Code ou Pairing Code com feedback em tempo real via **SSE (Server-Sent Events)**.
- **Segurança de Nível Bancário**: Spring Security com JWT, Refresh Tokens seguros via Cookies HTTP-Only e fluxos de recuperação de senha com tokens de propósito único.

---

## 🛠️ Mapa de Funcionalidades Completo

### 1. Gestão de Agendamento (Booking Engine)
- **Janelas Inteligentes**: Definição de antecedência permitida com base no nível de fidelidade do cliente (Configurável por Salon Profile).
- **Prevenção de Conflitos**: Validação rigorosa de horários de trabalho, intervalos de descanso e bloqueios manuais do profissional.
- **Multi-Service Booking**: Suporte a agendamentos com múltiplos "Add-ons" (serviços extras), recalculando automaticamente a duração total.
- **Lock Pessimista**: Proteção contra agendamentos simultâneos no mesmo slot através de bloqueio em nível de banco de dados.

### 2. Automação de CRM & Retenção
- **Previsão de Visita**: Geração de `RetentionForecast` no momento da finalização do atendimento.
- **Follow-up Automatizado**: Job diário que identifica clientes "sumidos" ou em período de manutenção e dispara convites personalizados.
- **Métricas de Fidelidade**: Cálculo automático de "Loyal Status" baseado no histórico de comparecimento para liberação de janelas preferenciais.

### 3. Painel Administrativo & Business Intelligence
- **Onboarding de Tenants**: Fluxo automatizado de criação de conta, perfil do salão e primeiro administrador.
- **Insights de Receita**: Dashboards com faturamento mensal, semanal, ticket médio e série histórica diária.
- **Auditoria de Clientes**: Perfil 360º do cliente com total gasto, taxa de cancelamento, histórico de faltas (No-show) e última visita.
- **Gestão de Serviços**: CRUD completo de serviços com controle de status ativo/inativo e intervalos de manutenção customizados.

### 4. Gestão de Profissionais
- **Work Schedules**: Configuração granular de dias de trabalho e horários (entrada, saída, almoço).
- **Schedule Blocks**: Bloqueios rápidos de agenda para imprevistos ou folgas.
- **Profile Management**: Upload de fotos de perfil e gestão de informações profissionais.

### 5. Mensageria & Notificações
- **WhatsApp Lifecycle**: Notificações automáticas de confirmação, lembretes de agendamento (15 min antes) e mensagens de retenção.
- **Retry Mechanism**: Controle de tentativas de envio de mensagens com log de erros e status detalhado (Sent, Failed, Pending).
- **Email System**: Suporte a notificações via E-mail (Resend/SMTP) com controle de quota para evitar spam.

---

## 🏗️ Stack Tecnológica

- **Backend**: Java 21, Spring Boot 3.4.
- **Persistência**: Spring Data JPA, PostgreSQL 15, Flyway.
- **Segurança**: Spring Security, JWT (Auth0), BCrypt.
- **Integrações**: Evolution API (WhatsApp), Resend (E-mail), Sentry (Observabilidade).
- **Infraestrutura**: Docker & Docker Compose, GitHub Actions (CI/CD Ready).
- **Testes**: JUnit 5, Mockito, Testcontainers.

---

## 🏛️ Destaques de Arquitetura (Clean Code & DDD)

O projeto é organizado seguindo princípios de **Screaming Architecture**:
- `application`: Casos de uso puros (UseCases) que orquestram a lógica sem depender de frameworks.
- `domain`: O coração do negócio, contendo as entidades ricas, eventos de domínio e regras de política.
- `infrastructure`: Adaptadores para o mundo externo (Controllers, Repositories, API Clients).
- `shared/tenant`: Lógica transversal para multi-tenancy.

---

## 🚦 Execução Local

```bash
# Clone e configure o .env
cp .env.example .env

# Build e Up
./mvnw clean package -DskipTests
docker compose up -d --build
```

Acesse a documentação interativa em: `http://localhost:8080/swagger-ui/index.html`