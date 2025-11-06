# 10. Fluxo de Desenvolvimento Local

### 10.1. Pré-requisitos

*   JDK 21
*   Maven 3.x
*   Node.js (LTS) e NPM
*   MySQL Server 8.x
*   Git

### 10.2. Configuração Inicial

1.  **Clonar o repositório.**
2.  **Configurar Backend:** Navegue para `taskflow-backend`, copie `.env.example` para `.env` e configure as variáveis do banco de dados. Rode `mvn clean install`.
3.  **Configurar Frontend:** Navegue para `taskflow-frontend`, copie `.env.local.example` para `.env.local`. Rode `npm install`.
4.  **Configurar Shared:** Navegue para `shared` e rode `npm install`.

### 10.3. Comandos de Desenvolvimento

*   **Iniciar Backend:** Em `taskflow-backend`, rode `mvn spring-boot:run`.
*   **Iniciar Frontend:** Em `taskflow-frontend`, rode `npm start`.
