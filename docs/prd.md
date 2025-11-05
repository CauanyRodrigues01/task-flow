# TaskFlow Product Requirements Document (PRD)

## Goals and Background Context

### Goals

*   **Resolver o Ruído de Comunicação e a Perda de Contexto:** Fornecer uma plataforma onde o status real do projeto seja transparente e confiável, reduzindo a necessidade de comunicação externa e o retrabalho.
*   **Empoderar os Usuários com Simplicidade:**
    *   **Gerente de Projeto:** Oferecer confiança e transparência no status das tarefas.
    *   **Colaborador:** Proporcionar uma interface rápida e *clean* que exija o mínimo esforço para ser mantida.
    *   **Administrador:** Garantir dados confiáveis e permissões rigorosas para uma gestão eficaz.
*   **Alcançar Métricas de Sucesso Claras:**
    *   **Adoção e Engajamento:** Reter 80% dos Colaboradores ativos após 30 dias.
    *   **Redução de Ruído:** Aumentar o uso do Histórico de Atividade como fonte primária de atualização.
    *   **Confiabilidade dos Dados:** Manter 99.9% de *uptime* nos serviços de Autenticação e Permissões.

### Background Context

O TaskFlow surge da necessidade de combater a ineficiência gerada por sistemas de gestão de tarefas complexos e burocráticos. Ferramentas como Jira e Asana, embora poderosas, frequentemente sobrecarregam os usuários, forçando-os a "trabalhar para a ferramenta" e a buscar informações contextuais em canais externos como e-mail e chat. Isso resulta em retrabalho massivo, perda de contexto e uma desconfiança generalizada no status real dos projetos.

O TaskFlow propõe uma abordagem diferente, focada em um MVP *lean* que prioriza a **simplicidade da UX** e a **integridade dos dados**. O objetivo é criar uma experiência de usuário intuitiva e de baixo atrito, onde a atualização do status e a consulta ao histórico de atividades sejam tão simples que se tornem a forma natural de trabalhar. Ao fazer isso, buscamos restaurar a confiança na ferramenta, reduzir o ruído de comunicação e permitir que as equipes se concentrem no que realmente importa: a execução de suas tarefas.

### Change Log

| Date | Version | Description | Author |
| :--- | :--- | :--- | :--- |
| 2025-11-04 | 1.0 | Versão inicial do PRD | John (PM) |

## Requirements

### Functional

1.  **FR1: Autenticação e Perfis:**
    *   O sistema deve permitir que os usuários se registrem e façam login.
    *   Deve haver três perfis de usuário: Administrador, Gerente de Projeto e Colaborador, com permissões distintas.
2.  **FR2: Gestão de Projetos:**
    *   Somente o **Gerente de Projeto** pode criar, editar e excluir projetos.
    *   Cada projeto deve ter **um único Gerente de Projeto** responsável.
    *   Dentro de um projeto, o **Gerente de Projeto** pode gerenciar a equipe, adicionando ou removendo Colaboradores.
    *   Cada projeto deve ter um nome, descrição e uma equipe de membros associados.
3.  **FR3: Gestão de Tarefas:**
    *   O **Gerente de Projeto** pode criar, editar e excluir tarefas dentro de um projeto.
    *   O **Colaborador** pode **apenas editar o status** de uma tarefa.
    *   Cada tarefa deve ter um título, descrição, responsável, status (A Fazer, Em Progresso, Concluído), prioridade e data de entrega.
    *   As tarefas devem ser visualizáveis em formato de lista e em um quadro Kanban simples.
    *   Os usuários devem poder filtrar e buscar tarefas por status, responsável e prioridade.
4.  **FR4: Colaboração:**
    *   Os usuários devem poder adicionar comentários a uma tarefa.
    *   O sistema deve registrar um histórico de atividades para cada tarefa (criação, mudanças de status, novos comentários, etc.).
    *   O sistema deve enviar notificações básicas (ex: "Tarefa atribuída a você").
5.  **FR5: Dashboard e Relatórios:**
    *   O sistema deve apresentar um painel principal com uma contagem de tarefas por status.
    *   O sistema deve permitir a geração de um relatório simples de tarefas por projeto, com a opção de exportar para CSV.
6.  **FR6: Gestão de Usuários e Alocação:**
    *   O **Administrador** deve ter uma interface para gerenciar todos os usuários do sistema (convidar, remover, alterar perfil para Gerente de Projeto ou Colaborador).
    *   O **Administrador** pode alocar qualquer Colaborador a qualquer projeto no sistema.

