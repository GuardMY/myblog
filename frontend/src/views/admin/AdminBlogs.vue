<template>
  <div class="admin-blogs">
    <div class="card">
      <div class="card-header">
        <h2>博客列表</h2>
      </div>
      <div class="card-body">
        <div v-if="loading" class="loading">
          <p>加载中...</p>
        </div>

        <table v-else class="blogs-table">
          <thead>
            <tr>
              <th>标题</th>
              <th>作者</th>
              <th>发布时间</th>
              <th>状态</th>
              <th>浏览量</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="blog in blogs" :key="blog.id">
              <td>{{ blog.title }}</td>
              <td>{{ blog.author.username }}</td>
              <td>{{ formatDate(blog.publishedAt || blog.createdAt) }}</td>
              <td>
                <span :class="['status-badge', blog.status === 'PUBLISHED' ? 'published' : 'draft']">
                  {{ blog.status === 'PUBLISHED' ? '已发布' : '草稿' }}
                </span>
              </td>
              <td>{{ blog.viewCount }}</td>
              <td class="actions">
                <router-link :to="`/admin/edit/${blog.id}`" class="action-btn edit-btn">
                  编辑
                </router-link>
                <button @click="deleteBlog(blog.id)" class="action-btn delete-btn">
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <div v-if="!loading && blogs.length === 0" class="empty-state">
          <p>暂无博客文章</p>
          <router-link to="/admin/create" class="create-btn">创建第一篇博客</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AdminBlogs',
  data() {
    return {
      blogs: [],
      loading: true
    }
  },
  mounted() {
    this.fetchBlogs()
  },
  methods: {
    async fetchBlogs() {
      try {
        const response = await axios.get('http://localhost:8080/api/blogs')
        this.blogs = response.data
      } catch (error) {
        console.error('Error fetching blogs:', error)
      } finally {
        this.loading = false
      }
    },
    async deleteBlog(id) {
      if (confirm('确定要删除这篇博客吗？')) {
        try {
          await axios.delete(`http://localhost:8080/api/blogs/${id}`)
          this.fetchBlogs()
        } catch (error) {
          console.error('Error deleting blog:', error)
          alert('删除失败，请稍后重试')
        }
      }
    },
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleString()
    }
  }
}
</script>

<style scoped>
.admin-blogs {
  width: 100%;
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header {
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  background-color: #f9f9f9;
}

.card-header h2 {
  font-size: 1.5rem;
  color: #333;
  margin: 0;
}

.card-body {
  padding: 1.5rem;
}

.loading {
  text-align: center;
  padding: 3rem;
  color: #666;
}

.blogs-table {
  width: 100%;
  border-collapse: collapse;
}

.blogs-table th,
.blogs-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.blogs-table th {
  background-color: #f5f5f5;
  font-weight: 600;
  color: #333;
}

.blogs-table tr:hover {
  background-color: #f9f9f9;
}

.status-badge {
  padding: 0.3rem 0.6rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
}

.status-badge.published {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-badge.draft {
  background-color: #fff3e0;
  color: #ef6c00;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  text-decoration: none;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-btn {
  background-color: #667eea;
  color: white;
}

.edit-btn:hover {
  background-color: #5a6fd8;
}

.delete-btn {
  background-color: #f44336;
  color: white;
  border: none;
}

.delete-btn:hover {
  background-color: #d32f2f;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #666;
}

.create-btn {
  display: inline-block;
  margin-top: 1rem;
  padding: 0.6rem 1.2rem;
  background-color: #667eea;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.create-btn:hover {
  background-color: #5a6fd8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .card-body {
    padding: 1rem;
  }

  .blogs-table {
    font-size: 0.9rem;
  }

  .blogs-table th,
  .blogs-table td {
    padding: 0.8rem;
  }

  .actions {
    flex-direction: column;
    gap: 0.3rem;
  }

  .action-btn {
    font-size: 0.8rem;
    padding: 0.3rem 0.6rem;
  }
}

@media (max-width: 480px) {
  .blogs-table {
    display: block;
    overflow-x: auto;
  }

  .blogs-table thead,
  .blogs-table tbody,
  .blogs-table th,
  .blogs-table td,
  .blogs-table tr {
    display: block;
  }

  .blogs-table tr {
    margin-bottom: 1rem;
    border: 1px solid #eee;
    border-radius: 4px;
  }

  .blogs-table td {
    text-align: right;
    padding: 0.5rem 1rem;
    position: relative;
  }

  .blogs-table td::before {
    content: attr(data-label);
    position: absolute;
    left: 1rem;
    font-weight: bold;
    text-align: left;
  }
}
</style>