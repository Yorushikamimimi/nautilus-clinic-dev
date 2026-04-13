<template>
  <div>
    <h2>患者管理</h2>
    <div style="display:flex;gap:8px;margin-bottom:16px">
      <input v-model="searchName" placeholder="按姓名搜索" style="padding:6px;border:1px solid #ccc;border-radius:4px;width:200px" />
      <button @click="fetchPatients" style="padding:6px 16px;background:#1a73e8;color:#fff;border:none;border-radius:4px;cursor:pointer">搜索</button>
      <button @click="openCreate" style="padding:6px 16px;background:#34a853;color:#fff;border:none;border-radius:4px;cursor:pointer">新增患者</button>
    </div>

    <table style="width:100%;border-collapse:collapse">
      <thead>
        <tr style="background:#f5f5f5">
          <th style="padding:8px;border:1px solid #ddd">ID</th>
          <th style="padding:8px;border:1px solid #ddd">姓名</th>
          <th style="padding:8px;border:1px solid #ddd">性别</th>
          <th style="padding:8px;border:1px solid #ddd">出生日期</th>
          <th style="padding:8px;border:1px solid #ddd">手机号</th>
          <th style="padding:8px;border:1px solid #ddd">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="p in patients" :key="p.id">
          <td style="padding:8px;border:1px solid #ddd;text-align:center">{{ p.id }}</td>
          <td style="padding:8px;border:1px solid #ddd">{{ p.name }}</td>
          <td style="padding:8px;border:1px solid #ddd;text-align:center">{{ p.gender }}</td>
          <td style="padding:8px;border:1px solid #ddd;text-align:center">{{ p.birthDate }}</td>
          <td style="padding:8px;border:1px solid #ddd">{{ p.phone }}</td>
          <td style="padding:8px;border:1px solid #ddd;text-align:center">
            <button @click="openEdit(p)" style="margin-right:8px;padding:4px 10px;cursor:pointer">编辑</button>
            <button @click="deletePatient(p.id)" style="padding:4px 10px;color:red;cursor:pointer">删除</button>
          </td>
        </tr>
        <tr v-if="patients.length === 0">
          <td colspan="6" style="padding:16px;text-align:center;color:#999">暂无数据</td>
        </tr>
      </tbody>
    </table>

    <!-- Modal -->
    <div v-if="showModal" style="position:fixed;inset:0;background:rgba(0,0,0,.4);display:flex;align-items:center;justify-content:center">
      <div style="background:#fff;padding:32px;border-radius:8px;width:400px">
        <h3>{{ editId ? '编辑患者' : '新增患者' }}</h3>
        <div style="margin-bottom:12px">
          <label>姓名 *</label>
          <input v-model="form.name" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
        </div>
        <div style="margin-bottom:12px">
          <label>性别 *</label>
          <select v-model="form.gender" style="width:100%;padding:6px;border:1px solid #ccc;border-radius:4px;margin-top:4px">
            <option value="男">男</option>
            <option value="女">女</option>
          </select>
        </div>
        <div style="margin-bottom:12px">
          <label>出生日期</label>
          <input v-model="form.birthDate" type="date" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
        </div>
        <div style="margin-bottom:20px">
          <label>手机号</label>
          <input v-model="form.phone" style="width:100%;padding:6px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px;margin-top:4px" />
        </div>
        <div style="display:flex;gap:8px;justify-content:flex-end">
          <button @click="showModal=false" style="padding:6px 16px;cursor:pointer">取消</button>
          <button @click="savePatient" style="padding:6px 16px;background:#1a73e8;color:#fff;border:none;border-radius:4px;cursor:pointer">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { patientApi } from '../api'

const patients = ref([])
const searchName = ref('')
const showModal = ref(false)
const editId = ref(null)
const form = reactive({ name: '', gender: '男', birthDate: '', phone: '' })

onMounted(fetchPatients)

async function fetchPatients() {
  const res = await patientApi.list(searchName.value)
  if (res.success) patients.value = res.data
}

function openCreate() {
  editId.value = null
  Object.assign(form, { name: '', gender: '男', birthDate: '', phone: '' })
  showModal.value = true
}

function openEdit(p) {
  editId.value = p.id
  Object.assign(form, { name: p.name, gender: p.gender, birthDate: p.birthDate, phone: p.phone })
  showModal.value = true
}

async function savePatient() {
  if (!form.name) return alert('姓名不能为空')
  if (editId.value) {
    await patientApi.update(editId.value, form)
  } else {
    await patientApi.create(form)
  }
  showModal.value = false
  fetchPatients()
}

async function deletePatient(id) {
  if (!confirm('确定删除该患者？')) return
  await patientApi.delete(id)
  fetchPatients()
}
</script>
