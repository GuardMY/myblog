<template>
  <div class="login">
    <div class="container">
      <div class="login-form">
        <h1>用户登录</h1>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" id="username" v-model="username" required>
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input type="password" id="password" v-model="password" required>
          </div>
          <div v-if="error" class="error-message">
            {{ error }}
          </div>
          <button type="submit" class="login-btn" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>
        <div class="register-link">
          <p>还没有账号？<router-link to="/register">立即注册</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      loading: false,
      error: ''
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      this.error = ''

      try {
        // 登录请求，携带cookies
        const loginResponse = await axios.post('http://localhost:8080/api/auth/login', {
          username: this.username,
          password: this.password
        }, {
          withCredentials: true
        })

        if (loginResponse.status === 200) {
          // 登录成功后获取用户完整信息（包含角色），携带cookies
          const userInfoResponse = await axios.get('http://localhost:8080/api/auth/me', {
            withCredentials: true
          })
          if (userInfoResponse.status === 200) {
            const userInfo = userInfoResponse.data
            localStorage.setItem('user', userInfo.username)
            localStorage.setItem('userRole', userInfo.role)
            this.$router.push('/')
          }
        }
      } catch (error) {
        if (error.response && error.response.data && error.response.data.error) {
          this.error = error.response.data.error
        } else {
          this.error = '登录失败，请检查用户名和密码'
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 2rem;
}

.container {
  max-width: 400px;
  width: 100%;
}

.login-form {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: 2rem;
}

.login-form h1 {
  margin-bottom: 2rem;
  font-size: 1.8rem;
  color: #333;
  text-align: center;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  color: #666;
}

.form-group input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 0.8rem;
  border-radius: 4px;
  margin-bottom: 1.5rem;
  font-size: 0.9rem;
}

.login-btn {
  width: 100%;
  padding: 0.8rem;
  background-color: #667eea;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn:hover:not(:disabled) {
  background-color: #5a6fd8;
}

.login-btn:disabled {
  background-color: #a0a8d9;
  cursor: not-allowed;
}

.register-link {
  margin-top: 1.5rem;
  text-align: center;
  font-size: 0.9rem;
  color: #666;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.register-link a:hover {
  text-decoration: underline;
}

@media (max-width: 480px) {
  .login-form {
    padding: 1.5rem;
  }

  .login-form h1 {
    font-size: 1.5rem;
  }
}
</style>