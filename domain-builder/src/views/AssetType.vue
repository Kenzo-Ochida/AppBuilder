<template>
  <div class="container">
    <h2>Lista de Asset Types</h2>

    <button @click="openCreateModal">➕ Criar novo Asset Type</button>
    <div class="scrollable-table-wrapper">
      <table class="asset-table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="name in assetTypeList" :key="name" class="clickable-row">
            <td>{{ name }}</td>
            <td class="actions">
              <span title="Ver detalhes" @click="fetchAssetTypeDetails(name)"
                >🔍</span
              >
              <span title="Editar" @click="openEditModal(name)">✏️</span>
              <span title="Deletar" @click.stop="deleteAssetType(name)"
                >🗑️</span
              >
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="loading" class="loading-message">Carregando detalhes...</div>

    <div v-if="assetTypeDetails && !loading" class="details-container">
      <h3>Detalhes do Asset Type: {{ assetTypeDetails.name }}</h3>
      <DataTable :obj="assetTypeDetails" />
    </div>

    <!-- MODAL DE CRIAÇÃO -->
    <section class="form">
      <dialog ref="createDialog" @click.self="closeCreateModal">
        <form @submit.prevent="submitCreate">
          <h3>Criar Novo Asset Type</h3>

          <label>NAME: <input v-model.trim="createForm.name" required /></label>
          <label>DOMAIN: <input v-model.trim="createForm.domain" /></label>
          <label
            >I18N_NAME_LABEL: <input v-model.trim="createForm.i18nNameLabel"
          /></label>
          <label
            >I18N_CREATEUSER_LABEL:
            <input v-model.trim="createForm.i18nCreateUserLabel"
          /></label>
          <label
            >I18N_DATECREATED_LABEL:
            <input v-model.trim="createForm.i18nDateCreatedLabel"
          /></label>
          <label
            >I18N_DESCRIPTION_LABEL:
            <input v-model.trim="createForm.i18nDescriptionLabel"
          /></label>
          <label
            >I18N_LASTUPDATED_LABEL:
            <input v-model.trim="createForm.i18nLastUpdatedLabel"
          /></label>
          <label
            >I18N_UPDATEUSER_LABEL:
            <input v-model.trim="createForm.i18nUpdateUserLabel"
          /></label>
          <label>ICON: <input v-model.trim="createForm.icon" /></label>
          <label>ICON64: <textarea v-model.trim="createForm.icon64" /></label>

          <label>Attributes (Chave | Label i18n | Tipo):</label>
          <div
            v-for="(attr, index) in createForm.attributes"
            :key="index"
            class="AttributeInput"
          >
            <input v-model.trim="attr.name" placeholder="Chave" required />
            <input
              v-model.trim="attr.label"
              placeholder="Label i18n"
              required
            />
            <select v-model="attr.type" required>
              <option disabled value="">Selecione tipo</option>
              <option v-for="opt in typeOptions" :key="opt" :value="opt">
                {{ opt }}
              </option>
            </select>
            <button
              type="button"
              @click="removeAttributeFrom(createForm, index)"
            >
              –
            </button>
          </div>
          <button type="button" @click="addAttributeToForm(createForm)">
            + Adicionar atributo
          </button>

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
        <h3>Editar Asset Type</h3>

        <label>NAME: <input v-model.trim="editForm.name" required /></label>
        <label>DOMAIN: <input v-model.trim="editForm.domain" /></label>
        <label
          >I18N_NAME_LABEL: <input v-model.trim="editForm.i18nNameLabel"
        /></label>
        <label
          >I18N_CREATEUSER_LABEL:
          <input v-model.trim="editForm.i18nCreateUserLabel"
        /></label>
        <label
          >I18N_DATECREATED_LABEL:
          <input v-model.trim="editForm.i18nDateCreatedLabel"
        /></label>
        <label
          >I18N_DESCRIPTION_LABEL:
          <input v-model.trim="editForm.i18nDescriptionLabel"
        /></label>
        <label
          >I18N_LASTUPDATED_LABEL:
          <input v-model.trim="editForm.i18nLastUpdatedLabel"
        /></label>
        <label
          >I18N_UPDATEUSER_LABEL:
          <input v-model.trim="editForm.i18nUpdateUserLabel"
        /></label>
        <label>ICON: <input v-model.trim="editForm.icon" /></label>
        <label
          >ICON64: <textarea v-model.trim="editForm.icon64" rows="2" />
        </label>

        <label>Attributes (Chave | Label i18n | Tipo):</label>
        <div
          v-for="(attr, idx) in editForm.attributes"
          :key="idx"
          class="AttributeInput"
        >
          <input v-model.trim="attr.name" placeholder="Chave" required />
          <input v-model.trim="attr.label" placeholder="Label i18n" required />

          <select v-model="attr.type" required>
            <option disabled value="">Selecione tipo</option>
            <option v-for="opt in typeOptions" :key="opt" :value="opt">
              {{ opt }}
            </option>
          </select>
          <button type="button" @click="removeAttributeFrom(editForm, idx)">
            –
          </button>
        </div>
        <button type="button" @click="addAttributeToForm(editForm)">
          + Adicionar atributo
        </button>

        <div class="modal-actions">
          <button type="submit">Salvar</button>
          <button type="button" @click="closeEditModal">Cancelar</button>
        </div>
      </form>
    </dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import DataTable from "@/components/DataTable.vue";
