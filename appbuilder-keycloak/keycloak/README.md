
# Iniciando o Keycloak com Docker

Suba o container com:

```bash
docker compose up -d
```

Acesse o painel de administração em:

```
http://localhost:8080/keycloak
```

* **Usuário:** `admin`
* **Senha:** `admin`

---

## 2. Configurando Realm e Client

### Criando um Realm

Na barra lateral esquerda, clique em **"Create realm"**.

* **Realm name:** `GC`

> *Realms* são domínios que contêm usuários, funções e aplicações (chamadas de *clients*) que interagem entre si.
> Os *clients* são as aplicações que usarão o Keycloak para autenticação, enquanto os *users* são os usuários finais que irão se autenticar.

---

### Criando um Client para o Gerenciador de Competências

1. Vá em **"Clients"**
2. Clique em **"Create client"**
3. Em **Client ID**, digite: `GP-client`
4. Clique em **Next** duas vezes
5. Preencha os campos da seguinte forma:

* **Root URL:** `http://localhost:5173/`
* **Home URL:** `http://localhost:5173/`
* **Valid redirect URIs:**

  ```
  http://localhost:5173/*
  http://localhost:5173/
  ```
* **Valid post logout redirect URIs:**

  ```
  http://localhost:5173/
  ```
* **Web origins:**

  ```
  http://localhost:5173/
  ```

6. Clique em **Save**

---

## Criando Usuários

1. No menu lateral, clique em **"Users"**
2. Clique em **"Add user"**
3. Preencha os campos:

   * **Username:** (ex: `joao.silva`)
   * **Email:** (ex `joao.silva@email.com`)
   * **First name** e **Last name:** (ex: `João`) (`Silva`)
   * Marque o checkbox **"Email Verified"** (ex: `joao.silva@email.com`)
4. Clique em **Save**
5. Vá até a aba **"Credentials"**
6. Defina uma senha para o usuário:

   * Digite a senha (ex: `senha123`)
   * Marque **Temporary: OFF**
   * Clique em **"Set Password"**

> Agora o usuário poderá se autenticar com as credenciais definidas.

---

## Verificando – Obtendo Token via `curl`

Podemos fazer uma requisição `curl` para verificar se a autenticação está funcionando corretamente, trocando o login e a senha por um **access token**.

Execute o seguinte comando no terminal (substitua os valores conforme necessário):

```bash
curl -X POST http://localhost:8080/keycloak/realms/GC/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=GP-client" \
  -d "grant_type=password" \
  -d "username=joao.silva" \
  -d "password=senha123"
```

### Explicação dos parâmetros:

* `client_id`: ID do client configurado no Keycloak
* `grant_type`: tipo de fluxo (aqui usamos `password`)
* `username` e `password`: credenciais do usuário criado

---

### Exemplo de resposta esperada:

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9....",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9....",
  "token_type": "Bearer",
  "session_state": "...",
  "scope": "email profile"
}
```

---

## Trocando o Tema de Login da Realm `master`

Você já possui dois temas customizados:

* `tagarela`
* `fisio-avc`

Vamos aplicar um deles ao login da Realm `master`.

---

### 1. Verifique o Local dos Temas

Certifique-se de que seus temas estão localizados em:

```bash
./themes/
```

Exemplo:

```
./themes/tagarela
./themes/fisio-avc
```

Estrutura esperada:

```
fisio-avc/
└── login/
    ├── theme.properties
    ├── login.ftl
    └── ...
```

---

### 2. Alterando o Tema no Admin Console

1. Acesse o Admin Console: `http://localhost:8080/keycloak`
2. Selecione a realm `master`
3. Vá em **Realm Settings** > **Themes**
4. Em **Login Theme**, selecione `tagarela` ou `fisio-avc`
5. Clique em **Save**

---

### 3. Reinicie o Container (se necessário)

Se o tema não aparecer na lista, reinicie o Keycloak:

```bash
docker compose restart
```

---

### 4. Teste

Abra a URL de login da realm para testar o tema aplicado:

```
http://localhost:8080/realms/master/account
```

---

Perfeito! Aqui está a última seção que você pode adicionar ao final da sua documentação, incentivando os leitores a explorar a criação de seus próprios temas com base nos que já existem:

---

## Criando Seu Próprio Tema Personalizado

Agora que você já tem os temas `tagarela` e `fisio-avc` disponíveis e funcionando, uma ótima forma de entender como o Keycloak lida com personalização é **criar o seu próprio tema** com base nesses exemplos.

### Passos sugeridos:

1. Acesse o diretório onde os temas estão salvos:

```bash
cd ./themes/
```

2. Copie um dos temas existentes:

```bash
cp -r tagarela meu-tema-personalizado
```

3. Renomeie e edite os arquivos conforme desejar:

* Edite o arquivo `theme.properties` e altere o valor de `theme name`.
* Modifique o layout em `login.ftl`, os estilos em `resources/css/`, e qualquer imagem ou script.

4. Salve as alterações e, se necessário, reinicie o container:

```bash
docker compose restart
```

5. Vá até o Admin Console e, na seção **Realm Settings > Themes**, selecione o seu novo tema em **Login Theme** e clique em **Save**.

### Dica

Você pode testar pequenas alterações nos arquivos `.ftl` e `.css` e atualizar a página de login para ver as mudanças em tempo real (caso o cache de tema esteja desabilitado).

---


