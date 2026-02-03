<template>
  <div class="admin-users">
    <div class="card">
      <div class="card-header">
        <h2>用户管理</h2>
      </div>
      <div class="card-body">
        <div v-if="loading" class="loading">
          <p>加载中...</p>
        </div>

        <table v-else class="users-table">
          <thead>
            <tr>
              <th>用户名</th>
              <th>邮箱</th>
              <th>角色</th>
              <th>注册时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td>{{ user.username }}</td>
              <td>{{ user.email }}</td>
              <td>
                <span :class="['role-badge', user.role === 'ADMIN' ? 'admin' : 'user']">
                  {{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}
                </span>
              </td>
              <td>{{ formatDate(user.createdAt) }}</td>
              <td class="actions">
                <button @click="editUser(user)" class="action-btn edit-btn">
                  编辑
                </button>
                <button @click="deleteUser(user.id)" class="action-btn delete-btn">
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <div v-if="!loading && users.length === 0" class="empty-state">
          <p>暂无用户</p>
        </div>
      </div>
    </div>

    <!-- 编辑用户模态框 -->
    <div v-if="showEditModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>编辑用户</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="updateUser">
            <div class="form-group">
              <label for="edit-username">用户名</label>
              <input type="text" id="edit-username" v-model="editUserForm.username" disabled>
            </div>
            <div class="form-group">
              <label for="edit-email">邮箱</label>
              <input type="email" id="edit-email" v-model="editUserForm.email" required>
            </div>
            <div class="form-group">
              <label for="edit-role">角色</label>
              <select id="edit-role" v-model="editUserForm.role">
                <option value="USER">普通用户</option>
                <option value="ADMIN">管理员</option>
              </select>
            </div>
            <div class="modal-actions">
              <button type="submit" class="submit-btn" :disabled="saving">
                {{ saving ? '保存中...' : '保存' }}
              </button>
              <button type="button" @click="closeModal" class="cancel-btn">取消</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AdminUsers',
  data() {
    return {
      users: [],
      loading: true,
      saving: false,
      showEditModal: false,
      editUserForm: {
        id: '',
        username: '',
        email: '',
        role: 'USER'
      }
    }
  },
  mounted() {
    this.fetchUsers()
  },
  methods: {
    async fetchUsers() {
      try {
        // 这里需要实现获取用户列表的API调用
        // 暂时使用模拟数据
        this.users = [
          {
            id: 1,
            username: 'admin',
            email: 'admin@example.com',
            role: 'ADMIN',
            createdAt: new Date().toISOString()
          },
          {
            id: 2,
            username: 'user1',
            email: 'user1@example.com',
            role: 'USER',
            createdAt: new Date().toISOString()
          }
        ]
      } catch (error) {
        console.error('Error fetching users:', error)
      } finally {
        this.loading = false
      }
    },
    editUser(user) {
      this.editUserForm = {
        id: user.id,
        username: user.username,
        email: user.email,
        role: user.role
      }
      this.showEditModal = true
    },
    async updateUser() {
      this.saving = true
      try {
        // 这里需要实现更新用户的API调用
        // 暂时模拟更新
        const index = this.users.findIndex(u => u.id === this.editUserForm.id)
        if (index !== -1) {
          this.users[index] = {
            ...this.users[index],
            email: this.editUserForm.email,
            role: this.editUserForm.role
          }
        }
        this.closeModal()
      } catch (error) {
        console.error('Error updating user:', error)
        alert('更新失败，请稍后重试')
      } finally {
        this.saving = false
      }
    },
    async deleteUser(id) {
      if (confirm('确定要删除这个用户吗？')) {
        try {
          // 这里需要实现删除用户的API调用
          // 暂时模拟删除
          this.users = this.users.filter(user => user.id !== id)
        } catch (error) {
          console.error('Error deleting user:', error)
          alert('删除失败，请稍后重试')
        }
      }
    },
    closeModal() {
      this.showEditModal = false
      this.editUserForm = {
        id: '',
        username: '',
        email: '',
        role: 'USER'
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
.admin-users {
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

.users-table {
  width: 100%;
  border-collapse: collapse;
}

.users-table th,
.users-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.users-table th {
  background-color: #f5f5f5;
  font-weight: 600;
  color: #333;
}

.users-table tr:hover {
  background-color: #f9f9f9;
}

.role-badge {
  padding: 0.3rem 0.6rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
}

.role-badge.admin {
  background-color: #e3f2fd;
  color: #1565c0;
}

.role-badge.user {
  background-color: #e8f5e9;
  color: #2e7d32;
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
  border: none;
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

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  font-size: 1.3rem;
  color: #333;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.close-btn:hover {
  background-color: #f5f5f5;
}

.modal-body {
  padding: 1.5rem;
}

.modal-body .form-group {
  margin-bottom: 1.5rem;
}

.modal-body label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 1rem;
  font-weight: 500;
  color: #333;
}

.modal-body input,
.modal-body select {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.modal-body input:focus,
.modal-body select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.modal-actions {
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
  border: none;
  border-radius: 4px;
  cursor: pointer;
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
    padding: 1rem;
  }

  .users-table {
    font-size: 0.9rem;
  }

  .users-table th,
  .users-table td {
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

  .modal-content {
    width: 95%;
  }

  .modal-header,
  .modal-body {
    padding: 1rem;
  }

  .modal-actions {
    flex-direction: column;
  }
}
</style>