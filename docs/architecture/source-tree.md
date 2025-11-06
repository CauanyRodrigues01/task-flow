# 4. Estrutura Unificada do Projeto (Polyrepo)

A estrutura do projeto adota uma abordagem Polyrepo dentro de um único repositório Git, com diretórios distintos para `taskflow-backend`, `taskflow-frontend` e um diretório `shared` para tipos TypeScript, garantindo separação de responsabilidades e compartilhamento de código.

```
TaskFlow/
├── taskflow-backend/           # Aplicação Backend (Java/Spring Boot)
│   ├── src/main/java/com/taskflow/
│   │   ├── project/            # Módulo de funcionalidade para Projetos
│   │   ├── task/               # Módulo de funcionalidade para Tarefas
│   │   ├── user/               # Módulo de funcionalidade para Usuários
│   │   ├── security/           # Componentes de segurança transversais
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
├── shared/                     # Pacote de tipos compartilhados (TypeScript)
│   ├── src/
│   │   └── types/              # Interfaces TypeScript (User, Project, Task, etc.)
│   └── package.json
└── docs/
    └── architecture.md         # Este documento
```
