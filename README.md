# TestWithSpringBoot

Este repositório contém uma API simples de gerenciamento de pessoas (**Person API**) desenvolvida como **projeto de estudo**, com foco principal em **aprendizado de testes no Spring Boot**, incluindo testes unitários e testes de integração.

---

## 🎯 Objetivo

O propósito deste projeto é **experimentação, estudo e referência pessoal**.  
Ele foi criado para aprofundar conhecimentos em:

- Estruturação de uma API REST com Spring Boot
- Testes unitários usando MockMvc e Mockito
- Testes de integração com Rest‑Assured
- Uso de banco real (MySQL) em ambiente de teste
- Execução de scripts SQL para cenários isolados com `@Sql`
- Organização de perfis de execução (`test`, `test-integration`)

Por se tratar de um projeto didático, você encontrará:

- Trechos comentados
- Código alternativo deixado propositalmente
- Anotações e experimentos
- Decisões focadas no aprendizado e não em produção

---

## 📦 Conteúdo

### ✔️ API REST – CRUD de Person
- Endpoints REST seguindo boas práticas
- Controllers, Services e Repositories bem estruturados
- Uso de ResponseEntity, validações e tratamento de erros simples

### ✔️ Camada de dados
- Entidade `Person` com:
    - Campos customizados (`first_name`, `last_name`, etc.)
    - Mapeamento JPA
    - Geração automática de ID
- Repositório com:
    - Queries derivadas
    - JPQL
    - Native Query

### ✔️ Testes unitários
- Feitos com:
    - `@WebMvcTest`
    - `MockMvc`
    - `Mockito` / `BDDMockito`
- Testes para:
    - Sucesso
    - Erros (404, 400)
    - Regra de negócio

### ✔️ Testes de integração
- Feitos com:
    - `@SpringBootTest(webEnvironment = RANDOM_PORT)`
    - `Rest-Assured`
    - `@ActiveProfiles("test-integration")`
- Banco:
    - **MySQL real**
    - Dados de cenário com **@Sql**
- Testes completos de:
    - GET /all
    - GET /{id}
    - POST
    - PUT
    - DELETE

### ✔️ Perfis e ambientes
- `application-test.yml` → usado com H2 (testes unitários)
- `application-test-integration.yml` → usado com MySQL (integração)
- Scripts e migrations organizados:


--- 
## ▶️ Como rodar

### 🔧 Pré‑requisitos

- **Java 21+**
- **Maven** (ou wrapper `mvnw`)
- **MySQL 8+** (para testes de integração)

### 🏗️ Executar em desenvolvimento

Usando Maven Wrapper:

```bash
./mvnw spring-boot:run
````

---
Aprender é muito bom, mas compartilhar conhecimento é ainda melhor!
