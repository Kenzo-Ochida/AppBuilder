<template>
  <div class="p-4">
    <h2 class="text-xl font-bold mb-4">Tabela de Dados</h2>

    <table v-if="dados.length" class="table-auto w-full border border-gray-300">
      <thead class="bg-gray-100">
        <tr>
          <th
            class="border px-4 py-2"
            v-for="(value, key) in dados[0]"
            :key="key"
          >
            {{ key }}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(linha, i) in dados" :key="i">
          <td
            class="border px-4 py-2"
            v-for="(valor, chave) in linha"
            :key="chave"
          >
            {{ valor }}
          </td>
        </tr>
      </tbody>
    </table>

    <p v-else>Carregando dados...</p>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";

const dados = ref([]);

onMounted(async () => {
  const response = await fetch("/src/assets/realm1.json");
  dados.value = await response.json();
});
</script>
