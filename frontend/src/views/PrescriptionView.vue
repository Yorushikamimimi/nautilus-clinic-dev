<template>
  <div>
    <h2>处方管理</h2>

    <div style="margin-bottom:16px;display:flex;gap:8px;align-items:center">
      <label>就诊ID：</label>
      <input v-model="visitId" type="number" placeholder="输入就诊ID查询" style="padding:6px;border:1px solid #ccc;border-radius:4px;width:160px" />
      <button @click="loadPrescriptions" style="padding:6px 16px;background:#1a73e8;color:#fff;border:none;border-radius:4px;cursor:pointer">查询</button>
      <button v-if="canWrite" @click="openCreate" style="padding:6px 16px;background:#34a853;color:#fff;border:none;border-radius:4px;cursor:pointer">+ 新增处方</button>
    </div>

    <table style="width:100%;border-collapse:collapse">
      <thead>
        <tr style="background:#f5f5f5">
          <th style="border:1px solid #ddd;padding:8px;text-align:left">ID</th>
          <th style="border:1px solid #ddd;padding:8px;text-align:left">就诊ID</th>
          <th style="border:1px solid #ddd;padding:8px;text-align:left">药品</th>
          <th style="border:1px solid #ddd;padding:8px;text-align:left">剂量</th>
          <th style="border:1px solid #ddd;padding:8px;text-align:left">备注</th>
          <th style="border:1px solid #ddd;padding:8px;text-align:left">创建时间</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="p in prescriptions" :key="p.id">
          <td style="border:1px solid #ddd;padding:8px">{{ p.id }}</td>
          <td style="border:1px solid #ddd;padding:8px">{{ p.visit?.id }}</td>
          <td style="border:1px solid #ddd;padding:8px">{{ p.drug?.name }}</td>
          <td style="border:1px solid #ddd;padding:8px">{{ p.dosage }}</td>
          <td style="border:1px solid #ddd;padding:8px">{{ p.notes }}</td>
          <td style="border:1px solid #ddd;padding:8px">{{ p.createdAt?.substring(0, 10) }}</td>
        </tr>
        <tr v-if="prescriptions.length === 0">
          <td colspan="6" style="border:1px solid #ddd;padding:16px;text-align:center;color:#999">暂无数据，请输入就诊ID查询</td>
        </tr>
      </tbody>
    </table>

    <!-- Create Modal -->
    <div v-if="showModal" style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.5);display:flex;align-items:center;justify-content:center;z-index:100">
      <div style="background:#fff;padding:24px;border-radius:8px;width:400px">
        <h3 style="margin-top:0">新增处方</h3>
        <div style="display:flex;flex-direction:column;gap:12px">
          <label>就诊ID
            <input v-model="form.visitId" type="number" style="display:block;width:100%;padding:6px;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
          </label>
          <label>药品ID
            <input v-model="form.drugId" type="number" style="display:block;width:100%;padding:6px;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
          </label>
          <label>剂量
            <input v-model="form.dosage" style="display:block;width:100%;padding:6px;border:1px solid #ccc;border-radius:4px;margin-top:4px" placeholder="如: 10mg/次，3次/天" />
          </label>
          <label>备注
            <input v-model="form.notes" style="display:block;width:100%;padding:6px;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
          </label>
        </div>
        <div style="margin-top:16px;display:flex;gap:8px;justify-content:flex-end">
          <button @click="showModal=false" style="padding:6px 16px;border:1px solid #ccc;border-radius:4px;cursor:pointer;background:#fff">取消</button>
          <button @click="createPrescription" style="padding:6px 16px;background:#1a73e8;color:#fff;border:none;border-radius:4px;cursor:pointer">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { prescriptionApi } from '../api/index.js'

const visitId = ref('')
const prescriptions = ref([])
const showModal = ref(false)
const form = ref({ visitId: '', drugId: '', dosage: '', notes: '' })

const role = localStorage.getItem('role')
const canWrite = computed(() => role === 'ADMIN' || role === 'DOCTOR')

async function loadPrescriptions() {
  if (!visitId.value) return
  const res = await prescriptionApi.byVisit(visitId.value)
  prescriptions.value = res.data.data || []
}

function openCreate() {
  form.value = { visitId: visitId.value || '', drugId: '', dosage: '', notes: '' }
  showModal.value = true
}

async function createPrescription() {
  await prescriptionApi.create(form.value)
  showModal.value = false
  if (visitId.value) loadPrescriptions()
}
</script>
