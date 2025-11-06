# 8. Especificação da API RESTful

**URL Base:** `/api/v1`
**Autenticação:** Todas as rotas, exceto `/auth/**`, exigem um `Bearer Token` JWT.

### 8.1. Autenticação (`/auth`)

*   `POST /auth/register`: Registra um novo usuário.
*   `POST /auth/login`: Autentica um usuário e retorna um token JWT.

### 8.2. Projetos (`/projects`)

*   `POST /projects`: Cria um novo projeto (Permissão: `ADMIN`, `PROJECT_MANAGER`).
*   `GET /projects`: Lista projetos com base no papel do usuário.
*   `GET /projects/{projectId}`: Obtém detalhes de um projeto.
*   `PUT /projects/{projectId}`: Atualiza um projeto (Permissão: `ADMIN`, `PROJECT_MANAGER` proprietário).
*   `DELETE /projects/{projectId}`: Deleta um projeto (Permissão: `ADMIN`, `PROJECT_MANAGER` proprietário).

### 8.3. Membros do Projeto (`/projects/{projectId}/members`)

*   `POST /projects/{projectId}/members`: Adiciona um usuário a um projeto (Permissão: `ADMIN`, `PROJECT_MANAGER` proprietário).
*   `DELETE /projects/{projectId}/members/{userId}`: Remove um membro de um projeto (Permissão: `ADMIN`, `PROJECT_MANAGER` proprietário).

### 8.4. Tarefas (`/projects/{projectId}/tasks`)

*   `POST /projects/{projectId}/tasks`: Cria uma nova tarefa (Permissão: `ADMIN`, `PROJECT_MANAGER`).
*   `GET /projects/{projectId}/tasks`: Lista todas as tarefas de um projeto.
*   `PUT /projects/{projectId}/tasks/{taskId}`: Atualiza uma tarefa (Permissão: `ADMIN`, `PROJECT_MANAGER`).
*   `PATCH /projects/{projectId}/tasks/{taskId}`: Atualiza parcialmente uma tarefa (Permissão: `ADMIN`, `PROJECT_MANAGER`).
*   `DELETE /projects/{projectId}/tasks/{taskId}`: Deleta uma tarefa (Permissão: `ADMIN`, `PROJECT_MANAGER`).

### 8.5. Comentários (`/tasks/{taskId}/comments`)

*   `POST /tasks/{taskId}/comments`: Adiciona um novo comentário a uma tarefa (Permissão: Membros do projeto).
