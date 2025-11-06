# 13. Logging e Monitoramento

### 13.1. Logging

*   **Framework:** Utilizaremos o **SLF4J com Logback**, que é a configuração padrão do Spring Boot.
*   **Estratégia:**
    *   Logs serão gerados para eventos importantes da aplicação, como inicialização, erros, requisições recebidas e lógicas de negócio críticas.
    *   Utilizaremos diferentes níveis de log (`INFO`, `DEBUG`, `WARN`, `ERROR`) para controlar a verbosidade dos logs em diferentes ambientes.
    *   As exceções não tratadas serão capturadas pelo `GlobalExceptionHandler` e logadas com o nível `ERROR`, incluindo o stack trace completo.

### 13.2. Monitoramento (Local)

*   **Ferramenta:** **Spring Boot Actuator** será utilizado para expor endpoints de monitoramento.
*   **Endpoints Principais:**
    *   `/actuator/health`: Fornece informações básicas sobre a saúde da aplicação, incluindo o status da conexão com o banco de dados.
    *   `/actuator/info`: Exibe informações gerais da aplicação.
    *   `/actuator/metrics`: Fornece métricas detalhadas sobre o uso de memória, CPU, threads, etc.
