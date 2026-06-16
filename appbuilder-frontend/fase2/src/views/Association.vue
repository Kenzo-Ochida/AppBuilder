<template>
  <div class="container">
    <h2>Lista de Association:</h2>

    <button @click="openCreateModal">➕ Criar novo Association</button>
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
            v-for="association in associationList"
            :key="association.subjectType"
            class="clickable-row"
          >
            <td>
              {{ association.subjectType }}
              {{ association.associationTypeName }} {{ association.objectType }}
            </td>
            <td class="actions">
              <span
                title="Deletar"
                @click.stop="deleteAssociation(association.subjectType)"
                >🗑️</span
              >
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="loading" class="loading-message">Carregando detalhes...</div>

    <div v-if="associationDetails && !loading" class="details-container">
      <h3>Detalhes do Association: {{ associationDetails.subjectType }}</h3>
      <DataTable :obj="associationDetails" />
    </div>

    <!-- MODAL DE CRIAÇÃO -->
    <section class="form">
      <dialog ref="createDialog" @click.self="closeCreateModal">
        <form @submit.prevent="submitCreate">
          <h3>Criar Novo Association</h3>

          <label
            >subjectType:
            <input
              v-model.trim="createForm.subjectType"
              list="subjectTypeOptions"
              required
            />
          </label>
          <datalist id="subjectTypeOptions">
            <option
              v-for="assetType in assetTypeList"
              :key="assetType"
              :value="assetType"
            />
          </datalist>

          <label
            >objectType:
            <input
              v-model.trim="createForm.objectType"
              list="objectTypeOptions"
              required
            />
          </label>
          <datalist id="objectTypeOptions">
            <option
              v-for="assetType in assetTypeList"
              :key="assetType"
              :value="assetType"
            />
          </datalist>

          <label
            >subjectType: <input v-model.trim="createForm.subjectType" required
          /></label>
          <label
            >associationTypeName:
            <input v-model.trim="createForm.associationTypeName"
          /></label>
          <label
            >objectType: <input v-model.trim="createForm.objectType"
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

        <label
          >subjectType: <input v-model.trim="createForm.subjectType" required
        /></label>
        <label
          >objectType: <input v-model.trim="createForm.objectType"
        /></label>
        <label
          >associationTypeName:
          <input v-model.trim="createForm.associationTypeName"
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

interface Association {
  subjectType: string;
  associationTypeName: string;
  objectType: string;
  domain: string;
}

const associationList = ref<Association[]>([]);
const associationDetails = ref<Association | null>(null);
const loading = ref(false);

const createDialog = ref<HTMLDialogElement | null>(null);
const editDialog = ref<HTMLDialogElement | null>(null);

const assetTypeList = ref<string[]>([]);
const associationTypeList = ref<AssociationType[]>([]);
interface AssociationType {
  name: string;
  description?: string;
  domain: string;
}

const createForm = reactive({
  subjectType: "",
  associationTypeName: "",
  objectType: "",
  domain: "teste",
});

onMounted(() => {
  loadAssociationList();
  loadAssetTypeList();
});

async function loadAssociationList() {
  try {
    const response = await fetch(
      "https://mackleaps.mackenzie.br/asset-api/association/list",
      {
        headers: {
          Accept: "application/json",
          domain: "teste",
          user: "dnani",
          role: "admin-builder",
        },
      },
    );
    if (!response.ok) throw new Error("Erro ao buscar lista de Association");
    associationList.value = await response.json();
  } catch (error) {
    alert(
      "Erro ao carregar a lista de Association. Tente novamente mais tarde.",
    );
    console.error(error);
  }
}

async function loadAssetTypeList() {
  try {
    const response = await fetch(
      "https://mackleaps.mackenzie.br/asset-api/assetType/list",
      {
        headers: {
          Accept: "application/json",
          domain: "teste",
          user: "dnani",
          role: "admin-builder",
        },
      },
    );

    if (!response.ok) throw new Error("Erro ao buscar os asset types");
    assetTypeList.value = await response.json();
  } catch (err) {
    alert(
      "Erro ao carregar a lista de asset types. Tente novamente mais tarde.",
    );
    console.error("Erro ao carregar os dados:", err);
  }
}

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

async function fetchAssociationDetails(associationTypeName: string) {
  loading.value = true;
  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/association/${encodeURIComponent(associationTypeName)}`,
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
    associationDetails.value = data;
  } catch (error) {
    alert("Erro ao carregar detalhes. Tente novamente mais tarde.");
    console.error(error);
    associationDetails.value = null;
  } finally {
    loading.value = false;
  }
}

async function openEditModal(subjectType: string) {
  await fetchAssociationDetails(subjectType);
  if (associationDetails.value) {
    createForm.subjectType = associationDetails.value.subjectType || "";
    createForm.objectType = associationDetails.value.objectType || "";
    createForm.associationTypeName =
      associationDetails.value.associationTypeName || "";
  }
  if (editDialog.value) editDialog.value.showModal();
}

function closeEditModal() {
  if (editDialog.value) editDialog.value.close();
}

async function submitEdit() {
  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/association`,
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
    await loadAssociationList();
    closeEditModal();
  } catch (error) {
    alert("Erro ao salvar alterações. Tente novamente mais tarde.");
    console.error(error);
  }
}

async function deleteAssociation(subjectType: string) {
  if (
    !confirm(
      `Tem certeza que deseja excluir o association type "${subjectType}"?`,
    )
  )
    return;
  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/association/${encodeURIComponent(subjectType)}`,
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
    await loadAssociationList();
  } catch (error) {
    alert("Erro ao excluir association type. Tente novamente mais tarde.");
    console.error(error);
  }
}

function openCreateModal() {
  createForm.subjectType = "";
  createForm.associationTypeName = "";
  createForm.objectType = "";
  if (createDialog.value) createDialog.value.showModal();
}

function closeCreateModal() {
  if (createDialog.value) createDialog.value.close();
}

async function submitCreate() {
  try {
    const payload = {
      subjectType: createForm.subjectType,
      associationTypeName: createForm.associationTypeName,
      objectType: createForm.objectType,
      domain: "teste",
    };

    const response = await fetch(
      "https://mackleaps.mackenzie.br/asset-api/association",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          user: "EnzoTeste",
          role: "admin",
          domain: "teste",
        },
        body: JSON.stringify(payload),
      },
    );

    if (!response.ok) throw new Error("Erro ao criar Association type");

    alert("Association type criado com sucesso!");
    await loadAssociationList();
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
h2,
h3 {
  margin-bottom: 16px;
}

/* Botão primário */
.create-button {
  margin-bottom: 16px;
  padding: 8px 12px;
  font-weight: bold;
  background: #007bff;
  color: white;
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
  color: #007bff;
}

/* Container de detalhes */
.details-container {
  margin-top: 24px;
}

/* Mensagem de carregamento */
.loading-message {
  font-style: italic;
  color: #bbb;
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
  background-color: #161616;
  color: #f0f0f0;
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

/* Estilização escura para campos de formulário */
form input,
textarea,
.attr-row input,
.attr-row select {
  width: 100%;
  padding: 6px;
  margin-top: 4px;
  font-family: monospace;
  background-color: #2a2a2a;
  color: #f0f0f0;
  border: 1px solid #444;
  border-radius: 4px;
}

form input:focus,
textarea:focus,
.attr-row input:focus,
.attr-row select:focus {
  outline: none;
  border-color: #007bff;
  background-color: #333;
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

/* Garante que o cabeçalho fique fixo se quiser (opcional) */
.scrollable-table-wrapper thead th {
  position: sticky;
  top: 0;
  z-index: 1;
}
</style>
