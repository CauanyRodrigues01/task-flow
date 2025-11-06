# 7. Arquitetura do Frontend (SPA)

### 7.1. Organização de Pastas

A estrutura de pastas é organizada por funcionalidade para promover a coesão e a escalabilidade.

```
src/
├── components/         # Componentes de UI globais e reutilizáveis (Button, Input)
├── features/           # Módulos de funcionalidade (features)
│   ├── auth/           # Funcionalidade de autenticação
│   ├── projects/       # Funcionalidade de projetos
│   └── tasks/          # Funcionalidade de tarefas
├── services/           # Lógica de comunicação com a API
├── contexts/           # Contextos React para estado global
├── hooks/              # Hooks customizados
├── pages/              # Componentes que representam as páginas/rotas
└── ...
```

### 7.2. Gerenciamento de Estado

*   **Estado Global:** React Context API para dados de autenticação do usuário (`AuthContext`).
*   **Estado do Servidor:** Hooks customizados (ex: `useProjects`, `useTasks`) que encapsulam a lógica de fetch, cache, loading e erros, mantendo os componentes de UI limpos.

### 7.3. Roteamento

*   **Biblioteca:** React Router.
*   **Rotas Protegidas:** Um componente `ProtectedRoute` verifica o estado de autenticação no `AuthContext` e redireciona para a página de login se o usuário não estiver autenticado.
