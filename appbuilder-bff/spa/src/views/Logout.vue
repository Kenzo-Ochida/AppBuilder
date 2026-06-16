<template>
  <div class="container">
    <h1>Saindo...</h1>
    <p>Você está sendo redirecionado.</p>
  </div>
</template>

<script setup>
import { onMounted } from "vue";

// Redireciona para o BFF, que invalida a sessão e chama o logout do Keycloak
onMounted(() => {
  window.location.href =
    "http://localhost:8080/keycloak/realms/Appbuilder/protocol/openid-connect/logout";
});
</script>

<style scoped>
.container {
  text-align: center;
  padding-top: 100px;
  font-family: sans-serif;
}
</style>
