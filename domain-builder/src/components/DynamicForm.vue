<template>
  <form @submit.prevent="handleSubmit" class="space-y-4">
    <div v-for="field in form.fields" :key="field.model">
      <label class="block font-semibold">{{ field.label }}</label>
      <input
        v-model="formData[field.model]"
        :type="field.type"
        class="w-full p-2 border rounded"
      />
    </div>
    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">
      Enviar
    </button>
  </form>
</template>

<script setup lang="ts">
import { defineProps, reactive } from "vue";

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

const props = defineProps<{
  form: Form;
}>();

const formData = reactive<Record<string, string>>({});

for (const field of props.form.fields) {
  formData[field.model] = "";
}

function handleSubmit() {
  console.log("Enviando dados:", formData);
  // POST para uma API
}
</script>
