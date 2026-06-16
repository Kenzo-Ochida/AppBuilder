## Get Access Token

Use the OpenID Connect token endpoint to obtain an access token. Include your One-Time Password only if two-factor authentication is enabled:

```bash
curl -sS -X POST "https://mackleaps.mackenzie.br/keycloak/realms/master/protocol/openid-connect/token" \
  -d "client_id=<CLIENT_ID>" \
  -d "username=<USERNAME>" \
  -d "password=<PASSWORD>" \
  -d "grant_type=password" \
  -d "otp=<OTP_CODE>"  # include if 2FA is enabled
```

---
## Create a New Realm

Send a `POST` request to the Admin REST API’s realms endpoint. Be sure to remove all comments from the JSON file, since JSON does not support them:

```bash
curl -sS -X POST "https://mackleaps.mackenzie.br/keycloak/admin/realms" \
  -H "Authorization: Bearer $(<token)" \
  -H "Content-Type: application/json" \
  -d @novo-realm.json
```

Contents of **novo-realm.json**:

```json
{
  "id": "meureino",
  "realm": "meureino",
  "displayName": "Meu Reino",
  "enabled": true,
  "loginWithEmailAllowed": true,
  "resetPasswordAllowed": true,
  "editUsernameAllowed": true,
  "internationalizationEnabled": true,
  "supportedLocales": ["pt-BR", "en"],
  "loginTheme": "keycloak.v2",
  "smtpServer": {
    "from": "yourapplication@mackenzie.br",
    "fromDisplayName": "Keycloak",
    "host": "SMTP-IP",
    "port": "SMTP-PORT",
    "auth": "false",
    "ssl": "false",
    "starttls": "false"
  }
}
```

---
## Create a New Client

Use the Admin REST API to register a new client within your realm:

```bash
curl -sS -L -X POST "https://mackleaps.mackenzie.br/keycloak/admin/realms/<your realme>/clients" \
  -H "Authorization: Bearer $(<token)" \
  -H "Content-Type: application/json" \
  -d @novo-cliente.json
```

Example **novo-cliente.json**:

```json
{
  "clientId": "meu-cliente",
  "enabled": true,
  "protocol": "openid-connect",
  "publicClient": true,
  "redirectUris": ["https://example.com/*"],
  "webOrigins": ["https://example.com"],
  "baseUrl": "https://example.com",
  "standardFlowEnabled": true,
  "implicitFlowEnabled": false,
  "directAccessGrantsEnabled": true,
  "serviceAccountsEnabled": false,
  "rootUrl": "https://example.com"
}
```

---
## Create a New User

1. **Create the user**  
   ```bash
   curl -sS -X POST \
     -H "Authorization: Bearer $(<token)" \
     -H "Content-Type: application/json" \
     -d @novo-usuario.json \
     https://mackleaps.mackenzie.br/keycloak/admin/realms/<your realme>/users
   ```
   **novo-usuario.json**:
   ```json
   {
     "username": "<USERNAME>",
     "email": "<EMAIL>",
     "enabled": true
   }
   ```
2. **Retrieve the user ID**  
   ```bash
   USER_ID=$(curl -sS \
     -H "Authorization: Bearer $(<token)" \
     "https://mackleaps.mackenzie.br/keycloak/admin/realms/<your realme>/users?username=<username>" \
     | jq -r '.[0].id')
   ```
3. **Send a verification email**  
   ```bash
   curl -sS -X PUT \
     -H "Authorization: Bearer $(<token)" \
     https://mackleaps.mackenzie.br/keycloak/admin/realms/<your realme>/users/<user id>/send-verify-email
   ```
4. **Send a password reset email**  
   ```bash
   curl -sS -X PUT \
     -H "Authorization: Bearer $(<token)" \
     -H "Content-Type: application/json" \
     -d '["UPDATE_PASSWORD"]' \
     https://mackleaps.mackenzie.br/keycloak/admin/realms/<your realme>/users/<user id>/execute-actions-email
   ```
*For other administrative actions, replace `"UPDATE_PASSWORD"` with the appropriate action name (e.g., `"VERIFY_EMAIL"`, `"CONFIGURE_TOTP"`).*  
