# Epic List

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
