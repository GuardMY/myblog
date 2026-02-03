<template>
  <div class="app">
    <!-- 导航栏 -->
    <nav class="navbar">
      <div class="container">
        <div class="navbar-brand">
          <router-link to="/">个人博客</router-link>
        </div>
        <div class="navbar-menu">
          <router-link to="/" class="nav-link">首页</router-link>
          <div v-if="isLoggedIn" class="user-menu">
            <span class="welcome">欢迎, {{ username }}</span>
            <router-link to="/admin" class="nav-link">后台管理</router-link>
            <button @click="handleLogout" class="logout-btn">退出登录</button>
          </div>
          <div v-else class="auth-menu">
            <router-link to="/login" class="nav-link">登录</router-link>
            <router-link to="/register" class="nav-link">注册</router-link>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主内容区域 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="container">
        <p>&copy; {{ new Date().getFullYear() }} 个人博客. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      isLoggedIn: false,
      username: ''
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const user = localStorage.getItem('user')
      if (user) {
        this.isLoggedIn = true
        this.username = user
      } else {
        this.isLoggedIn = false
        this.username = ''
      }
    },
    handleLogout() {
      localStorage.removeItem('user')
      this.isLoggedIn = false
      this.username = ''
      this.$router.push('/')
    }
  },
  watch: {
    '$route': function() {
      this.checkLoginStatus()
    }
  }
}
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 16px;
  line-height: 1.6;
  color: #333;
  background-color: #f5f5f5;
}

/* 导航栏样式 */
.navbar {
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  height: 60px;
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: bold;
}

.navbar-brand a {
  color: #667eea;
  text-decoration: none;
}

.navbar-menu {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 1rem;
  transition: color 0.3s;
}

.nav-link:hover {
  color: #667eea;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.welcome {
  font-size: 0.9rem;
  color: #666;
}

.logout-btn {
  padding: 0.4rem 0.8rem;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: #d32f2f;
}

.auth-menu {
  display: flex;
  gap: 1.5rem;
}

/* 主内容区域 */
.main-content {
  min-height: calc(100vh - 120px);
}

/* 页脚样式 */
.footer {
  background-color: #333;
  color: white;
  padding: 2rem 0;
  margin-top: 2rem;
}

.footer .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  text-align: center;
}

.footer p {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navbar .container {
    padding: 0 1rem;
  }

  .navbar-menu {
    gap: 1rem;
  }

  .user-menu {
    flex-direction: column;
    align-items: flex-end;
    gap: 0.5rem;
  }
}
</style>