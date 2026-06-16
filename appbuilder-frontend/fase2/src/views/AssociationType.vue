<template>
  <div class="container">
    <div class="subTitle">
      <h2>Association Types</h2>
      <button @click="openCreateModal">➕ Criar novo Association Type</button>
    </div>

    <div class="scrollable-table-wrapper">
      <table class="association-table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="associationType in associationTypeList"
            :key="associationType.name"
            class="clickable-row"
          >
            <td>{{ associationType.name }}</td>
            <td class="actions">
              <span
                title="Ver detalhes"
                @click="fetchAssociationTypeDetails(associationType.name)"
                >🔍</span
              >
              <span title="Editar" @click="openEditModal(associationType.name)"
                >✏️</span
              >
              <span
                title="Deletar"
                @click.stop="deleteAssociationType(associationType.name)"
                >🗑️</span
              >
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="loading" class="loading-message">Carregando detalhes...</div>

    <div v-if="associationTypeDetails && !loading" class="details-container">
      <h3>Detalhes do Association Type: {{ associationTypeDetails.name }}</h3>
      <DataTable :obj="associationTypeDetails" />
    </div>

    <!-- MODAL DE CRIAÇÃO -->
    <section class="form">
      <dialog ref="createDialog" @click.self="closeCreateModal">
        <form @submit.prevent="submitCreate">
          <h3>Criar Novo Association Type</h3>

          <label>NAME: <input v-model.trim="createForm.name" required /></label>
          <label
            >DESCRIPTION: <input v-model.trim="createForm.description"
          /></label>

          <div class="modal-actions">
            <button type="submit" :disabled="loading">Criar</button>
            <button type="button" @click="closeCreateModal">Cancelar</button>
          </div>
        </form>
      </dialog>
    </section>

    <!-- MODAL DE EDIÇÃO -->
    <dialog ref="editDialog" @click.self="closeEditModal">
      <form @submit.prevent="submitEdit">
        <h3>Editar Association Type</h3>

        <label>NAME: <input v-model.trim="createForm.name" required /></label>
        <label
          >DESCRIPTION: <input v-model.trim="createForm.description"
        /></label>

        <div class="modal-actions">
          <button type="submit">Salvar</button>
          <button type="button" @click="closeEditModal">Cancelar</button>
        </div>
      </form>
    </dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import DataTable from "@/components/DataTable.vue";

interface AssociationType {
  name: string;
  description?: string;
  domain: string;
}

const associationTypeList = ref<AssociationType[]>([]);
const associationTypeDetails = ref<AssociationType | null>(null);
const loading = ref(false);

const createDialog = ref<HTMLDialogElement | null>(null);
const editDialog = ref<HTMLDialogElement | null>(null);

const createForm = reactive({
  name: "",
  description: "",
  domain: "teste",
});

onMounted(() => {
  loadAssociationTypeList();
});

async function loadAssociationTypeList() {
  try {
    const response = await fetch(
      "https://mackleaps.mackenzie.br/asset-api/associationType/list",
      {
        headers: {
          Accept: "application/json",
          domain: "teste",
          user: "dnani",
          role: "admin-builder",
        },
      },
    );
    if (!response.ok)
      throw new Error("Erro ao buscar lista de association types");
    associationTypeList.value = await response.json();
  } catch (error) {
    alert(
      "Erro ao carregar a lista de association types. Tente novamente mais tarde.",
    );
    console.error(error);
  }
}

async function fetchAssociationTypeDetails(name: string) {
  loading.value = true;
  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/associationType/${encodeURIComponent(name)}`,
      {
        headers: {
          Accept: "application/json",
          domain: "teste",
          user: "dnani",
          role: "admin-builder",
        },
      },
    );
    if (!response.ok)
      throw new Error("Erro ao buscar detalhes do association type");
    const data = await response.json();
    associationTypeDetails.value = data;
  } catch (error) {
    alert("Erro ao carregar detalhes. Tente novamente mais tarde.");
    console.error(error);
    associationTypeDetails.value = null;
  } finally {
    loading.value = false;
  }
}

async function openEditModal(name: string) {
  await fetchAssociationTypeDetails(name);
  if (associationTypeDetails.value) {
    createForm.name = associationTypeDetails.value.name;
    createForm.description = associationTypeDetails.value.description || "";
  }
  if (editDialog.value) editDialog.value.showModal();
}

function closeEditModal() {
  if (editDialog.value) editDialog.value.close();
}

async function submitEdit() {
  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/associationType`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          domain: "teste",
          user: "dnani",
          role: "admin-builder",
        },
        body: JSON.stringify(createForm),
      },
    );
    if (!response.ok) throw new Error("Erro ao salvar alterações");
    alert("Alterações salvas com sucesso!");
    await loadAssociationTypeList();
    closeEditModal();
  } catch (error) {
    alert("Erro ao salvar alterações. Tente novamente mais tarde.");
    console.error(error);
  }
}

