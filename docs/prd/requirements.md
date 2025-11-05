# Requirements

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
