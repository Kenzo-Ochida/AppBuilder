# AppBuilderBFF REFACTORED

Integrated with Keycloack, Redis and VueJS. Tested in localhost.

1 - Run **Keycloak** (at `http://localhost:8080`) with

```bash
cd keycloak
docker compose up -d
```

**Attention:** Create a realm, a client and a user following the README at the main AppBuilderBFF project in GitLab.

2 - Run the **SPA** (at `http://localhost:5173`) with

```bash
cd spa
npm run dev
```

3 - Run **Redis** (at `http://localhost:6379`) with

```bash
cd redis
docker compose up -d
```

4 - Run the **BFF** (at `http://localhost:8090`) with

```bash
mvn clean install
mvn spring-boot:run
```

Now you should be able to login, being redirected to Keycloak, and logout. The session should be saved only in the BFF (without access to the user in the frontend).
