# MVS Sistema

`MVS Sistema` é uma aplicação desktop moderna construída com **JavaFX** para a interface gráfica e **Spring Boot** para o gerenciamento do backend, injeção de dependência e persistência de dados.

---
<img src="/src/main/resources/static/imagens/tituloLogin.png" width=1200/>

---
## Tecnologias Principais

| Categoria | Tecnologia | Versão |
| :--- | :--- | :--- |
| **Linguagem** | **Java** | `25` | 
| **Framework** | **Spring Boot** | `3.5.7` | 
| **Banco de Dados** | **PostgreSQL** | (Runtime) | 
| **Interface** | **AtlantaFX** | `2.1.0` | 
| **Build System** | **Maven** | `3.9.11` |

## Requisitos

- **Java Development Kit (JDK) 25**.
- **PostgreSQL**: Uma instância de banco de dados rodando.
---

## ⚙️ Configuração do Ambiente

### 1. Banco de Dados

Este projeto usa **Flyway** para gerenciar automaticamente as migrações do banco de dados (criação de tabelas, etc.). Você só precisa:

- Criar um banco de dados vazio no seu PostgreSQL (ex: `mvsistema`).
- Configurar as credenciais de acesso no arquivo:
  `src/main/resources/application.properties`

**Exemplo de `application.properties`:**

```properties
# Configuração do Banco de Dados PostgreSQL

spring.application.name=mvsistema

spring.datasource.url=jdbc:postgresql://localhost:5432/mvsistema
spring.datasource.username=postgres
spring.datasource.password=SENHA_SECRETA   # <- Informe sua senha aqui
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.flyway.enabled=true

spring.jpa.open-in-view=false

logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

```


## Instalação

#### Clonar o repositório
```
git clone https://github.com/seuusuario/mvsistema.git      
```

#### Entrar na raiz do projeto
```                   
cd mvsistema   
```
#### Dar permissão ao Maven Wrapper (caso Linux/Mac)
```   
chmod +x mvnw
```   
#### Rodar a aplicação
```   
./mvnw spring-boot:run
```   

---

## Licença

[![CC BY-NC 4.0](https://licensebuttons.net/l/by-nc/4.0/88x31.png)](https://creativecommons.org/licenses/by-nc/4.0/)

