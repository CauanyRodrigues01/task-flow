# Technical Assumptions

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
