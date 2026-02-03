<template>
  <div class="create-blog">
    <div class="card">
      <div class="card-header">
        <h2>创建博客</h2>
      </div>
      <div class="card-body">
        <form @submit.prevent="handleSubmit">
          <!-- 标题输入 -->
          <div class="form-group">
            <label for="title">标题</label>
            <input type="text" id="title" v-model="blog.title" required placeholder="请输入博客标题...">
          </div>

          <!-- 特色图片上传 -->
          <div class="form-group">
            <label for="featuredImage">特色图片</label>
            <input type="file" id="featuredImage" @change="handleImageUpload" accept="image/*">
            <div v-if="blog.featuredImage" class="image-preview">
              <img :src="blog.featuredImage" :alt="blog.title">
              <button type="button" @click="removeImage" class="remove-image-btn">删除</button>
            </div>
          </div>

          <!-- 富媒体内容编辑 -->
          <div class="form-group">
            <label>富媒体内容</label>
            <RichEditor @contentSaved="handleContentSaved" />
          </div>

          <!-- 传统内容编辑 -->
          <div class="form-group">
            <label for="content">内容</label>
            <textarea id="content" v-model="blog.content" rows="10" required placeholder="请输入博客内容..."></textarea>
          </div>

          <!-- Markdown内容 -->
          <div class="form-group">
            <label for="markdownContent">Markdown内容</label>
            <textarea id="markdownContent" v-model="blog.markdownContent" rows="10" placeholder="请输入Markdown内容..."></textarea>
          </div>

          <!-- 状态选择 -->
          <div class="form-group">
            <label for="status">状态</label>
            <select id="status" v-model="blog.status">
              <option value="DRAFT">草稿</option>
              <option value="PUBLISHED">已发布</option>
            </select>
          </div>

          <!-- 提交按钮 -->
          <div class="form-actions">
            <button type="submit" class="submit-btn" :disabled="loading">
              {{ loading ? '提交中...' : '提交' }}
            </button>
            <router-link to="/admin/blogs" class="cancel-btn">取消</router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import RichEditor from '../../components/RichEditor.vue'

export default {
  name: 'CreateBlog',
  components: {
    RichEditor
  },
  data() {
    return {
      blog: {
        title: '',
        content: '',
        markdownContent: '',
        featuredImage: '',
        status: 'DRAFT'
      },
      loading: false
    }
  },
  methods: {
    handleContentSaved(data) {
      console.log('Rich editor content saved:', data)
      // 这里可以将富媒体内容转换为HTML或其他格式，然后保存到blog.content中
      this.blog.content = JSON.stringify(data)
    },
    async handleSubmit() {
      this.loading = true
      try {
        const response = await axios.post('http://localhost:8080/api/blogs', this.blog)
        if (response.status === 201) {
          this.$router.push('/admin/blogs')
        }
      } catch (error) {
        console.error('Error creating blog:', error)
        alert('创建失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },
    async handleImageUpload(event) {
      const file = event.target.files[0]
      if (!file) return

      try {
        const formData = new FormData()
        formData.append('file', file)

        const response = await axios.post('http://localhost:8080/api/blogs/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })

        const responseData = typeof response.data === 'string' ? JSON.parse(response.data) : response.data
        this.blog.featuredImage = responseData.url
      } catch (error) {
        console.error('Error uploading image:', error)
        alert('图片上传失败，请稍后重试')
      }
    },
    removeImage() {
      this.blog.featuredImage = ''
    }
  }
}
</script>

<style scoped>
.create-blog {
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

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 1rem;
  font-weight: 500;
  color: #333;
}

.form-group input[type="text"],
.form-group select {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group input[type="text"]:focus,
.form-group select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.form-group input[type="file"] {
  margin-bottom: 1rem;
}

.image-preview {
  position: relative;
  max-width: 300px;
  margin-top: 1rem;
}

.image-preview img {
  width: 100%;
  height: auto;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.remove-image-btn {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  padding: 0.3rem 0.6rem;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  transition: background-color 0.3s;
}

.remove-image-btn:hover {
  background-color: #d32f2f;
}

.form-group textarea {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  resize: vertical;
  transition: border-color 0.3s;
}

.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.submit-btn {
  padding: 0.8rem 1.5rem;
  background-color: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: background-color 0.3s;
}

.submit-btn:hover:not(:disabled) {
  background-color: #5a6fd8;
}

.submit-btn:disabled {
  background-color: #a0a8d9;
  cursor: not-allowed;
}

.cancel-btn {
  padding: 0.8rem 1.5rem;
  background-color: #666;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  transition: background-color 0.3s;
}

.cancel-btn:hover {
  background-color: #555;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .card-body {
    padding: 1.5rem;
  }

  .form-group {
    margin-bottom: 1.2rem;
  }

  .form-actions {
    flex-direction: column;
  }

  .submit-btn,
  .cancel-btn {
    width: 100%;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .card-header {
    padding: 1rem;
  }

  .card-header h2 {
    font-size: 1.3rem;
  }

  .card-body {
    padding: 1rem;
  }

  .form-group input[type="text"],
  .form-group select,
  .form-group textarea {
    padding: 0.6rem;
    font-size: 0.9rem;
  }

  .submit-btn,
  .cancel-btn {
    padding: 0.6rem 1.2rem;
    font-size: 0.9rem;
  }
}
</style>