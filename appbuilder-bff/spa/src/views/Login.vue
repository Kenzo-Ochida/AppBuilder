<template>
  <div class="container">
    <h1>Login</h1>
    <button @click="redirectToLogin">Fazer login</button>
  </div>
</template>

<script>
export default {
  name: "Login",
  methods: {
    redirectToLogin() {
      // Redireciona para o BFF, que iniciará o fluxo OAuth2 com Keycloak
      window.location.href = "http://localhost:8090/auth/redirect";
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 100px;
}

button {
  padding: 10px 20px;
  font-size: 16px;
}
</style>
