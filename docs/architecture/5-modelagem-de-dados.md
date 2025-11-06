# 5. Modelagem de Dados

### 5.1. Modelo Conceitual e Papéis

A lógica de permissões do sistema é baseada em três papéis de usuário distintos, que definem o que cada um pode fazer.

*   **Entidades Principais:**
    *   **User:** Representa um usuário do sistema.
    *   **Project:** Contêiner para tarefas. Cada projeto tem um `owner` e múltiplos `members`.
    *   **ProjectMember:** Tabela de associação que liga `Users` a `Projects`.
    *   **Task:** A unidade de trabalho, pertencente a um `Project`.
    *   **Comment:** Comentários feitos em uma `Task`.

*   **Definição Detalhada dos Papéis:**

    #### 1. `ADMIN` (Administrador)
    *   **Função:** Superusuário com acesso total e irrestrito ao sistema.
    *   **Permissões:**
        *   Realizar **todas as ações** de um `PROJECT_MANAGER` em **qualquer projeto**.
        *   Acesso a todos os endpoints da API, sem exceção.
        *   Capacidade implícita de gerenciar todos os dados do sistema.

    #### 2. `PROJECT_MANAGER` (Gerente de Projeto)
    *   **Função:** Criar e gerenciar os projetos dos quais é proprietário.
    *   **Permissões:**
        *   **Projetos:**
            *   Pode **criar** novos projetos.
            *   Para os projetos que gerencia (`owner`), pode **visualizar**, **atualizar** e **deletar**.
        *   **Membros:**
            *   Pode **adicionar** e **remover** membros (`COLLABORATOR`) de seus projetos.
        *   **Tarefas:**
            *   Pode **criar**, **visualizar**, **atualizar**, **deletar** e **atribuir** tarefas dentro de seus projetos.
        *   **Comentários:**
            *   Pode **adicionar** comentários.

    #### 3. `COLLABORATOR` (Colaborador)
    *   **Função:** Membro de um projeto com acesso limitado, focado em visualização e comunicação.
    *   **Permissões:**
        *   **Projetos:**
            *   Pode **visualizar** os projetos dos quais é membro.
            *   **NÃO PODE** criar, atualizar ou deletar projetos.
        *   **Membros:**
            *   **NÃO PODE** gerenciar membros.
        *   **Tarefas:**
            *   Pode **visualizar** as tarefas dos projetos dos quais é membro.
            *   **NÃO PODE** criar, editar, deletar ou atribuir tarefas.
        *   **Comentários:**
            *   Pode **adicionar** comentários às tarefas.

### 5.2. Esquema do Banco de Dados (MySQL DDL)

Este script SQL define a estrutura completa do banco de dados.

```sql
CREATE DATABASE IF NOT EXISTS taskflow_db;
USE taskflow_db;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE project_members (
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'TODO',
    priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    due_date DATETIME,
    project_id BIGINT NOT NULL,
    assignee_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    task_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);
```
