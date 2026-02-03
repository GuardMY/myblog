<template>
  <div class="blog-detail">
    <div class="container">
      <div v-if="loading" class="loading">
        <p>加载中...</p>
      </div>

      <div v-else-if="blog" class="blog-content">
        <h1>{{ blog.title }}</h1>
        <div class="blog-meta">
          <span>作者: {{ blog.author.username }}</span>
          <span>发布时间: {{ formatDate(blog.publishedAt) }}</span>
          <span>浏览量: {{ blog.viewCount }}</span>
        </div>

        <div v-if="blog.featuredImage" class="featured-image">
          <img :src="blog.featuredImage" :alt="blog.title">
        </div>

        <div class="content-body" v-html="blog.content"></div>

        <!-- 评论部分 -->
        <div class="comments-section">
          <h2>评论 ({{ comments.length }})</h2>

          <!-- 评论表单 -->
          <div v-if="isLoggedIn" class="comment-form">
            <h3>发表评论</h3>
            <textarea v-model="commentContent" placeholder="写下你的评论..." rows="4"></textarea>
            <button @click="submitComment" class="submit-comment">提交评论</button>
          </div>

          <div v-else class="login-prompt">
            <p>请 <router-link to="/login">登录</router-link> 后发表评论</p>
          </div>

          <!-- 评论列表 -->
          <div class="comments-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-author">
                <span class="author-name">{{ comment.user.username }}</span>
                <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
              </div>
              <div class="comment-body">{{ comment.content }}</div>
            </div>
          </div>

          <div v-if="comments.length === 0" class="no-comments">
            <p>暂无评论，快来发表第一条评论吧！</p>
          </div>
        </div>
      </div>

      <div v-else class="error-state">
        <p>博客文章不存在</p>
        <router-link to="/" class="back-home">返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'BlogDetail',
  data() {
    return {
      blog: null,
      comments: [],
      loading: true,
      commentContent: '',
      isLoggedIn: false
    }
  },
  mounted() {
    this.checkLoginStatus()
    this.fetchBlog()
    this.fetchComments()
  },
  methods: {
    checkLoginStatus() {
      this.isLoggedIn = !!localStorage.getItem('user')
    },
    async fetchBlog() {
      try {
        const { id } = this.$route.params
        const response = await axios.get(`http://localhost:8080/api/blogs/${id}`)
        this.blog = response.data
      } catch (error) {
        console.error('Error fetching blog:', error)
      } finally {
        this.loading = false
      }
    },
    async fetchComments() {
      try {
        const { id } = this.$route.params
        const response = await axios.get(`http://localhost:8080/api/comments/blog/${id}`)
        this.comments = response.data
      } catch (error) {
        console.error('Error fetching comments:', error)
      }
    },
    async submitComment() {
      if (!this.commentContent.trim()) return

      try {
        const { id } = this.$route.params
        const response = await axios.post('http://localhost:8080/api/comments', {
          content: this.commentContent,
          blog: {
            id: parseInt(id)
          }
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        })
        this.comments.unshift(response.data)
        this.commentContent = ''
      } catch (error) {
        console.error('Error submitting comment:', error)
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
.blog-detail {
  min-height: 100vh;
  padding: 2rem 0;
  background-color: #f5f5f5;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 2rem;
}

.loading,
.error-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #666;
}

.error-state .back-home {
  display: inline-block;
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: #667eea;
  color: white;
  text-decoration: none;
  border-radius: 4px;
}

.blog-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 2rem;
}

.blog-content h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: #333;
}

.blog-meta {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 2rem;
  font-size: 0.9rem;
  color: #666;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.featured-image {
  margin-bottom: 2rem;
}

.featured-image img {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.content-body {
  line-height: 1.8;
  color: #333;
  margin-bottom: 3rem;
}

.content-body p {
  margin-bottom: 1.5rem;
}

.content-body h2,
.content-body h3 {
  margin: 2rem 0 1rem;
  color: #333;
}

.content-body img {
  max-width: 100%;
  height: auto;
  margin: 1rem 0;
}

/* 评论部分 */
.comments-section {
  margin-top: 3rem;
  padding-top: 2rem;
  border-top: 1px solid #eee;
}

.comments-section h2 {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  color: #333;
}

.comment-form {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.comment-form h3 {
  font-size: 1.2rem;
  margin-bottom: 1rem;
  color: #333;
}

.comment-form textarea {
  width: 100%;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  font-size: 1rem;
  margin-bottom: 1rem;
}

.submit-comment {
  padding: 0.5rem 1.5rem;
  background-color: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.submit-comment:hover {
  background-color: #5a6fd8;
}

.login-prompt {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  text-align: center;
}

.login-prompt a {
  color: #667eea;
  text-decoration: none;
}

.login-prompt a:hover {
  text-decoration: underline;
}

.comments-list {
  margin-top: 2rem;
}

.comment-item {
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-author {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.author-name {
  font-weight: bold;
  color: #333;
}

.comment-time {
  color: #666;
}

.comment-body {
  line-height: 1.6;
  color: #333;
}

.no-comments {
  text-align: center;
  padding: 3rem 2rem;
  color: #666;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-top: 2rem;
}

@media (max-width: 768px) {
  .container {
    padding: 0 1rem;
  }

  .blog-content {
    padding: 1.5rem;
  }

  .blog-content h1 {
    font-size: 2rem;
  }

  .blog-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
}
</style>