async function deleteAssociationType(name: string) {
  if (!confirm(`Tem certeza que deseja excluir o association type "${name}"?`))
    return;
  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/associationType/${encodeURIComponent(name)}`,
      {
        method: "DELETE",
        headers: {
          Accept: "application/json",
          domain: "teste",
          user: "dnani",
          role: "admin-builder",
        },
      },
    );
    if (!response.ok) throw new Error("Erro ao excluir association type");
    alert("Association type excluído com sucesso!");
    await loadAssociationTypeList();
  } catch (error) {
    alert("Erro ao excluir association type. Tente novamente mais tarde.");
    console.error(error);
  }
}

function openCreateModal() {
  createForm.name = "";
  createForm.description = "";
  if (createDialog.value) createDialog.value.showModal();
}

function closeCreateModal() {
  if (createDialog.value) createDialog.value.close();
}

async function submitCreate() {
  try {
    const payload = {
      name: createForm.name,
      description: createForm.description,
      domain: "teste",
    };

    const response = await fetch(
      "https://mackleaps.mackenzie.br/asset-api/associationType",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          user: "dnani",
          role: "admin",
          domain: "teste",
        },
        body: JSON.stringify(payload),
      },
    );

    if (!response.ok) throw new Error("Erro ao criar Association type");

    alert("Association type criado com sucesso!");
    await loadAssociationTypeList();
    closeCreateModal();
  } catch (error) {
    alert("Erro ao criar Association type. Tente novamente mais tarde.");
    console.error(error);
  }
}
</script>

<style scoped>
/* Layout Base */
.container {
  padding: 24px;
  color: #000000; /* texto geral claro */
}

/* Títulos */

h3 {
  margin-bottom: 16px;
  font-weight: 500;
}

h2 {
  font-weight: bold;
  text-align: center;
  padding-top: 5%;
  padding-bottom: 5%;
}

h2 {
  margin-bottom: 16px;
  font-weight: bold;
}

h2 {
  margin-bottom: 16px;
  font-weight: bold;
}

/* Botão primário */
.button_create {
  margin-bottom: 16px;
  padding: 8px 12px;
  font-weight: bold;
  color: black;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

/* Tabela */
.association-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 32px;
}

.association-table th,
.association-table td {
  padding: 12px;
  border: 1px solid black;
  text-align: left;
  background-color: #fff;
  color: #000;
}

.association-table th {
  background-color: #f0f0f0;
  font-weight: bold;
}

/* Linhas clicáveis */
.clickable-row {
  transition: background-color 0.2s ease;
}

.clickable-row:hover {
  background-color: #f9fafb5e;
}

/* Ações com ícones */
.actions span {
  cursor: pointer;
  margin-right: 12px;
  font-size: 18px;
  transition: color 0.2s;
}

.actions span:hover {
  color: #e1001e;
}

/* Container de detalhes */
.details-container {
  margin-top: 24px;
}

/* Mensagem de carregamento */
.loading-message {
  font-style: italic;
  color: black;
}

/* Modal */
dialog {
  padding: 20px;
  margin-top: 2%;
  margin-left: 2%;
  border: none;
  border-radius: 8px;
  flex: content;
  width: 100%;
  max-width: 600px;
  background-color: white;
  color: black;
  box-shadow: 5px 5px 5px 5px #2a2a2a;
}

/* Formulário */
form label {
  display: block;
  margin-bottom: 12px;
}

.form {
  box-shadow: 20% red;
}

/* Estilização clara para campos de formulário */
form input,
textarea,
.attr-row input,
.attr-row select {
  width: 100%;
  padding: 6px;
  margin-top: 4px;
  font-family: monospace;
  background-color: white;
  color: black;
  border: 1px solid #f0f0f0;
  border-radius: 4px;
}

form input:focus,
textarea:focus,
.attr-row input:focus,
.attr-row select:focus {
  outline: none;
  border-color: #e1001e;
  background-color: #ffffff;
}

/* Botões do modal */
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 16px;
}

/* Atributos dinâmicos */
.attr-row {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-bottom: 8px;
}

.attr-row input {
  width: 140px;
}

.attr-row select {
  width: 160px;
}

.attr-row button {
  padding: 4px 10px;
  font-size: 18px;
  font-weight: bold;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.attr-row button:hover {
  background: #c82333;
}

.scrollable-table-wrapper {
  max-height: calc(
    5 * 60 px + 52px
  ); /* 5 linhas (~48px cada) + cabeçalho (~52px) */
  overflow-y: auto;
}

.scrollable-table-wrapper thead th {
  position: sticky;
  top: 0;
  z-index: 1;
}
</style>
