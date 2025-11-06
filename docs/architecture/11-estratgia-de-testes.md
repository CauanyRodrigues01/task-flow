# 11. Estratégia de Testes

Para garantir a qualidade e a estabilidade da aplicação, adotaremos uma abordagem de testes em múltiplas camadas.

### 11.1. Testes de Backend

*   **Testes Unitários:**
    *   **Frameworks:** JUnit 5 e Mockito.
    *   **Escopo:** Isolar e testar a lógica de negócio em classes de `Service`. As dependências (como `Repositories`) serão mockadas para garantir que apenas a unidade de código em questão seja testada.
*   **Testes de Integração:**
    *   **Framework:** Spring Boot Test com `@SpringBootTest`.
    *   **Escopo:** Testar a interação entre as camadas da aplicação, desde o `Controller` até o banco de dados. Utilizaremos um banco de dados em memória (como H2) ou uma instância de teste do MySQL para validar os fluxos completos da API.

### 11.2. Testes de Frontend

*   **Testes de Componentes:**
    *   **Frameworks:** Jest e React Testing Library.
    *   **Escopo:** Testar componentes de UI de forma isolada para garantir que eles renderizem corretamente e respondam às interações do usuário conforme o esperado.
*   **Testes de Integração:**
    *   **Escopo:** Testar a interação entre múltiplos componentes, como a submissão de um formulário que chama um serviço de API mockado e atualiza o estado da aplicação.

### 11.3. Testes End-to-End (E2E)

*   **Framework (Sugestão Futura):** Cypress ou Playwright.
*   **Escopo:** Embora não seja o foco para a implementação inicial, em uma fase futura, os testes E2E seriam implementados para simular jornadas completas do usuário no navegador, garantindo que o frontend e o backend funcionem juntos corretamente.
