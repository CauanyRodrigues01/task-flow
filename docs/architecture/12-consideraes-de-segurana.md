# 12. Considerações de Segurança

Além da autenticação e autorização já definidas, as seguintes práticas de segurança serão adotadas:

*   **Armazenamento de Senhas:** As senhas dos usuários serão armazenadas no banco de dados usando um algoritmo de hash forte e com salt, como o **BCrypt**, que é o padrão do Spring Security.
*   **Validação de Entrada:** Todas as entradas de dados provenientes do cliente (corpos de requisição, parâmetros de URL) serão validadas no backend para prevenir ataques de injeção (ex: SQL Injection). Utilizaremos a **Bean Validation (`@Valid`)** nos DTOs dos controllers.
*   **Proteção contra XSS (Cross-Site Scripting):** O React, por padrão, já escapa o conteúdo renderizado na DOM, o que oferece uma boa proteção contra ataques XSS.
*   **Configuração de CORS (Cross-Origin Resource Sharing):** O backend será configurado para permitir requisições apenas de origens confiáveis (neste caso, o endereço do frontend em desenvolvimento, `http://localhost:3000`).
*   **Gerenciamento de Segredos:** Chaves secretas, como o segredo do JWT e senhas de banco de dados, serão gerenciadas através de variáveis de ambiente e nunca serão versionadas no código-fonte.
