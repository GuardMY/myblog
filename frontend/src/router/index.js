import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/blog/:id',
    name: 'BlogDetail',
    component: () => import('../views/BlogDetail.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/Admin.vue'),
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: 'blogs',
        name: 'AdminBlogs',
        component: () => import('../views/admin/AdminBlogs.vue')
      },
      {
        path: 'create',
        name: 'CreateBlog',
        component: () => import('../views/admin/CreateBlog.vue')
      },
      {
        path: 'edit/:id',
        name: 'EditBlog',
        component: () => import('../views/admin/EditBlog.vue')
      },
      {
      path: 'users',
      name: 'AdminUsers',
      component: () => import('../views/admin/AdminUsers.vue'),
      meta: {
        requiresAdmin: true
      }
    }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫，检查登录状态和管理员权限
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin)
  const isLoggedIn = localStorage.getItem('token') || localStorage.getItem('user')
  const userRole = localStorage.getItem('userRole')

  // 已登录用户不能访问登录和注册页面
  if ((to.name === 'Login' || to.name === 'Register') && isLoggedIn) {
    next('/')
  }
  // 未登录用户访问需要认证的页面时重定向到登录
  else if (requiresAuth && !isLoggedIn) {
    next('/login')
  }
  // 需要管理员权限的页面，检查用户是否为管理员
  else if (requiresAdmin && (userRole !== 'ADMIN')) {
    next('/')
  } else {
    next()
  }
})

export default router