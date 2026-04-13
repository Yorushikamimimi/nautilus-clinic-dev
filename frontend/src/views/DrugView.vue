<template>
  <div>
    <h2>药品库存</h2>
    <div style="display:flex;gap:8px;margin-bottom:16px">
      <input v-model="searchName" placeholder="按名称搜索" style="padding:6px;border:1px solid #ccc;border-radius:4px;width:200px" />
      <button @click="fetchDrugs" style="padding:6px 16px;background:#1a73e8;color:#fff;border:none;border-radius:4px;cursor:pointer">搜索</button>
      <button @click="openCreate" style="padding:6px 16px;background:#34a853;color:#fff;border:none;border-radius:4px;cursor:pointer">新增药品</button>
    </div>

    <table style="width:100%;border-collapse:collapse">
      <thead>
        <tr style="background:#f5f5f5">
          <th style="padding:8px;border:1px solid #ddd">ID</th>
          <th style="padding:8px;border:1px solid #ddd">药品名称</th>
          <th style="padding:8px;border:1px solid #ddd">库存</th>
          <th style="padding:8px;border:1px solid #ddd">单位</th>
          <th style="padding:8px;border:1px solid #ddd">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="d in drugs" :key="d.id">
          <td style="padding:8px;border:1px solid #ddd;text-align:center">{{ d.id }}</td>
          <td style="padding:8px;border:1px solid #ddd">{{ d.name }}</td>
          <td style="padding:8px;border:1px solid #ddd;text-align:center" :style="d.stock < 20 ? 'color:red' : ''">{{ d.stock }}</td>
          <td style="padding:8px;border:1px solid #ddd;text-align:center">{{ d.unit }}</td>
          <td style="padding:8px;border:1px solid #ddd;text-align:center">
            <button @click="openEdit(d)" style="padding:4px 10px;cursor:pointer">编辑</button>
          </td>
        </tr>
        <tr v-if="drugs.length === 0">
          <td colspan="5" style="padding:16px;text-align:center;color:#999">暂无数据</td>
        </tr>
      </tbody>
    </table>

    <div v-if="showModal" style="position:fixed;inset:0;background:rgba(0,0,0,.4);display:flex;align-items:center;justify-content:center">
      <div style="background:#fff;padding:32px;border-radius:8px;width:360px">
        <h3>{{ editId ? '编辑药品' : '新增药品' }}</h3>
        <div style="margin-bottom:12px">
          <label>药品名称 *</label>
          <input v-model="form.name" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
        </div>
        <div style="margin-bottom:12px">
          <label>库存数量 *</label>
          <input v-model="form.stock" type="number" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
        </div>
        <div style="margin-bottom:20px">
          <label>单位 *</label>
          <input v-model="form.unit" placeholder="如：片、瓶、支" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
        </div>
        <div style="display:flex;gap:8px;justify-content:flex-end">
          <button @click="showModal=false" style="padding:6px 16px;cursor:pointer">取消</button>
          <button @click="saveDrug" style="padding:6px 16px;background:#1a73e8;color:#fff;border:none;border-radius:4px;cursor:pointer">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { drugApi } from '../api'

const drugs = ref([])
const searchName = ref('')
const showModal = ref(false)
const editId = ref(null)
const form = reactive({ name: '', stock: 0, unit: '' })

onMounted(fetchDrugs)

async function fetchDrugs() {
  const res = await drugApi.list(searchName.value)
  if (res.success) drugs.value = res.data
}

function openCreate() {
  editId.value = null
  Object.assign(form, { name: '', stock: 0, unit: '' })
  showModal.value = true
}

function openEdit(d) {
  editId.value = d.id
  Object.assign(form, { name: d.name, stock: d.stock, unit: d.unit })
  showModal.value = true
}

async function saveDrug() {
  if (!form.name || !form.unit) return alert('请填写完整信息')
  if (editId.value) {
    await drugApi.update(editId.value, form)
  } else {
    await drugApi.create(form)
  }
  showModal.value = false
  fetchDrugs()
}
</script>