import { reactive } from "vue";

interface AssetType {
  name: string;
  description?: string;
  json1?: Record<string, any>;
  json2?: Record<string, any>;
  [key: string]: any;
}

interface AttributeInput {
  name: string;
  label: string;
  type: string;
}

interface AssetTypeForm {
  name: string;
  domain: string;
  i18nNameLabel: string;
  i18nCreateUserLabel: string;
  i18nDateCreatedLabel: string;
  i18nDescriptionLabel: string;
  i18nLastUpdatedLabel: string;
  i18nUpdateUserLabel: string;
  icon: string;
  icon64: string;
  attributes: AttributeInput[];
}

const createForm = reactive<AssetTypeForm>({
  name: "",
  domain: "",
  i18nNameLabel: "",
  i18nCreateUserLabel: "",
  i18nDateCreatedLabel: "",
  i18nDescriptionLabel: "",
  i18nLastUpdatedLabel: "",
  i18nUpdateUserLabel: "",
  icon: "",
  icon64: "",
  attributes: [],
});

const editForm = reactive<AssetTypeForm>({
  name: "",
  domain: "",
  i18nNameLabel: "",
  i18nCreateUserLabel: "",
  i18nDateCreatedLabel: "",
  i18nDescriptionLabel: "",
  i18nLastUpdatedLabel: "",
  i18nUpdateUserLabel: "",
  icon: "",
  icon64: "",
  attributes: [],
});

const assetTypeList = ref<string[]>([]);
const assetTypeDetails = ref<AssetType | null>(null);
const loading = ref(false);
const editDialog = ref<HTMLDialogElement | null>(null);
const createDialog = ref<HTMLDialogElement | null>(null);

const typeOptions = ["string", "number", "boolean", "date"];

onMounted(async () => {
  await loadAssetTypeList();
});

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

async function fetchAssetTypeDetails(name: string) {
  loading.value = true;
  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/assetType/${encodeURIComponent(name)}`,
      {
        headers: {
          Accept: "application/json",
          domain: "teste",
          user: "dnani",
          role: "admin-builder",
        },
      },
    );

    if (!response.ok) throw new Error("Erro ao buscar detalhes do asset type");
    const data = await response.json();
    assetTypeDetails.value = data;
  } catch (err) {
    alert("Erro ao carregar os detalhes. Tente novamente mais tarde.");
    console.error("Erro ao buscar os detalhes:", err);
    assetTypeDetails.value = null;
  } finally {
    loading.value = false;
  }
}

async function deleteAssetType(name: string) {
  const confirmDelete = confirm(
    `Tem certeza de que deseja excluir o asset type "${name}"?`,
  );
  if (!confirmDelete) return;

  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/assetType/${encodeURIComponent(name)}`,
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

    if (!response.ok) throw new Error("Erro ao excluir o asset type");

    alert("Asset type excluído com sucesso!");
    await loadAssetTypeList();
  } catch (err) {
    alert("Erro ao excluir o asset type. Tente novamente mais tarde.");
    console.error("Erro ao excluir:", err);
  }
}

