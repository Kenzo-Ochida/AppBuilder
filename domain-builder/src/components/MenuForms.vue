<template>
  <div class="flex">
    <!-- Menu lateral -->
    <ul class="w-64 bg-gray-100 p-4 border-r">
      <li
        v-for="form in forms"
        :key="form.id"
        class="cursor-pointer p-2 hover:bg-gray-200"
        :class="{ 'bg-blue-100': selectedForm?.id === form.id }"
        @click="selectedForm = form"
      >
        {{ form.name }}
      </li>
    </ul>

    <!-- Formulário dinâmico -->
    <div class="flex-1 p-4">
      <DynamicForm v-if="selectedForm" :form="selectedForm" />
      <p v-else>Selecione um formulário no menu.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";
import DynamicForm from "./DynamicForm.vue";

interface Field {
  label: string;
  type: string;
  model: string;
}

interface Form {
  id: string;
  name: string;
  fields: Field[];
}

const forms = ref<Form[]>([]);
const selectedForm = ref<Form | null>(null);

onMounted(async () => {
  const response = await axios.get<Form[]>("/api/forms");
  forms.value = response.data;
});
</script>
