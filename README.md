# Plataforma de Crowdsourcing para Mapear Apagões

Este projeto é uma API RESTful desenvolvida com Java 17 e Spring Boot 3, cujo objetivo é permitir que usuários registrem e consultem relatos de **apagões de energia elétrica** por região, com foco em **monitoramento colaborativo**.

## 🧠 Autores
 - Deivison Pertel          - RM550803
 - Eduardo Akira Murata     - RM98713
 - Wesley Souza de Oliveira - RM97874


---

## 🧱 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security + JWT
- H2 Database
- Springdoc OpenAPI (Swagger)
- Maven

---

## 📦 Funcionalidades da API

- 🔐 Autenticação com JWT (`/auth/login`)
- 🧾 CRUD de apagões:
  - `GET /apagao` – lista todos os apagões
  - `GET /apagao/{id}` – busca apagão por ID
  - `POST /apagao` – cria novo apagão (autenticado)
  - `PUT /apagao/{id}` – atualiza apagão existente (autenticado)
  - `DELETE /apagao/{id}` – remove apagão por ID (autenticado)

---

## 📌 Validações

- Todos os campos são obrigatórios, exceto `dataHora`
- A data e hora do apagão é sempre preenchida com a **data atual do sistema**, independentemente do que o usuário enviar

---

## 🔐 Autenticação

### Usuário em memória:
- **Username:** `admin`
- **Senha:** `123456`

### Exemplo de login:
```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

Resposta:
```json
{
  "token": "Bearer eyJhbGciOiJIUzI1NiJ9..."
}
```

Use esse token nas rotas protegidas:
```
Authorization: Bearer <seu_token>
```

---

## 💾 Banco de Dados H2

- Acesso via: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: *(em branco)*

---

## 📘 Documentação Swagger

Acesse via:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---


---

## 🌐 Integrações com APIs Públicas

O projeto realiza integração com duas APIs externas para enriquecimento automático dos dados dos apagões:

### 📍 ViaCEP
Utilizada para obter informações de localização (bairro, cidade, estado) a partir do **CEP informado** pelo usuário.

### 🌦️ Open-Meteo
Consulta em tempo real o **clima atual** (temperatura e condição climática por código) com base na geolocalização (latitude e longitude), usando a API gratuita da Open-Meteo, sem necessidade de chave de autenticação.

### 🔄 Fluxo de enriquecimento:
1. Usuário envia: **CEP** e **Descrição**
2. A API do ViaCEP corrige e padroniza os dados de localização
3. A OpenWeatherMap consulta o clima baseado na cidade e UF
4. O apagão é salvo com:
   - Cidade e estado corrigidos
   - Temperatura
   - Condição climática
   - Data/hora atual

**Exemplo de resposta com enriquecimento usando Open-Meteo e ViaCEP:**
```json
{
	"id": 1,
	"cep": "01310-100",
	"rua": "Avenida Paulista",
	"bairro": "Bela Vista",
	"cidade": "São Paulo",
	"estado": "SP",
	"dataHora": "2025-05-27T23:06:55.0941925",
	"descricao": "Apagão completo por mais de 2 horas",
	"condicaoClimatica": "Nublado",
	"temperatura": 19.1
}
```


## ▶️ Como executar o projeto

```bash
# compilar o projeto
mvn clean install

# rodar a aplicação
mvn spring-boot:run
```

---

## 📝 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/apagao/cidadao/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── config/
│   └── resources/
│       ├── application.properties
├── test/...
```

---


## 📷 Evidências de Funcionamento

### ▶️ Execução de compilação das dependências
> Demonstração do terminal apresentando `Build Sucess` após executar `mvn clean compile`.

![Execução da compilação no terminal](assets/compilacao.png)

---

### ▶️ Execução da aplicação
> Demonstração do terminal com a aplicação rodando no `localhost:8080`.

![Execução da aplicação no terminal](assets/execucao.png)

---

### 📘 Swagger UI – Documentação da API
> Acesso e visualização das rotas documentadas com OpenAPI no Swagger.

![Swagger - documentação da API](assets/swagger.png)

---

### 🔐 Login com JWT (Insomnia)
> Autenticação bem-sucedida via `/auth/login`, com retorno do token JWT.

![Insomnia - login com JWT](assets/token.png)

---

### 📩 Criação de Apagão com enriquecimento de dados (Insomnia)
> Envio de requisição com CEP e descrição. A resposta inclui dados enriquecidos via ViaCEP e Open-Meteo.

![Insomnia - criação de apagão com resposta enriquecida](assets/post-requisicao.png)

Obs: Para fazer o Post é necessário passar um `Bearer Token`, com o Token gerado na autenticação. Caso contrário a chamada retornará um 403 - Forbiden

![alt text](assets/forbiden.png)

---

### 📩 Registros de Apagão com enriquecimento de dados (Insomnia)
> Envio de requisição GET. A resposta inclui Registros de Apagão com enriquecimento de dados.

![Insomnia - registros de apagão com enriquecimento de dados](assets/get-requisicao.png)

---

### 📄 Exemplo de resposta JSON enriquecida:
```json
{
  "id": 1,
  "cep": "01310-100",
  "rua": "Avenida Paulista",
  "bairro": "Bela Vista",
  "cidade": "São Paulo",
  "estado": "SP",
  "dataHora": "2025-05-27T23:06:55",
  "descricao": "Apagão completo por mais de 2 horas",
  "condicaoClimatica": "Nublado",
  "temperatura": 19.1
}
```


## 📄 Licença

Projeto acadêmico desenvolvido para a FIAP - Global Solution 2025.
