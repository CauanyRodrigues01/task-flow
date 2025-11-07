# 4. Estrutura Unificada do Projeto (Polyrepo)

A estrutura do projeto adota uma abordagem Polyrepo dentro de um único repositório Git, com diretórios distintos para `taskflow-backend`, `taskflow-frontend` e um diretório `shared` para tipos TypeScript, garantindo separação de responsabilidades e compartilhamento de código.

```
TaskFlow/
├── taskflow-backend/           # Aplicação Backend (Java/Spring Boot)
│   ├── src/main/java/com/taskflow/
│   │   ├── activityhistory/    # Módulo para histórico de atividades
│   │   ├── comment/            # Módulo para comentários
│   │   ├── config/             # Configurações globais (CORS, Swagger)
│   │   ├── dashboard/          # Módulo para funcionalidades do dashboard
│   │   ├── notification/       # Módulo para notificações
│   │   ├── project/            # Módulo de funcionalidade para Projetos
│   │   ├── security/           # Componentes de segurança transversais
│   │   ├── task/               # Módulo de funcionalidade para Tarefas
│   │   ├── user/               # Módulo de funcionalidade para Usuários
│   │   └── ...
│   ├── pom.xml
│   └── .env.example
├── taskflow-frontend/          # Aplicação Frontend (React/TypeScript)
│   ├── src/
│   │   ├── components/         # Componentes UI reutilizáveis
│   │   ├── features/           # Módulos de funcionalidade (auth, projects, tasks)
│   │   ├── services/           # Clientes de API
│   │   └── ...
│   ├── package.json
│   └── .env.local.example
└── docs/
    └── architecture.md         # Este documento
```
