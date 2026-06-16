<script setup lang="ts">
import { ref } from "vue";
import { RouterLink, RouterView } from "vue-router";
import HelloWorld from "./components/HelloWorld.vue";

const sidebarOpen = ref(true);
const toggleSidebar = () => {
  sidebarOpen.value = !sidebarOpen.value;
};
</script>

<template>
  <div class="layout">
    <!-- Sidebar com toggle -->
    <aside :class="{ open: sidebarOpen, closed: !sidebarOpen }">
      <button class="toggle-btn" @click="toggleSidebar">☰</button>
      <nav v-show="sidebarOpen">
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/associationType">Association Types</RouterLink>
        <RouterLink to="/assetType">Asset Types</RouterLink>
        <RouterLink to="/detailsRealm">Details Realm</RouterLink>
        <RouterLink to="/forms">Forms</RouterLink>
        <RouterLink to="/users">Users</RouterLink>
        <RouterLink to="/group">Group</RouterLink>
      </nav>
    </aside>

    <!-- Conteúdo principal -->
    <div class="main-content">
      <header class="header">
        <HelloWorld msg="APP BUILDER" />
        <div class="logo-container">
          <a
            href="https://computacao.mackenzie.br/mackleaps/"
            target="_blank"
            rel="noopener noreferrer"
          >
            <img
              alt="FCI logo"
              class="logo"
              src="@/assets/Logo_MackLEAPS_transparente.png"
              width="90"
              height="90"
            />
          </a>
          <a
            href="https://computacao.mackenzie.br/"
            target="_blank"
            rel="noopener noreferrer"
          >
            <img
              alt="FCI logo"
              class="logo"
              src="@/assets/logoFCTransparente.png"
              width="90"
              height="90"
            />
          </a>
        </div>
      </header>
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
:global(body, html) {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

.layout {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* Sidebar */
aside {
  background-color: #f8f8f8;
  padding: 1rem;
  width: 220px;
  transition:
    width 0.3s,
    background-color 0.3s,
    padding 0.3s;
  overflow: hidden;
  position: relative;
  height: 100%;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
}

aside.closed {
  width: 50px;
  padding: 1rem 0.5rem;
  background-color: transparent;
  box-shadow: none;
}

aside nav {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 3rem;
}

aside a {
  text-decoration: none;
  color: #000;
  font-weight: 400;
}

aside a.router-link-exact-active {
  color: #c60000;
  font-weight: 600;
}

.toggle-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 1;
}

/* Conteúdo principal */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 2rem;
  overflow-y: auto;
  background-color: #fff;
}

/* Cabeçalho */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 1rem;
}

.logo-container {
  order: 2;
}

.logo {
  width: 90px;
  height: auto;
}
</style>
