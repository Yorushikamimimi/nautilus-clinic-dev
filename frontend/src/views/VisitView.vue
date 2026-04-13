<template>
  <div>
    <h2>就诊记录</h2>
    <button @click="showModal=true" style="margin-bottom:16px;padding:6px 16px;background:#34a853;color:#fff;border:none;border-radius:4px;cursor:pointer">新增就诊</button>

    <table style="width:100%;border-collapse:collapse">
      <thead>
        <tr style="background:#f5f5f5">
          <th style="padding:8px;border:1px solid #ddd">ID</th>
          <th style="padding:8px;border:1px solid #ddd">患者</th>
          <th style="padding:8px;border:1px solid #ddd">主诉</th>
          <th style="padding:8px;border:1px solid #ddd">状态</th>
          <th style="padding:8px;border:1px solid #ddd">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="v in visits" :key="v.id">
          <td style="padding:8px;border:1px solid #ddd;text-align:center">{{ v.id }}</td>
          <td style="padding:8px;border:1px solid #ddd">{{ v.patient?.name }}</td>
          <td style="padding:8px;border:1px solid #ddd">{{ v.complaint }}</td>
          <td style="padding:8px;border:1px solid #ddd;text-align:center">
            <span :style="statusStyle(v.status)">{{ statusLabel(v.status) }}</span>
          </td>
          <td style="padding:8px;border:1px solid #ddd;text-align:center">
            <select @change="e => changeStatus(v.id, e.target.value)" style="padding:4px">
              <option value="">更新状态</option>
              <option value="IN_PROGRESS">接诊</option>
              <option value="COMPLETED">完成</option>
              <option value="CLOSED">关闭</option>
            </select>
          </td>
        </tr>
        <tr v-if="visits.length === 0">
          <td colspan="5" style="padding:16px;text-align:center;color:#999">暂无数据</td>
        </tr>
      </tbody>
    </table>

    <div v-if="showModal" style="position:fixed;inset:0;background:rgba(0,0,0,.4);display:flex;align-items:center;justify-content:center">
      <div style="background:#fff;padding:32px;border-radius:8px;width:400px">
        <h3>新增就诊记录</h3>
        <div style="margin-bottom:12px">
          <label>患者 ID *</label>
          <input v-model="form.patientId" type="number" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
        </div>
        <div style="margin-bottom:12px">
          <label>医生 ID *</label>
          <input v-model="form.doctorId" type="number" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
        </div>
        <div style="margin-bottom:20px">
          <label>主诉</label>
          <textarea v-model="form.complaint" rows="3" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px"></textarea>
        </div>
        <div style="display:flex;gap:8px;justify-content:flex-end">
          <button @click="showModal=false" style="padding:6px 16px;cursor:pointer">取消</button>
          <button @click="createVisit" style="padding:6px 16px;background:#1a73e8;color:#fff;border:none;border-radius:4px;cursor:pointer">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { visitApi } from '../api'

const visits = ref([])
const showModal = ref(false)
const form = reactive({ patientId: '', doctorId: '', complaint: '' })

onMounted(fetchVisits)

async function fetchVisits() {
  const res = await visitApi.list()
  if (res.success) visits.value = res.data
}

async function createVisit() {
  if (!form.patientId || !form.doctorId) return alert('请填写患者ID和医生ID')
  await visitApi.create(form)
  showModal.value = false
  fetchVisits()
}

async function changeStatus(id, status) {
  if (!status) return
  await visitApi.updateStatus(id, status)
  fetchVisits()
}

function statusLabel(s) {
  return { WAITING: '待就诊', IN_PROGRESS: '就诊中', COMPLETED: '已完成', CLOSED: '已关闭' }[s] || s
}

function statusStyle(s) {
  const colors = { WAITING: '#f0ad4e', IN_PROGRESS: '#1a73e8', COMPLETED: '#34a853', CLOSED: '#999' }
  return `color:${colors[s]};font-weight:bold`
}
</script>
