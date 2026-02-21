Plataforma SaaS com foco em backend para estúdios de unhas e salões de beleza, orientada a dois resultados:
- Aumentar a ocupação da agenda com agendamento inteligente e automações
- Melhorar retenção e visibilidade de receita com CRM + analytics

## Por Que Este Projeto Se Destaca
- Não é apenas uma API de agendamento: combina agenda, inteligência de cliente, previsão de retenção, automação via WhatsApp e dashboards de negócio.
- Foi desenhado para expansão SaaS, com onboarding tenant-aware, claim de tenant no JWT e dados orientados por tenant.
- Utiliza fluxos orientados a eventos, então ações operacionais (agendar, finalizar, cancelar, falta) disparam automações e atualização de métricas.

## Mapa de Funcionalidades (Por Módulo do Sistema)

### 1. Experiência de Agendamento do Cliente
- Agendamento e cancelamento de atendimento (`/api/v1/booking`)
- Consulta de horários disponíveis com janela calculada (`/api/v1/booking/times`)
- Política inteligente de agendamento
- Clientes fiãis podem receber uma janela maior de agendamento
- Clientes novos podem ter uma janela menor
- Data recomendada de retorno com base no intervalo de manutenção do serviço
- Prevenção de conflito
- Lock pessimista no fluxo de reserva
- Validação de conflitos com agenda/bloqueios do profissional

### 2. Operação de Agenda do Profissional
- CRUD de horários de trabalho (`/api/v1/schedule`)
- Gestão de bloqueios de agenda (`/api/v1/schedule/block`)
- Visualização da agenda diária do profissional (`/professional/appointments`)
- Ações de ciclo de vida do atendimento (confirmar, finalizar, cancelar, faltou) com eventos de domínio

### 3. Admin, CRM e Insights
- Endpoint de onboarding para novo salão (`/api/internal/onboard`)
- Gestão de perfil do salão (`/api/v1/admin/salon/profile`)
- Gestão de serviços do salão (`/api/v1/admin/salon/service`)
- Gestão de clientes e status (`/api/v1/admin/client`)
- Insight de CRM por cliente (`/api/v1/admin/insight/clients/{clientId}`)
- Total gasto
- Atendimentos concluídos
- Cancelamentos
- Faltas
- Última visita
- Auditoria de atendimentos por cliente (`/api/v1/admin/appointments/users/{userId}`)
- Dashboard de receita (`/api/v1/admin/insight/salon/revenue`)
- Receita mensal
- Receita semanal
- Ticket médio
- Série diária para gráfico

### 4. Mensageria e Automação de Retenção
- Integração com WhatsApp via Evolution API
- Envio de confirmação apãs commit da tansação de agendamento
- Scheduler de lembretes a cada 15 minutos para atendimentos próximos
- Geração de previsão de retenção apãs finalização do atendimento
- Job diário de follow-up para clientes no período previsto de retorno
- Rastreamento de falhas de envio para monitoramento/retentativa

### 5. Operação em Tempo Real
- Canal SSE de inscriãão (`/api/v1/notifications/subscribe`)
- Atualização em tempo real de QR Code para pareamento do WhatsApp
- Notificações em tempo real de conexão/desconexão para o dono do salão

### 6. Segurança e Controle de Acesso
- Spring Security + JWT com claims de papel e tenant
- Suporte a cookie de autenticação HTTP-only e secure
- Autorização por papãis para ãreas admin/profissional
- Tokens de reset de senha com propósito e expiração

## Destaques de Engenharia
- Organização pragmática em DDD (`application`, `domain`, `infrastructure`)
- Núcleo orientado a eventos com listeners transacionais
- Processamento assíncrono para mensageria e listeners de métricas
- Modelagem multi-tenant em entidades e contexto de requisição
- Ambiente local containerizado (app + PostgreSQL + Evolution API + Evolution DB)

## Stack
- Java 21
- Spring Boot 3.4.x
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Docker + Docker Compose
- Evolution API (WhatsApp)

## Execução Local

### Pré-requisitos
- Docker
- Docker Compose
- Maven (ou `./mvnw`)

### Ambiente
- Ajuste os valores do `.env.example` conforme necessário (`EVO_DB_PASSWORD`, `RESEND_API_KEY`)
- Configuraçães principais da aplicação em `src/main/resources/application.properties`

### Build e Subida
```bash
./mvnw clean package -DskipTests
docker compose up -d --build
```

Serviços:
- App: `http://localhost:8080 (Sem interface de usuário)`
- PostgreSQL: `localhost:5432`
- Evolution API: `http://localhost:8081`
- Evolution PostgreSQL: `localhost:5433`

## Status Atual
- O backend é o foco principal e segue em evolução ativa.
- Implementação API-first com arquitetura pronta para produção, enquanto o client apenas tem algumas views validadas.

## Valor para Recrutadores e Empresas
- Demonstra visão de negócio, não apenas implementação CRUD
- Mostra capacidade de desenhar políticas de domínio (agendamento por fidelidade, previsão de retenção)
- Comprova integração com provedores externos de mensageria e canais em tempo real
- Aplica padrões escaláveis de backend: eventos, assíncrono, serviços modulares, segurança por papéis e entrega containerizada replicável
