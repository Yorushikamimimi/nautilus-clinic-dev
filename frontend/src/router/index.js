import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import PatientView from '../views/PatientView.vue'
import VisitView from '../views/VisitView.vue'
import DrugView from '../views/DrugView.vue'
import PrescriptionView from '../views/PrescriptionView.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView },
  { path: '/patients', component: PatientView, meta: { requiresAuth: true } },
  { path: '/visits', component: VisitView, meta: { requiresAuth: true } },
  { path: '/drugs', component: DrugView, meta: { requiresAuth: true } },
  { path: '/prescriptions', component: PrescriptionView, meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !localStorage.getItem('token')) {
    return '/login'
  }
})

export default router
