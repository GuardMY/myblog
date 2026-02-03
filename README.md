# 个人博客服务

一个基于Vue3和Spring Boot 3.5.10的个人博客服务，支持非登录查看博客、用户登录、用户评论、后台系统管理员，以及在线编辑博客支持文本、MD、语音、图片、视频等富媒体内容。

## 技术栈

### 前端
- Vue 3
- Vue Router 4
- Axios
- Editor.js (富媒体编辑器)

### 后端
- Spring Boot 3.5.10
- Spring Web
- Spring Data JPA
- MySQL
- Spring Security

## 项目结构

```
blog/
├── frontend/          # 前端Vue3项目
│   ├── src/
│   │   ├── components/   # 组件
│   │   ├── views/        # 页面
│   │   ├── router/       # 路由配置
│   │   └── main.js        # 入口文件
│   ├── package.json       # 依赖配置
│   └── vite.config.js     # Vite配置
├── backend/           # 后端Spring Boot项目
│   ├── src/
│   │   ├── main/java/com/blog/   # 后端代码
│   │   └── main/resources/       # 配置文件
│   └── pom.xml                   # Maven依赖配置
└── README.md          # 项目说明
```

## 功能特性

1. **非登录查看博客**：无需登录即可浏览所有已发布的博客文章
2. **用户登录**：支持用户注册和登录，基于Spring Security的认证和授权
3. **用户评论**：登录用户可以对博客发表评论，支持评论的展示和管理
4. **后台系统管理员**：完整的后台管理系统，包括博客管理、用户管理等功能
5. **在线编辑博客**：支持文本、Markdown、图片、视频等富媒体内容的编辑

## 安装和运行

### 前提条件
- JDK 17 或更高版本
- MySQL 5.7 或更高版本
- Node.js 16 或更高版本
- npm 或 yarn

### 后端安装和运行

1. **配置数据库**
   - 启动MySQL服务
   - 创建名为 `blog` 的数据库
   - 确保数据库用户有足够的权限

2. **修改数据库配置**
   - 编辑 `backend/src/main/resources/application.properties` 文件
   - 修改数据库连接信息：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/blog?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=123456
   ```

3. **启动后端服务**
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   - 服务将在 http://localhost:8080 上运行

### 前端安装和运行

1. **安装依赖**
   ```bash
   cd frontend
   npm install
   ```

2. **启动前端服务**
   ```bash
   npm run dev
   ```
   - 服务将在 http://localhost:5173 上运行

## 核心功能说明

### 前端功能

1. **首页和博客列表**：展示所有已发布的博客文章，支持分页和搜索
2. **博客详情**：展示博客的详细内容，包括标题、作者、发布时间、浏览量等信息
3. **评论功能**：登录用户可以对博客发表评论，支持评论的展示
4. **用户登录/注册**：支持用户注册和登录，基于Spring Security的认证
5. **后台管理**：
   - 博客管理：创建、编辑、删除博客文章
   - 用户管理：编辑、删除用户
   - 富媒体编辑：支持文本、Markdown、图片、视频等内容的编辑

### 后端功能

1. **用户认证系统**：基于Spring Security的认证和授权，支持用户注册、登录、权限控制
2. **博客管理系统**：支持博客的CRUD操作，富媒体内容的存储和管理
3. **评论系统**：支持评论的发布、管理，关联到博客和用户
4. **文件上传服务**：支持图片、视频等富媒体文件的上传和存储

## API接口

### 认证相关
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/me` - 获取当前用户信息

### 博客相关
- `GET /api/blogs` - 获取所有已发布的博客
- `GET /api/blogs/{id}` - 获取指定博客的详情
- `POST /api/blogs` - 创建新博客
- `PUT /api/blogs/{id}` - 更新博客
- `DELETE /api/blogs/{id}` - 删除博客
- `PUT /api/blogs/{id}/publish` - 发布博客
- `GET /api/blogs/search` - 搜索博客
- `GET /api/blogs/top` - 获取热门博客
- `POST /api/blogs/upload` - 上传文件

### 评论相关
- `GET /api/comments/blog/{blogId}` - 获取指定博客的评论
- `POST /api/comments` - 发表评论
- `PUT /api/comments/{id}` - 更新评论
- `DELETE /api/comments/{id}` - 删除评论

## 富媒体编辑

项目集成了Editor.js富媒体编辑器，支持以下内容类型：
- 文本
- 标题
- 列表
- 代码块
- 链接
- 图片（支持上传）
- 视频（支持嵌入链接）

## 安全配置

- 基于Spring Security的认证和授权
- 密码加密存储
- 角色权限控制（ADMIN/USER）
- CSRF保护

## 数据库设计

### 核心表结构

1. **users** - 用户表
   - id (主键)
   - username (用户名)
   - password (密码)
   - email (邮箱)
   - role (角色)
   - avatar (头像)
   - created_at (创建时间)
   - updated_at (更新时间)

2. **blogs** - 博客表
   - id (主键)
   - title (标题)
   - content (内容)
   - markdown_content (Markdown内容)
   - featured_image (特色图片)
   - status (状态：DRAFT/PUBLISHED)
   - view_count (浏览量)
   - author_id (外键，关联users表)
   - created_at (创建时间)
   - updated_at (更新时间)
   - published_at (发布时间)

3. **comments** - 评论表
   - id (主键)
   - content (内容)
   - user_id (外键，关联users表)
   - blog_id (外键，关联blogs表)
   - created_at (创建时间)
   - updated_at (更新时间)

## 部署说明

### 前端构建
```bash
cd frontend
npm run build
```
构建产物将生成在 `frontend/dist` 目录中

### 后端构建
```bash
cd backend
mvn clean package
```
构建产物将生成在 `backend/target` 目录中

## 注意事项

1. **数据库配置**：确保MySQL服务已启动，并且创建了名为 `blog` 的数据库
2. **端口冲突**：默认前端使用5173端口，后端使用8080端口，确保这些端口未被占用
3. **文件上传**：默认文件上传大小限制为100MB，可以在 `application.properties` 中修改
4. **安全性**：生产环境中应修改默认的管理员账号和密码，以及数据库连接信息

## 许可证

MIT License
