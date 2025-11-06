# Padrões de Codificação

Este documento define os padrões de codificação e as melhores práticas a serem seguidas no desenvolvimento do TaskFlow. A adesão a estes padrões é crucial para manter a qualidade, legibilidade e manutenibilidade do código.

## Geral

*   **Idioma:** Todo o código, comentários, nomes de variáveis e documentação devem ser escritos em **Português do Brasil (pt-BR)**.
*   **Versionamento:** Siga as convenções do [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) para mensagens de commit.
*   **Formatação:** Utilize as ferramentas de formatação automática configuradas no projeto (ex: Prettier, Google Java Format) para garantir um estilo de código consistente.

## Backend (Java / Spring Boot)

*   **Guia de Estilo:** Siga o [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
*   **Nomenclatura:**
    *   **Pacotes:** `com.taskflow.nomedomodulo` (ex: `com.taskflow.projeto`). Utilize substantivos no singular.
    *   **Classes:** `CamelCase` (ex: `GerenciadorDeProjetos`).
    *   **Interfaces:** `CamelCase` (ex: `ServicoUsuario`).
    *   **Métodos:** `camelCase` (ex: `buscarUsuarioPorId`).
    *   **Constantes:** `UPPER_SNAKE_CASE` (ex: `TAMANHO_MAXIMO_NOME`).
*   **Arquitetura de Pacotes:** A estrutura de pacotes deve ser organizada por **funcionalidade** (`package-by-feature`), conforme definido no documento `source-tree.md`.
*   **Imutabilidade:** Prefira objetos imutáveis sempre que possível, especialmente para DTOs e Entidades.
*   **Tratamento de Exceções:** Utilize exceções customizadas e específicas da aplicação para erros de negócio. Implemente um `@ControllerAdvice` global para tratar exceções e retornar respostas de erro padronizadas.
*   **Lombok:** Utilize o Lombok para reduzir código boilerplate (ex: `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`).

## Frontend (React / TypeScript)

*   **Guia de Estilo:** Siga o [Airbnb JavaScript Style Guide](https://github.com/airbnb/javascript) e as melhores práticas da comunidade TypeScript.
*   **Nomenclatura:**
    *   **Arquivos de Componente:** `PascalCase.tsx` (ex: `BotaoSalvar.tsx`).
    *   **Componentes:** `PascalCase` (ex: `<BotaoSalvar />`).
    *   **Hooks:** `useCamelCase` (ex: `useDadosUsuario`).
    *   **Variáveis e Funções:** `camelCase`.
*   **Estrutura de Componentes:**
    *   Mantenha os componentes pequenos e focados em uma única responsabilidade.
    *   Separe a lógica de negócio (hooks) da lógica de apresentação (JSX).
    *   Utilize a organização de pastas por funcionalidade (`features`), conforme definido no `source-tree.md`.
*   **Tipagem:**
    *   Utilize TypeScript para todas as partes do código.
    *   Evite o uso de `any` sempre que possível. Defina interfaces e tipos claros para props, estado e objetos da API.
    *   Compartilhe tipos com o backend através do diretório `shared/` quando aplicável.
*   **Gerenciamento de Estado:**
    *   Utilize o React Context para estado global (ex: autenticação).
    *   Para o estado do servidor (dados da API), utilize hooks customizados que encapsulem a lógica de fetch, cache e tratamento de erros.
*   **Estilização:** Utilize as classes do Tailwind CSS diretamente no JSX. Evite a criação de arquivos CSS separados, a menos que seja para estilização global ou de componentes muito complexos.