### Non Functional

1.  **NFR1: Usabilidade:** A interface do usuário deve ser extremamente simples, intuitiva e rápida, minimizando o número de cliques para realizar ações comuns.
2.  **NFR2: Segurança:** O sistema deve ser protegido com autenticação baseada em token JWT. As permissões de acesso baseadas no perfil do usuário devem ser rigorosamente aplicadas em toda a API.
3.  **NFR3: Desempenho:** As principais interações do usuário (carregar o painel, abrir uma tarefa, adicionar um comentário) devem ser concluídas em menos de 2 segundos.
4.  **NFR4: Confiabilidade:** O sistema deve ter um *uptime* de 99.9% para os serviços de autenticação e permissões.
5.  **NFR5: Manutenibilidade:** O código deve seguir as melhores práticas da *stack* definida (Java/Spring Boot e React) para garantir que seja fácil de manter e evoluir.

## Future Enhancements

*   **Edição e Exclusão de Comentários:** Permitir que os usuários editem ou excluam seus próprios comentários após a postagem, fornecendo mais flexibilidade na comunicação.
*   **Integração com Serviço de E-mail Real:** Substituir o serviço de e-mail mock local por um serviço de produção robusto (ex: SendGrid, AWS SES) para garantir a entrega confiável de notificações por e-mail.

## User Interface Design Goals

#### Overall UX Vision

A visão geral da UX para o TaskFlow é entregar uma experiência **extremamente simples, intuitiva e de baixo atrito**. O objetivo é que os usuários sintam que a ferramenta os ajuda a trabalhar, e não o contrário. A interface deve ser *clean*, moderna e focada em reduzir a sobrecarga de informações, especialmente para o Colaborador. A transparência e a facilidade de acesso ao contexto da tarefa são primordiais.

#### Key Interaction Paradigms

O principal paradigma de interação será o **Kanban** para a gestão visual de tarefas, complementado por uma **visualização em lista** para flexibilidade. As interações devem ser diretas, com o mínimo de cliques possível. A atualização de status deve ser um processo rápido e incentivado, talvez com mecanismos de "Snapshot de Contexto" (como discutido no brainstorming) para garantir a preservação do histórico sem burocracia. Notificações sutis e contextuais são preferidas em vez de alertas intrusivos.

#### Core Screens and Views

As telas e visualizações mais críticas para entregar os valores e metas do PRD são:

*   "Tela de Login/Registro"
*   "Dashboard Principal (Visão Geral do Usuário)"
*   "Tela de Projetos (Listagem e Detalhes do Projeto)"
*   "Quadro Kanban de Tarefas"
*   "Detalhes da Tarefa (com Comentários e Histórico de Atividade)"
*   "Tela de Gestão de Usuários (para Administrador)"
*   "Tela de Relatórios Básicos"
*   "Visualização 'My Focus' (para Colaborador)"

#### Accessibility: WCAG AA

Considerando a importância da inclusão e a necessidade de um produto acessível, o TaskFlow deve almejar a conformidade com as diretrizes **WCAG AA**.

#### Branding

O branding deve refletir a **simplicidade, clareza e eficiência**. Cores neutras com toques de cor para indicar status ou prioridade podem ser eficazes. A tipografia deve ser legível e moderna. O objetivo é uma estética *clean* e profissional que não distraia o usuário da sua tarefa principal.

#### Target Device and Platforms: Web Responsive

O TaskFlow será desenvolvido como uma aplicação **Web Responsiva**, garantindo uma experiência consistente e funcional em diferentes tamanhos de tela, incluindo desktops, tablets e dispositivos móveis.

## Technical Assumptions

#### Repository Structure: Polyrepo

A estrutura do repositório será **Polyrepo**, com repositórios separados para o *backend* (Java/Spring Boot) e o *frontend* (React).

#### Service Architecture

A arquitetura de serviço será um **Monolith** para o *backend* (Java/Spring Boot). Esta escolha é estratégica para o MVP, priorizando a velocidade de desenvolvimento e a simplicidade de implantação, evitando a complexidade desnecessária de microsserviços em uma fase inicial.

#### Testing Requirements

Os requisitos de teste incluirão **Unit + Integration** testing. Testes unitários garantirão a correção de componentes individuais, enquanto testes de integração verificarão a interação entre os módulos do *backend* e a comunicação com o banco de dados. Testes de *end-to-end* (E2E) serão considerados para fases posteriores, mas não são um requisito *must-have* para o MVP.

#### Additional Technical Assumptions and Requests

