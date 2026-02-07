<template>
  <div class="home">
    <header class="hero">
      <h1>个人博客</h1>
      <p>分享技术、生活和思考</p>
      <!-- 搜索表单 -->
      <div class="search-form">
        <input 
          type="text" 
          v-model="searchKeyword" 
          placeholder="搜索博客..."
          class="search-input"
        >
        <button @click="performSearch" class="search-btn">搜索</button>
        <button v-if="isSearching" @click="clearSearch" class="clear-btn">清除</button>
      </div>
    </header>

    <main class="container">
      <!-- 搜索结果提示 -->
      <div v-if="isSearching" class="search-result-info">
        <p>搜索结果: {{ searchKeyword }}</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>

      <!-- 错误提示 -->
      <div v-if="error" class="error-state">
        <p>{{ error }}</p>
        <button @click="fetchBlogs" class="retry-btn">重试</button>
      </div>

      <div v-if="!loading && !error" class="blog-list">
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
              <span v-if="blog.category" class="blog-category">
                分类: {{ blog.category.name }}
              </span>
            </div>
            <div class="blog-tags" v-if="blog.tags && blog.tags.length > 0">
              <span v-for="tag in blog.tags" :key="tag.id" class="tag">
                {{ tag.name }}
              </span>
            </div>
            <p class="blog-excerpt">{{ blog.content.substring(0, 150) }}...</p>
            <router-link :to="`/blog/${blog.id}`" class="read-more">阅读更多</router-link>
          </div>
        </div>
      </div>

      <div v-if="!loading && !error && blogs.length === 0" class="empty-state">
        <p>{{ isSearching ? '没有找到相关博客' : '暂无博客文章' }}</p>
      </div>

      <!-- 分页控件 -->
      <div v-if="!loading && !error && totalPages > 1" class="pagination">
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
      pageSize: 10,
      searchKeyword: '',
      isSearching: false,
      loading: false,
      error: null
    }
  },
  mounted() {
    this.fetchBlogs()
  },
  methods: {
    async fetchBlogs(page = 0, keyword = '') {
      this.loading = true
      this.error = null
      try {
        let url = `http://localhost:8080/api/blogs?page=${page}&size=${this.pageSize}`
        if (keyword) {
          url = `http://localhost:8080/api/blogs/search?keyword=${encodeURIComponent(keyword)}`
        }
        console.log('Fetching blogs from:', url)
        const response = await axios.get(url)
        console.log('Response data:', response.data)
        this.blogs = response.data.content || response.data
        if (response.data.number !== undefined) {
          this.currentPage = response.data.number
          this.totalPages = response.data.totalPages
          this.totalElements = response.data.totalElements
        } else {
          // 搜索结果没有分页信息
          this.currentPage = 0
          this.totalPages = 1
          this.totalElements = this.blogs.length
        }
      } catch (error) {
        console.error('Error fetching blogs:', error)
        this.error = '获取博客失败，请稍后重试'
        this.blogs = []
      } finally {
        this.loading = false
      }
    },
    changePage(page) {
      if (page >= 0 && page < this.totalPages) {
        this.fetchBlogs(page, this.isSearching ? this.searchKeyword : '')
      }
    },
    performSearch() {
      if (this.searchKeyword.trim()) {
        this.isSearching = true
        this.currentPage = 0
        this.fetchBlogs(0, this.searchKeyword.trim())
      }
    },
    clearSearch() {
      this.searchKeyword = ''
      this.isSearching = false
      this.currentPage = 0
      this.fetchBlogs()
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

/* 搜索表单样式 */
.search-form {
  display: flex;
  max-width: 600px;
  margin: 2rem auto 0;
  gap: 1rem;
  flex-wrap: wrap;
  justify-content: center;
}

.search-input {
  flex: 1;
  min-width: 300px;
  padding: 1rem 1.5rem;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 25px;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  font-size: 1rem;
  backdrop-filter: blur(10px);
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.search-btn {
  padding: 1rem 2rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 25px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s;
  backdrop-filter: blur(10px);
}

.search-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.clear-btn {
  padding: 1rem 1.5rem;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 25px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
  backdrop-filter: blur(10px);
}

.clear-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* 搜索结果信息 */
.search-result-info {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 2rem;
  border-left: 4px solid #667eea;
}

.search-result-info p {
  margin: 0;
  color: #666;
  font-size: 0.9rem;
}

/* 博客分类和标签样式 */
.blog-category {
  background: #e7f3ff;
  color: #0d6efd;
  padding: 0.2rem 0.6rem;
  border-radius: 12px;
  font-size: 0.8rem;
  margin-left: 0.5rem;
}

.blog-tags {
  margin: 1rem 0;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tag {
  background: #f8f9fa;
  color: #6c757d;
  padding: 0.3rem 0.8rem;
  border-radius: 15px;
  font-size: 0.8rem;
  border: 1px solid #dee2e6;
}

/* 加载状态样式 */
.loading-state {
  text-align: center;
  padding: 6rem 2rem;
  color: #666;
}

.loading-state p {
  font-size: 1.2rem;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
  100% {
    opacity: 1;
  }
}

/* 错误状态样式 */
.error-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #dc3545;
  background: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 8px;
  margin: 2rem 0;
}

.error-state p {
  margin-bottom: 1.5rem;
  font-size: 1rem;
}

.retry-btn {
  padding: 0.6rem 1.5rem;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.retry-btn:hover {
  background-color: #c82333;
}

@media (max-width: 768px) {
  .search-form {
    flex-direction: column;
    align-items: stretch;
    max-width: 90%;
  }
  
  .search-input {
    min-width: auto;
  }
  
  .blog-meta {
    flex-wrap: wrap;
    gap: 0.5rem;
  }
  
  .blog-category {
    margin-left: 0;
    margin-top: 0.5rem;
  }
  
  .loading-state,
  .error-state {
    padding: 3rem 1rem;
  }
}
</style>