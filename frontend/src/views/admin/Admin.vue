<template>
  <div class="admin">
    <!-- 侧边栏导航 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>后台管理</h2>
      </div>
      <nav class="sidebar-menu">
        <router-link to="/admin/blogs" class="menu-item">
          <span class="menu-icon">📝</span>
          <span>博客管理</span>
        </router-link>
        <router-link to="/admin/create" class="menu-item">
          <span class="menu-icon">➕</span>
          <span>创建博客</span>
        </router-link>
        <router-link to="/admin/users" class="menu-item">
          <span class="menu-icon">👥</span>
          <span>用户管理</span>
        </router-link>
      </nav>
    </aside>

    <!-- 主内容区域 -->
    <main class="admin-content">
      <header class="content-header">
        <h1>{{ pageTitle }}</h1>
      </header>
      <div class="content-body">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: 'Admin',
  data() {
    return {
      pageTitle: '后台管理'
    }
  },
  watch: {
    $route(to) {
      this.updatePageTitle(to.path)
    }
  },
  methods: {
    updatePageTitle(path) {
      if (path.includes('/admin/blogs')) {
        this.pageTitle = '博客管理'
      } else if (path.includes('/admin/create')) {
        this.pageTitle = '创建博客'
      } else if (path.includes('/admin/edit')) {
        this.pageTitle = '编辑博客'
      } else if (path.includes('/admin/users')) {
        this.pageTitle = '用户管理'
      } else {
        this.pageTitle = '后台管理'
      }
    }
  }
}
</script>

<style scoped>
.admin {
  display: flex;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 侧边栏样式 */
.sidebar {
  width: 250px;
  background-color: #333;
  color: white;
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  overflow-y: auto;
}

.sidebar-header {
  padding: 2rem 1.5rem;
  border-bottom: 1px solid #444;
}

.sidebar-header h2 {
  font-size: 1.5rem;
  font-weight: bold;
  color: #667eea;
}

.sidebar-menu {
  flex: 1;
  padding: 1rem 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  color: white;
  text-decoration: none;
  transition: background-color 0.3s;
  gap: 0.8rem;
}

.menu-item:hover {
  background-color: #444;
}

.menu-item.router-link-active {
  background-color: #667eea;
  color: white;
}

.menu-icon {
  font-size: 1.2rem;
}

/* 主内容区域 */
.admin-content {
  flex: 1;
  margin-left: 250px;
  min-height: 100vh;
}

.content-header {
  background-color: white;
  padding: 1.5rem 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.content-header h1 {
  font-size: 1.8rem;
  color: #333;
}

.content-body {
  padding: 0 2rem 2rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    width: 200px;
  }

  .admin-content {
    margin-left: 200px;
  }

  .content-header {
    padding: 1rem 1.5rem;
  }

  .content-body {
    padding: 0 1.5rem 1.5rem;
  }
}

@media (max-width: 480px) {
  .sidebar {
    width: 100%;
    height: auto;
    position: relative;
    flex-direction: row;
    overflow-x: auto;
  }

  .sidebar-header {
    padding: 1rem;
    border-bottom: none;
    border-right: 1px solid #444;
  }

  .sidebar-menu {
    display: flex;
    padding: 0;
  }

  .menu-item {
    white-space: nowrap;
    border-bottom: 3px solid transparent;
  }

  .menu-item.router-link-active {
    border-bottom-color: #667eea;
    background-color: transparent;
  }

  .admin-content {
    margin-left: 0;
  }
}
</style>