*   **Backend (API):**
    *   **Linguagem/Framework:** Java 21 com Spring Boot 3.2.5.
    *   **Banco de Dados:** MySQL, com acesso via Spring Data JPA (Hibernate).
    *   **Segurança:** Spring Security com Token JWT para autenticação e autorização rigorosa baseada em perfis.
    *   **Documentação da API:** SpringDoc (Swagger/OpenAPI) para documentação automática e interativa.
    *   **Produtividade:** Uso de Lombok para reduzir código *boilerplate*.
    *   **Validação:** Spring Boot Validation para garantir a integridade dos dados de entrada.
    *   **Estrutura de Pacotes (Package Structure):** A estrutura de pacotes do backend deve ser organizada por **funcionalidade** (ex: `com.taskflow.project`, `com.taskflow.task`) em vez de por **camadas** (ex: `com.taskflow.controller`, `com.taskflow.service`). Esta abordagem melhora a modularidade, a coesão e facilita a navegação e manutenção do código.
    *   **Endpoints "Context-Aware":** O backend deve registrar a causa contextual de cada mudança de status de forma atômica, garantindo a integridade do histórico de atividades.
*   **Frontend (UI):**
    *   **Framework:** React com JavaScript (ou TypeScript).
    *   **Estilização:** CSS (com possibilidade de Styled-components/Tailwind em fases futuras, se necessário).
    *   **Comunicação com API:** Axios ou Fetch API.
    *   **Gerenciamento de Estado:** React Context (Redux opcional para complexidade futura).
    *   **Simplicidade da UX:** Prioridade máxima na entrega de uma interface *clean* e intuitiva, evitando *over-engineering* na UI.
    *   **Visualização "My Focus":** Implementar uma visualização personalizada para o Colaborador, mostrando apenas suas tarefas prioritárias.
    *   **"Snapshot de Contexto" por Status:** Ao mudar um cartão no Kanban, o sistema deve exigir uma Nota Rápida ou checklist breve para preservar o contexto.
*   **Metodologia:** Aderência à metodologia BMAD, com foco na preservação de contexto em todas as camadas da aplicação.

## Epic List

1.  **Epic 1: Fundação e Acesso de Usuários**
    *   **Objetivo:** Estabelecer a infraestrutura central da aplicação, autenticação segura de usuários e permitir a configuração básica de usuários e projetos. Este épico permitirá que os usuários façam login, gerenciem seus perfis e que os administradores configurem usuários e projetos iniciais.

    #### Story 1.1: Configuração Inicial do Projeto e Autenticação de Usuários

    As a user,
    I want to register and log in to TaskFlow,
    so that I can securely access the system and have a defined user profile.

    **Acceptance Criteria:**
    1.  The system must allow new users to register with a valid email and password.
    2.  The system must allow existing users to log in with their credentials.
    3.  The system must validate user credentials and issue a valid JWT token upon successful login.
    4.  The system must store basic user profile information (name, email).
    5.  The system must assign a default profile (Collaborator) to new users upon registration.
    6.  The system must protect user passwords using secure hashing techniques.
    7.  The system must return clear error messages for failed login or registration attempts (e.g., invalid credentials, email already registered).
    8.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 1.2: Gestão de Perfis de Usuário e Permissões Básicas

    As an Administrator,
    I want to manage user profiles and their permissions,
    so that I can control who has access to which functionalities and roles in the system.

    **Acceptance Criteria:**
    1.  The Administrator must have access to an interface to view all registered users.
    2.  The Administrator must be able to invite new users to the system (with a default Collaborator profile).
    3.  The Administrator must be able to remove existing users from the system.
    4.  The Administrator must be able to change a user's profile between Collaborator, Project Manager, and Administrator.
    5.  The system must strictly apply permissions based on the user's profile across all API operations.
    6.  The system must record user profile changes made by the Administrator in the activity history.
    7.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 1.3: Criação e Gestão de Projetos pelo Gerente de Projeto

    As a Project Manager,
    I want to create and manage projects,
    so that I can organize my team's work and have a dedicated space for my tasks.

    **Acceptance Criteria:**
    1.  The Project Manager must have access to an interface to create new projects.
    2.  When creating a project, the Project Manager must define a name and a description.
    3.  The Project Manager who creates the project is automatically defined as the sole Project Manager responsible for it.
    4.  The Project Manager must be able to edit the name and description of their projects.
    5.  The Project Manager must be able to delete their projects (with confirmation).
    6.  The system must record the creation, editing, and deletion of projects in the activity history.
    7.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 1.4: Gestão de Equipes de Projeto pelo Gerente de Projeto

    As a Project Manager,
    I want to add and remove Collaborators from my projects,
    so that I can assemble and manage the team that will work on each project.

    **Acceptance Criteria:**
    1.  The Project Manager must have access to an interface to view the members of their projects.
    2.  The Project Manager must be able to add existing Collaborators in the system to their projects.
    3.  The Project Manager must be able to remove Collaborators from their projects.
    4.  The system must ensure that only users with a Collaborator profile can be added as Collaborators to a project.
    5.  The system must record the additions and removals of team members from a project in the activity history.
    6.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

