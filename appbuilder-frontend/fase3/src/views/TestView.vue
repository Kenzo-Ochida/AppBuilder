<template>
  <div class="about">
    <h1>{{ selectedName || 'This is a test' }}</h1>
    <List :key="selectedName" :obj="assetTypeList" @item-click="quandoClicar" />
  </div>

  <div v-if="selectedName !== ''">
    <div v-if="nameAssetList.length > 0">
      <List :key="selectedName" :obj="nameAssetList" @item-click="quandoClicar" />
    </div>
    <div v-else>
      <h1>This asset type doesn't have any asset</h1>
    </div>
  </div>
</template>

<script setup lang="ts">
// Overall application logic

import List from '@/components/List.vue'
import { computed, onMounted, ref } from 'vue'

onMounted(async () => {
  await loadAssetTypeList()
  await loadAssociationTypeList()
})

const selectedName = ref('')

function quandoClicar(value: string) {
  selectedName.value = value
  loadAssetsByType(selectedName.value)
}

// Assets

interface Asset {
  name: string
  description?: string
  domain: string
}

const assetList = ref<Asset[]>([])
const nameAssetList = computed(() => assetList.value.map((asset) => asset.name))

async function loadAssetsByType(type: string) {
  try {
    const response = await fetch(
      `https://mackleaps.mackenzie.br/asset-api/asset/listByType/${encodeURIComponent(type)}`,
      {
        method: 'GET',
        headers: {
          Accept: 'application/json',
          domain: 'teste',
          user: 'dnani',
          role: 'admin-builder',
        },
      },
    )

    if (response.status === 204) {
      // Nenhum conteudo: a lista fica vazia
      assetList.value = []
      return
    }

    if (!response.ok) throw new Error('Erro ao buscar os assets')

    // Atualiza a lista com os assets retornados
    assetList.value = await response.json()
  } catch (error) {
    alert('Erro ao carregar os assets. Tente novamente mais tarde.')
    console.error('Erro:', error)
  }
}

// AssetTypes

const assetTypeList = ref<string[]>([])

async function loadAssetTypeList() {
  try {
    const response = await fetch('https://mackleaps.mackenzie.br/asset-api/assetType/list', {
      headers: {
        Accept: 'application/json',
        domain: 'teste',
        user: 'dnani',
        role: 'admin-builder',
      },
    })

    if (!response.ok) throw new Error('Erro ao buscar os asset types')
    assetTypeList.value = await response.json()
  } catch (err) {
    alert('Erro ao carregar a lista de asset types. Tente novamente mais tarde.')
    console.error('Erro ao carregar os dados:', err)
  }
}

/* ASSOCIATION TYPE */

interface AssociationType {
  name: string
  description?: string
  domain: string
}

const associationTypeList = ref<AssociationType[]>([])
const nameAssociationTypeList = computed(() => associationTypeList.value.map((item) => item.name))

async function loadAssociationTypeList() {
  try {
    const response = await fetch('https://mackleaps.mackenzie.br/asset-api/associationType/list', {
      headers: {
        Accept: 'application/json',
        domain: 'teste',
        user: 'dnani',
        role: 'admin-builder',
      },
    })
    if (!response.ok) throw new Error('Erro ao buscar lista de association types')
    associationTypeList.value = await response.json()
  } catch (error) {
    alert('Erro ao carregar a lista de association types. Tente novamente mais tarde.')
    console.error(error)
  }
}
</script>

<style scoped>
.about {
  padding: 2rem;
}
</style>
