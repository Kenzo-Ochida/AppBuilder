<template>
  <div class="p-4">
    <h2 class="text-xl font-bold mb-4">Detalhes do Realm</h2>

    <DataTable v-if="dados" :obj="dados" />
    <p v-else>Carregando dados...</p>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import DataTable from "@/components/DataTable.vue";

const dados = ref<Record<string, any> | null>(null);

onMounted(async () => {
  try {
    const response = await fetch("/realm2.json");
    dados.value = await response.json();
  } catch (err) {
    console.error("Erro ao carregar JSON:", err);
  }
});
</script>