2.  **Epic 2: Gestão Essencial de Projetos e Tarefas**
    *   **Objetivo:** Implementar as funcionalidades essenciais para criar, gerenciar e visualizar projetos e tarefas. Isso inclui o quadro Kanban e as visualizações em lista, juntamente com detalhes básicos da tarefa e filtragem.

    #### Story 2.1: Criação e Detalhamento de Tarefas pelo Gerente de Projeto

    As a Project Manager,
    I want to create tasks with all the necessary details,
    so that I can clearly define the work to be done and assign it to my team.

    **Acceptance Criteria:**
    1.  The Project Manager must have access to an interface to create new tasks within a project.
    2.  When creating a task, the Project Manager must be able to define: title, description, assignee (a Collaborator of the project), initial status (To Do), priority, and due date.
    3.  The system must validate that the assigned assignee is an active Collaborator in the project.
    4.  The system must record the creation of the task in the activity history.
    5.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 2.2: Edição e Exclusão de Tarefas pelo Gerente de Projeto

    As a Project Manager,
    I want to be able to edit and delete tasks,
    so that I can adjust work details as needed or remove obsolete tasks.

    **Acceptance Criteria:**
    1.  The Project Manager must have access to an interface to edit any field of a task in their projects (title, description, assignee, status, priority, due date).
    2.  The Project Manager must be able to delete tasks from their projects (with confirmation).
    3.  The system must record the editing and deletion of tasks in the activity history.
    4.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 2.3: Visualização de Tarefas em Lista e Kanban

    As a user (Project Manager or Collaborator),
    I want to view tasks in different formats,
    so that I can have a clear view of the work and its progress.

    **Acceptance Criteria:**
    1.  The system must present a list view of all tasks in a project.
    2.  The system must present a simple Kanban board for tasks in a project, with columns for statuses (To Do, In Progress, Done).
    3.  Kanban cards must display the task title, assignee, and status.
    4.  The Collaborator must be able to drag and drop cards between status columns on the Kanban board to update the task status.
    5.  The system must record the task status change in the activity history.
    6.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 2.4: Filtragem e Busca de Tarefas

    As a user (Project Manager or Collaborator),
    I want to filter and search for tasks,
    so that I can quickly find relevant work.

    **Acceptance Criteria:**
    1.  The system must allow filtering tasks by status (To Do, In Progress, Done).
    2.  The system must allow filtering tasks by assignee.
    3.  The system must allow filtering tasks by priority.
    4.  The system must allow searching for tasks by keywords in the title or description.
    5.  Filters and search must be applicable in both list view and Kanban board.
    6.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

