# 14. CI/CD (Continuous Integration/Continuous Deployment)

Embora o foco do projeto seja o desenvolvimento local, esta seção descreve um pipeline de CI/CD hipotético para uma futura implantação em produção, utilizando **GitHub Actions**.

### 14.1. Gatilhos (Triggers)

*   O pipeline será acionado em cada `push` ou `pull_request` para a branch `main`.

### 14.2. Etapas do Pipeline (Jobs)

1.  **Build e Teste do Backend:**
    *   Configura o ambiente Java (JDK 21).
    *   Executa o comando `mvn clean install` para compilar o código, baixar dependências e rodar todos os testes unitários e de integração.
2.  **Build e Teste do Frontend:**
    *   Configura o ambiente Node.js.
    *   Executa `npm install` para instalar as dependências.
    *   Executa `npm test` para rodar os testes de componentes.
    *   Executa `npm run build` para gerar os arquivos estáticos da aplicação.
3.  **Deploy (Hipotético):**
    *   Se as etapas anteriores forem bem-sucedidas e o evento for um merge na branch `main`, uma etapa de deploy seria acionada.
    *   **Backend:** O artefato JAR gerado seria empacotado em uma imagem Docker e enviado para um container registry (ex: Docker Hub, AWS ECR).
    *   **Frontend:** Os arquivos estáticos gerados seriam enviados para um serviço de hospedagem de sites estáticos (ex: AWS S3 com CloudFront, Vercel, Netlify).
