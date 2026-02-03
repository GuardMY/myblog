<template>
  <div class="home">
    <header class="hero">
      <h1>个人博客</h1>
      <p>分享技术、生活和思考</p>
    </header>

    <main class="container">
      <div class="blog-list">
        <div v-for="blog in blogs" :key="blog.id" class="blog-item">
          <div v-if="blog.featuredImage" class="blog-image">
            <img :src="blog.featuredImage" :alt="blog.title">
          </div>
          <div class="blog-content">
            <h2><router-link :to="`/blog/${blog.id}`">{{ blog.title }}</router-link></h2>
            <div class="blog-meta">
              <span>作者: {{ blog.author.username }}</span>
              <span>发布时间: {{ formatDate(blog.publishedAt) }}</span>
              <span>浏览量: {{ blog.viewCount }}</span>
            </div>
            <p class="blog-excerpt">{{ blog.content.substring(0, 150) }}...</p>
            <router-link :to="`/blog/${blog.id}`" class="read-more">阅读更多</router-link>
          </div>
        </div>
      </div>

      <div v-if="blogs.length === 0" class="empty-state">
        <p>暂无博客文章</p>
      </div>

      <!-- 分页控件 -->
      <div v-if="totalPages > 1" class="pagination">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage === 0" class="page-btn">
          上一页
        </button>
        <div class="page-info">
          <span>第 {{ currentPage + 1 }} 页，共 {{ totalPages }} 页</span>
          <span>共 {{ totalElements }} 篇文章</span>
        </div>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages - 1" class="page-btn">
          下一页
        </button>
      </div>
    </main>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Home',
  data() {
    return {
      blogs: [],
      currentPage: 0,
      totalPages: 0,
      totalElements: 0,
      pageSize: 10
    }
  },
  mounted() {
    this.fetchBlogs()
  },
  methods: {
    async fetchBlogs(page = 0) {
      try {
        const response = await axios.get(`http://localhost:8080/api/blogs?page=${page}&size=${this.pageSize}`)
        this.blogs = response.data.content
        this.currentPage = response.data.number
        this.totalPages = response.data.totalPages
        this.totalElements = response.data.totalElements
      } catch (error) {
        console.error('Error fetching blogs:', error)
      }
    },
    changePage(page) {
      if (page >= 0 && page < this.totalPages) {
        this.fetchBlogs(page)
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
.home {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 分页控件样式 */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 2rem;
  padding: 1rem;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-btn {
  padding: 0.6rem 1.2rem;
  background-color: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.page-btn:hover:not(:disabled) {
  background-color: #5a6fd8;
}

.page-btn:disabled {
  background-color: #a0a8d9;
  cursor: not-allowed;
}

.page-info {
  display: flex;
  gap: 2rem;
  font-size: 0.9rem;
  color: #666;
}

.hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  text-align: center;
  padding: 6rem 2rem;
  margin-bottom: 2rem;
}

.hero h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.hero p {
  font-size: 1.2rem;
  opacity: 0.9;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.blog-list {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.blog-item {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

@media (min-width: 768px) {
  .blog-item {
    flex-direction: row;
  }
  
  .blog-image {
    flex: 0 0 300px;
  }
  
  .blog-content {
    flex: 1;
    padding: 2rem;
  }
}

.blog-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.blog-content {
  padding: 1.5rem;
}

.blog-content h2 {
  margin-bottom: 1rem;
  font-size: 1.5rem;
}

.blog-content h2 a {
  color: #333;
  text-decoration: none;
}

.blog-content h2 a:hover {
  color: #667eea;
}

.blog-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  color: #666;
}

.blog-excerpt {
  margin-bottom: 1.5rem;
  line-height: 1.6;
  color: #333;
}

.read-more {
  display: inline-block;
  padding: 0.5rem 1rem;
  background-color: #667eea;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.read-more:hover {
  background-color: #5a6fd8;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #666;
}
</style>