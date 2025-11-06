# Ata da Revisão e Retrospectiva do Sprint 1

**Data:** 06 de Novembro de 2025

**Participantes:** Bob (Scrum Master), Equipe de Desenvolvimento

## 1. Revisão do Sprint (Sprint Review)

### 1.1. Objetivo do Sprint

O objetivo do Sprint 1 era estabelecer a infraestrutura central da aplicação, incluindo autenticação, gestão de usuários, criação de projetos e gestão de equipes.

### 1.2. Análise por História

| História                                      | Resultado                               | QA Gate  | Observações Chave                                                                                                                                                           |
| --------------------------------------------- | --------------------------------------- | -------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **1.1: Autenticação de Usuários**             | Funcionalidade entregue                 | `CONCERNS` | Backend sólido, mas frontend desalinhado da arquitetura (React SPA), vulnerabilidade de segurança (JWT em `localStorage`) e falta de testes de frontend.                 |
| **1.2: Gestão de Perfis de Usuário**          | **Incompleta**                          | `FAIL`   | A interface de frontend crítica não foi implementada. Faltam testes de integração e a funcionalidade de histórico de atividades.                                        |
| **1.3: Criação e Gestão de Projetos**         | Funcionalidade entregue com ressalvas   | `CONCERNS` | Backend funcional, mas persistem os problemas de arquitetura do frontend, segurança (JWT) e falta de testes para o histórico de atividades.                               |
| **1.4: Gestão de Equipes de Projeto**         | Funcionalidade entregue com ressalvas   | `CONCERNS` | Padrão semelhante às outras histórias. Backend funcional, mas o débito técnico no frontend e as lacunas de teste continuam.                                             |

### 1.3. Conclusão da Revisão

O sprint entregou valor de negócio significativo no backend. No entanto, acumulou um débito técnico considerável no frontend devido a um impedimento técnico. A cobertura de testes está incompleta e uma vulnerabilidade de segurança de alta prioridade foi identificada.

---

## 2. Retrospectiva do Sprint

### 2.1. O que foi bem?

*   **Documentação Clara:** A documentação de arquitetura e o PRD serviram como uma base sólida e eficaz para o desenvolvimento.

### 2.2. O que não foi tão bem?

*   **Impedimento Técnico Crítico:** O Dev Agent não conseguiu configurar a stack de frontend com Vite/React, resultando em 6 horas de trabalho perdido e uma mudança de tecnologia que gerou débito técnico.

### 2.3. O que podemos melhorar?

*   **Validação Técnica Antecipada:** É necessário entender melhor as capacidades e limitações do Dev Agent antes de se comprometer com uma abordagem técnica, a fim de evitar loops e atrasos.

---

## 3. Plano de Ação para o Sprint 2

Com base na retrospectiva, os seguintes itens de ação foram acordados para serem implementados no próximo sprint:

1.  **Ação 1: Criar uma "Spike Story" (Investigação Técnica):**
    *   **Tarefa:** No início do Sprint 2, criar uma história para investigar e validar a criação de uma aplicação mínima "Hello World" com Vite/React. O objetivo é confirmar a capacidade do Dev Agent com a stack desejada antes de comprometer histórias de funcionalidades.

2.  **Ação 2: Priorizar o Débito Técnico de Segurança:**
    *   **Tarefa:** Criar uma história técnica para corrigir a vulnerabilidade de segurança do token JWT, movendo seu armazenamento de `localStorage` para cookies HttpOnly.

3.  **Ação 3: Tornar o Débito de Arquitetura Visível:**
    *   **Tarefa:** Adicionar o "desvio de arquitetura do frontend" ao backlog como um item de débito técnico. Isso garante que o problema seja rastreado e possa ser priorizado em um sprint futuro.
