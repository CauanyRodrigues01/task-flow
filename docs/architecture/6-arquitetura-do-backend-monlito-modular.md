# 6. Arquitetura do Backend (Monólito Modular)

A aplicação backend é um monólito, mas internamente organizada em módulos de negócio coesos e independentes, seguindo a abordagem **"package-by-feature"**.

### 6.1. Organização de Pacotes

```
com/taskflow/
├── project/            # Módulo de funcionalidade para Projetos
│   ├── ProjectController.java
│   ├── ProjectService.java
│   ├── ProjectRepository.java
│   ├── Project.java
│   └── dto/
├── task/               # Módulo de funcionalidade para Tarefas
│   └── ...
├── user/               # Módulo de funcionalidade para Usuários
│   └── ...
├── security/           # Componentes de segurança transversais
└── exception/          # Handlers de exceção globais
```

### 6.2. Camada de Acesso a Dados

*   **Tecnologia:** Spring Data JPA.
*   **Padrão:** O Repository Pattern é implementado via interfaces que estendem `JpaRepository`, abstraindo o acesso a dados e fornecendo métodos CRUD.
*   **Transações:** Gerenciadas pelo Spring com a anotação `@Transactional` na camada de `Service`, garantindo a atomicidade das operações.

### 6.3. Autenticação e Autorização

*   **Tecnologia:** Spring Security e JSON Web Tokens (JWT).
*   **Fluxo:** Um filtro customizado (`JwtAuthenticationFilter`) intercepta as requisições, valida o token JWT e configura o `SecurityContextHolder` do Spring. A autorização em nível de método é aplicada nos controllers usando a anotação `@PreAuthorize`.