async function openEditModal(name: string) {
  await fetchAssetTypeDetails(name);
  if (assetTypeDetails.value) {
    // Preenche o formulário com os dados obtidos
    editForm.name = assetTypeDetails.value.name;
    editForm.domain = assetTypeDetails.value.domain || "";
    editForm.i18nNameLabel = assetTypeDetails.value.i18nNameLabel || "";
    editForm.i18nCreateUserLabel =
      assetTypeDetails.value.i18nCreateUserLabel || "";
    editForm.i18nDateCreatedLabel =
      assetTypeDetails.value.i18nDateCreatedLabel || "";
    editForm.i18nDescriptionLabel =
      assetTypeDetails.value.i18nDescriptionLabel || "";
    editForm.i18nLastUpdatedLabel =
      assetTypeDetails.value.i18nLastUpdatedLabel || "";
    editForm.i18nUpdateUserLabel =
      assetTypeDetails.value.i18nUpdateUserLabel || "";
    editForm.icon = assetTypeDetails.value.icon || "";
    editForm.icon64 = assetTypeDetails.value.icon64 || "";
    editForm.attributes =
      inverseTransformAttributes(
        assetTypeDetails.value.attributesLabel,
        assetTypeDetails.value.attributesType,
      ) || [];
  }
  if (editDialog.value) editDialog.value.showModal();
}

async function closeEditModal() {
  if (editDialog.value) editDialog.value.close();
}