3.  **Epic 3: Colaboração Aprimorada e Preservação de Contexto**
    *   **Objetivo:** Entregar as funcionalidades colaborativas centrais, com foco na comunicação transparente e na preservação do contexto. Este épico incluirá comentários em tarefas, notificações básicas e o histórico de atividades crítico com "Endpoints Context-Aware" e "Snapshot de Contexto".

    #### Story 3.1: Comentários em Tarefas

    As a user (Project Manager or Collaborator),
    I want to add comments to tasks,
    so that I can communicate about the work and provide additional information.

    **Acceptance Criteria:**
    1.  Users must be able to add text comments to any task.
    2.  Each comment must display the author and the date/time of posting.
    3.  Comments must be visible to all project members associated with the task.
    4.  The system must record the addition of a comment in the task's activity history.
    5.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 3.2: Notificações Básicas de Atividade

    As a user,
    I want to receive notifications about relevant activities in my tasks and projects,
    so that I can stay updated without constantly checking the tool.

    **Acceptance Criteria:**
    1.  The system must send a notification to the assignee when a task is assigned to them.
    2.  The system must send a notification to the Project Manager when the status of a task in their project is changed.
    3.  The system must send a notification to project members when a new comment is added to a task they are following or are responsible for.
    4.  Notifications must be displayed discreetly in the user interface (e.g., bell icon, notification list).
    5.  Users must be able to view a summary of recent notifications.
    6.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 3.3: Histórico de Atividades "Context-Aware"

    As a user (Project Manager or Collaborator),
    I want to see a detailed and contextualized history of all changes in a task,
    so that I can understand the "why" behind each change and have full transparency about the progress.

    **Acceptance Criteria:**
    1.  Each task must have an activity history that automatically records: task creation, assignee assignment/reassignment, status changes, field edits (title, description, priority, due date), and comment additions.
    2.  For each event in the history, the system must record: the user who performed the action, the date/time of the action, and a clear description of the change.
    3.  For status changes, the backend must record the contextual cause of the change (e.g., "Task moved to 'In Progress' by [User] via Kanban").
    4.  The activity history must be immutable and visible to all project members associated with the task.
    5.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 3.4: "Snapshot de Contexto" na Mudança de Status (Kanban)

    As a Collaborator,
    I want to quickly provide additional context when changing a task's status on the Kanban board,
    so that the activity history is always rich and transparent, without requiring excessive effort.

    **Acceptance Criteria:**
    1.  When dragging and dropping a task card between status columns on the Kanban board, the system must present a prompt for the Collaborator to add a "Quick Note" or select an item from a pre-defined "Brief Checklist" (if applicable).
    2.  The "Quick Note" must be a free-text field and optional, but encouraged.
    3.  The "Brief Checklist" (if implemented) must be a list of pre-defined items that the Collaborator can quickly check off.
    4.  The content of the "Quick Note" or "Brief Checklist" must be recorded as part of the status change event in the task's activity history.
    5.  This functionality must be designed to be low-friction, not significantly interrupting the Collaborator's workflow.
    6.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

4.  **Epic 4: Dashboard, Relatórios e Refinamentos de UX**
    *   **Objetivo:** Fornecer aos usuários uma visão geral de seu trabalho e progresso do projeto através de um dashboard e relatórios básicos. Este épico também incluirá a visualização "My Focus" para colaboradores e garantirá a simplicidade geral da UX.

    #### Story 4.1: Painel Principal (Dashboard)

    As a user (Project Manager or Administrator),
    I want to have a main dashboard that gives me a quick overview of task and project status,
    so that I can track progress and quickly identify areas that need attention.

    **Acceptance Criteria:**
    1.  The main dashboard must display a count of tasks by status (To Do, In Progress, Done) for the projects the user has access to.
    2.  The dashboard must display a summary of project progress (e.g., percentage of completed tasks).
    3.  The dashboard must be customizable so that the user can focus on the most relevant information for them (e.g., filter by project).
    4.  Information on the dashboard must be updated in real-time or with minimal delay.
    5.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 4.2: Relatórios Básicos de Tarefas

    As a Project Manager or Administrator,
    I want to generate basic reports on tasks,
    so that I can analyze progress and share information with other stakeholders.

    **Acceptance Criteria:**
    1.  The system must allow the generation of a simple task report by project.
    2.  The report must include information such as task title, assignee, status, priority, and due date.
    3.  The system must allow exporting the report to CSV format.
    4.  The report must reflect the latest data from the system.
    5.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 4.3: Visualização "My Focus" para Colaboradores

    As a Collaborator,
    I want to have a personalized view that shows only my priority tasks,
    so that I can focus on my work without distractions and irrelevant information.

    **Acceptance Criteria:**
    1.  The system must offer a "My Focus" view that displays only the tasks assigned to the logged-in Collaborator.
    2.  This view must prioritize tasks based on due date and defined priority.
    3.  The interface of this view must be extremely *clean* and free of non-essential elements.
    4.  The Collaborator must be able to interact with their tasks (e.g., change status, add comments) directly from this view.
    5.  O comportamento da API/lógica de backend para esta funcionalidade pode ser verificado localmente (ex: via chamada direta à API, CLI ou teste de unidade/integração).

    #### Story 4.4: Refinamento da UX e Feedback Geral

    As a user,
    I want the TaskFlow interface to be consistently simple and intuitive,
    so that my user experience is fluid and pleasant, without frustrations.

    **Acceptance Criteria:**
    1.  Navigation between different sections of the application must be clear and consistent.
    2.  Interface elements must be responsive and load quickly.
    3.  System error messages and feedback must be clear and helpful.
    4.  The application must adhere to simplicity and minimalism design principles across all screens.
    5.  The system must provide a simple mechanism for users to submit feedback on the UX.
