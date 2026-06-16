<template>
  <table class="custom-table">
    <thead>
      <tr>
        <th>Chave</th>
        <th>Valor</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(valor, chave) in obj" :key="chave">
        <td>
          {{ chave }}
        </td>
        <td>
          <template v-if="isPrimitive(valor)">
            {{ valor }}
          </template>
          <template v-else>
            <DataTable :obj="valor" />
          </template>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup lang="ts">
defineProps<{
  obj: Record<string, any>;
}>();

const isPrimitive = (value: any) => {
  return (
    typeof value === "string" ||
    typeof value === "number" ||
    typeof value === "boolean" ||
    value === null
  );
};
</script>

<style scoped>
.custom-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 12px;
}

.custom-table th,
.custom-table td {
  padding: 12px;
  border: 1px solid white;
  vertical-align: top;
  text-align: left;
}

.custom-table th {
  font-weight: bold;
}

.custom-table tr {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  border-radius: 8px;
}
</style>