async function submitEdit() {
  try {
    const { attributesLabel, attributesType } = transformAttributes(
      editForm.attributes,
    );

    const payload = {
      name: editForm.name,
      domain: editForm.domain,
      attributesLabel,
      attributesType,
      icon: editForm.icon,
      icon64: editForm.icon64,
      i18nCreateUserLabel: editForm.i18nCreateUserLabel,
      i18nDateCreatedLabel: editForm.i18nDateCreatedLabel,
      i18nDescriptionLabel: editForm.i18nDescriptionLabel,
      i18nIdLabel: "ID",
      i18nLastUpdatedLabel: editForm.i18nLastUpdatedLabel,
      i18nNameLabel: editForm.i18nNameLabel,
      i18nUpdateUserLabel: editForm.i18nUpdateUserLabel,
    };

    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/assetType`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          domain: "teste",
          user: "dnani",
          role: "admin-builder",
        },
        body: JSON.stringify(payload),
      },
    );

    if (!response.ok) throw new Error("Erro ao salvar as alterações");

    alert("Alterações salvas com sucesso!");
    await loadAssetTypeList();
    if (editDialog.value) editDialog.value.close();
  } catch (err) {
    alert("Erro ao salvar as alterações. Tente novamente mais tarde.");
    console.error("Erro ao editar:", err);
  }
}

async function submitCreate() {
  try {
    const { attributesLabel, attributesType } = transformAttributes(
      createForm.attributes,
    );

    const payload = {
      name: createForm.name,
      domain: createForm.domain,
      attributesLabel,
      attributesType,
      icon: createForm.icon,
      icon64: createForm.icon64,
      i18nCreateUserLabel: createForm.i18nCreateUserLabel,
      i18nDateCreatedLabel: createForm.i18nDateCreatedLabel,
      i18nDescriptionLabel: createForm.i18nDescriptionLabel,
      i18nIdLabel: "ID",
      i18nLastUpdatedLabel: createForm.i18nLastUpdatedLabel,
      i18nNameLabel: createForm.i18nNameLabel,
      i18nUpdateUserLabel: createForm.i18nUpdateUserLabel,
    };

    const response = await fetch(
      "https://mackleaps.mackenzie.br/asset-api/assetType",
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

    if (!response.ok) throw new Error("Erro ao criar asset type");

    alert("Asset type criado com sucesso!");
    closeCreateModal();
  } catch (error) {
    alert("Erro ao criar asset type. Tente novamente mais tarde.");
    console.error(error);
  }
}

function openCreateModal() {
  createForm.name = "";
  createForm.domain = "";
  createForm.i18nNameLabel = "";
  createForm.i18nCreateUserLabel = "";
  createForm.i18nDateCreatedLabel = "";
  createForm.i18nDescriptionLabel = "";
  createForm.i18nLastUpdatedLabel = "";
  createForm.i18nUpdateUserLabel = "";
  createForm.icon = "";
  createForm.icon64 = "";
  createForm.attributes = [];
  if (createDialog.value) createDialog.value.showModal();
}

async function closeCreateModal() {
  if (createDialog.value) createDialog.value.close();
}

// addAttribute
// removeAttribute

function addAttributeToForm(form: { attributes: any[] }) {
  form.attributes.push({ name: "", label: "", type: "" });
}

function removeAttributeFrom(form: { attributes: any[] }, idx: number) {
  form.attributes.splice(idx, 1);
}

function inverseTransformAttributes(
  attributesLabel: Record<string, string>,
  attributesType: Record<string, string>,
): AttributeInput[] {
  return Object.keys(attributesLabel).map((name) => ({
    name,
    label: attributesLabel[name],
    type: attributesType[name],
  }));
}

function transformAttributes(attrs: AttributeInput[]) {
  const attributesLabel: Record<string, string> = {};
  const attributesType: Record<string, string> = {};

  attrs.forEach((attr) => {
    console.log(attr);
    attributesLabel[attr.name] = attr.label;
    attributesType[attr.name] = attr.type;
  });
  return { attributesLabel, attributesType };
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
}

h2 {
  margin-bottom: 16px;
  font-weight: bold;
}

/* Botão primário */
.create-button {
  margin-bottom: 16px;
  padding: 8px 12px;
  font-weight: bold;
  background: #007bff;
  color: #000000;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

/* Tabela */
.asset-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 32px;
}

.asset-table th,
.asset-table td {
  padding: 12px;
  border: 1px solid black;
  text-align: left;
  background-color: #fff;
  color: #000;
}

.asset-table th {
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
.AttributeInput input,
.AttributeInput select {
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
.AttributeInput input:focus,
.AttributeInput select:focus {
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
.AttributeInput {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-bottom: 8px;
}

.AttributeInput input {
  width: 140px;
}

.AttributeInput select {
  width: 160px;
}

.AttributeInput button {
  padding: 4px 10px;
  font-size: 18px;
  font-weight: bold;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.AttributeInput button:hover {
  background: #c82333;
}

.scrollable-table-wrapper {
  max-height: calc(
    5 * 60 px + 52px
  ); /* 5 linhas (~48px cada) + cabeçalho (~52px) */
  overflow-y: auto;
}

/* Garante que o cabeçalho fique fixo  */
.scrollable-table-wrapper thead th {
  position: sticky;
  top: 0;
  z-index: 1;
}

.asset-table th,
.asset-table td {
  padding: 12px 16px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  text-align: left;
  vertical-align: middle;
}

.asset-table th {
  background-color: #e9ecef;
  font-weight: bold;
}

.asset-table .clickable-row:hover {
  background-color: #eef6ff;
  cursor: pointer;
}

.actions span {
  margin-right: 12px;
  cursor: pointer;
  font-size: 18px;
}

/* Scroll wrapper para tabelas */
.scrollable-table-wrapper {
  overflow-x: auto;
  margin-bottom: 24px;
}

/* Detalhes */
.details-container {
  margin-top: 32px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}

/* Diálogos / Modais */
dialog {
  border: none;
  border-radius: 12px;
  padding: 24px;
  max-width: 600px;
  width: 100%;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

form label {
  display: block;
  margin: 12px 0 4px;
}

form input,
form select,
form textarea {
  width: 100%;
  padding: 8px;
  margin-bottom: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
}

/* Inputs de atributos */
.AttributeInput {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-bottom: 8px;
}

.AttributeInput input,
.AttributeInput select {
  flex: 1;
}

.AttributeInput button {
  padding: 6px 10px;
  border: none;
  background-color: #dc3545;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}

button {
  padding: 8px 14px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 16px;
}

.modal-actions button {
  background-color: #007bff;
  color: white;
}

.modal-actions button[type="button"] {
  background-color: #6c757d;
}

/* Mensagem de carregamento */
.loading-message {
  font-style: italic;
  margin-top: 12px;
}
</style>
