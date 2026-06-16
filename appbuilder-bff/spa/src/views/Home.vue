<template>
  <div class="container">
    <h1>🏠 Welcome to the Home Page</h1>
    <p>You are authenticated.</p>
    <button @click="logout">Logout</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

function logout() {
  router.push('/logout')
}
</script>
