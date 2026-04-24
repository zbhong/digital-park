# 智慧园区综合管理平台

## 项目简介

智慧园区综合管理平台是一套面向工业园区、产业园区、科技园区等场景的**综合管理解决方案**。平台围绕资产管理、能源管理、环境监测、安全管理、物业管理、招商引资、经济分析等核心业务，提供从数据采集、分析到决策支持的全链路数字化服务，助力园区实现智慧化运营与管理。

### 核心能力

- **资产管理**：资产分类、资产台账、资产盘点、资产维修全生命周期管理
- **能源管理**：电、水、气等多能源类型的实时监测与分析，充电桩与储能管理
- **环境监测**：空气质量、水质等环境指标实时监控与预警
- **安全管理**：安全巡检、消防设备、安全网络、安全生产全方位管控
- **物业管理**：设施管理、费用管理、车辆管理、访客管理、工单管理
- **招商引资**：客户管理、商机跟踪、合同管理、政策管理
- **经济分析**：经济指标监测、企业画像、经济报告
- **应急响应**：应急预案、应急事件、应急物资、应急队伍管理
- **数字孪生**：园区三维可视化，设备与建筑 POI 管理
- **AI 报告**：基于 AI 的智能报告生成

---

## 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.x | 基础框架 |
| Spring Security | 6.x | 安全认证框架 |
| MyBatis-Plus | 3.5.x | ORM 框架 |
| Knife4j | 4.x | API 文档（Swagger 增强） |
| JWT | 0.12.x | Token 认证 |
| Redis | 7.x | 缓存 & 分布式锁 |
| MySQL | 8.0 | 关系型数据库 |
| Hutool | 5.8.x | 工具类库 |
| EasyExcel | 3.x | Excel 导入导出 |

### 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 前端框架 |
| Vite | 5.x | 构建工具 |
| Element Plus | 2.x | UI 组件库 |
| Pinia | 2.x | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| Axios | 1.x | HTTP 请求 |
| ECharts | 5.x | 图表可视化 |

### 基础设施

| 技术 | 说明 |
|------|------|
| Docker | 容器化部署 |
| Docker Compose | 多容器编排 |
| Nginx | 反向代理 & 静态资源服务 |
| Git | 版本控制 |

---

## 快速开始

### 环境要求

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| Docker | >= 20.10 | 容器运行环境 |
| Docker Compose | >= 2.0 | 容器编排工具 |
| Node.js | >= 18.x | 前端开发（本地开发时） |
| JDK | >= 17 | 后端开发（本地开发时） |
| Maven | >= 3.9 | 后端构建（本地开发时） |
| MySQL | >= 8.0 | 数据库（本地开发时） |
| Redis | >= 7.x | 缓存（本地开发时） |

### 方式一：Docker 一键部署（推荐）

```bash
# 1. 克隆项目
git clone https://github.com/your-org/digital-park.git
cd digital-park

# 2. 复制并编辑环境变量文件
cp .env.example .env
# 根据实际情况修改 .env 中的配置

# 3. 构建并启动所有服务
docker compose up -d --build

# 4. 查看服务状态
docker compose ps

# 5. 查看日志
docker compose logs -f backend    # 查看后端日志
docker compose logs -f nginx      # 查看 Nginx 日志
docker compose logs -f mysql      # 查看数据库日志

# 6. 停止所有服务
docker compose down

# 7. 停止并清除数据卷（谨慎使用）
docker compose down -v
```

启动成功后，访问以下地址：

| 服务 | 地址 |
|------|------|
| 前端页面 | http://localhost |
| 后端 API | http://localhost/api |
| 接口文档 | http://localhost/doc.html |

### 方式二：本地开发

#### 后端启动

```bash
# 1. 进入后端目录
cd backend

# 2. 确保 MySQL 和 Redis 已启动
# 创建数据库 digital_park，执行 mysql/init/init.sql 初始化

# 3. 修改配置文件
# 编辑 src/main/resources/application-dev.yml
# 配置数据库连接、Redis 连接、JWT 密钥等

# 4. 使用 Maven 构建
mvn clean install -DskipTests

# 5. 启动应用
mvn spring-boot:run
# 或者
java -jar target/digital-park-backend-1.0.0.jar
```

#### 前端启动

```bash
# 1. 进入前端目录
cd frontend

# 2. 安装依赖
npm install

# 3. 修改 API 地址
# 编辑 .env.development，配置后端 API 地址

# 4. 启动开发服务器
npm run dev

# 5. 构建生产版本
npm run build
```

---

## 项目结构

```
digital-park/
├── docker-compose.yml              # Docker Compose 编排文件
├── .env                            # 环境变量配置（敏感信息，不入库）
├── .env.example                    # 环境变量示例文件
├── .gitignore                      # Git 忽略配置
├── README.md                       # 项目文档
│
├── backend/                        # 后端服务
│   ├── Dockerfile                  # 后端 Docker 镜像构建文件
│   ├── pom.xml                     # Maven 项目配置
│   └── src/
│       └── main/
│           ├── java/com/digitalpark/
│           │   ├── DigitalParkApplication.java   # 启动类
│           │   ├── common/                        # 公共模块
│           │   │   ├── annotation/                # 自定义注解
│           │   │   ├── aspect/                    # AOP 切面
│           │   │   ├── base/                      # 基础类
│           │   │   ├── exception/                 # 异常处理
│           │   │   ├── result/                    # 统一响应
│           │   │   └── utils/                     # 工具类
│           │   ├── config/                        # 配置类
│           │   ├── modules/                       # 业务模块
│           │   │   ├── system/                    # 系统管理
│           │   │   ├── asset/                     # 资产管理
│           │   │   ├── energy/                    # 能源管理
│           │   │   ├── environment/               # 环境监测
│           │   │   ├── safety/                    # 安全管理
│           │   │   ├── property/                  # 物业管理
│           │   │   ├── investment/                # 招商引资
│           │   │   ├── economy/                   # 经济分析
│           │   │   ├── emergency/                 # 应急响应
│           │   │   ├── twin/                      # 数字孪生
│           │   │   ├── enterprise/                # 企业服务
│           │   │   └── aireport/                  # AI 报告
│           │   └── security/                      # 安全框架配置
│           └── resources/
│               ├── application.yml                # 主配置文件
│               ├── application-dev.yml            # 开发环境配置
│               ├── init.sql                       # 数据库初始化脚本
│               └── mapper/                        # MyBatis XML 映射文件
│
├── frontend/                       # 前端服务
│   ├── Dockerfile                  # 前端 Docker 镜像构建文件
│   ├── nginx.conf                  # Nginx 配置（前端容器内）
│   ├── package.json                # 依赖配置
│   ├── index.html                  # HTML 入口
│   └── src/
│       ├── main.js                 # 应用入口
│       ├── App.vue                 # 根组件
│       ├── router/                 # 路由配置
│       ├── store/                  # Pinia 状态管理
│       ├── api/                    # API 接口定义
│       ├── views/                  # 页面组件
│       │   ├── login/              # 登录页
│       │   ├── dashboard/          # 首页大屏
│       │   ├── asset/              # 资产管理
│       │   ├── energy/             # 能源管理
│       │   ├── environment/        # 环境监测
│       │   ├── safety/             # 安全管理
│       │   ├── property/           # 物业管理
│       │   ├── investment/         # 招商引资
│       │   ├── economy/            # 经济分析
│       │   ├── emergency/          # 应急响应
│       │   ├── twin/               # 数字孪生
│       │   ├── system/             # 系统管理
│       │   └── aiReport/           # AI 报告
│       ├── layout/                 # 布局组件
│       ├── directive/              # 自定义指令
│       ├── styles/                 # 全局样式
│       └── utils/                  # 工具函数
│
├── mysql/
│   └── init/
│       └── init.sql                # 数据库初始化脚本
│
├── nginx/                          # Nginx 反向代理配置
│   ├── nginx.conf                  # Nginx 主配置
│   ├── conf.d/                     # 站点配置目录
│   │   └── default.conf            # 默认站点配置
│   └── ssl/                        # SSL 证书目录
│
└── docs/                           # 项目文档
    ├── api/                        # 接口文档
    ├── deploy/                     # 部署文档
    └── design/                     # 设计文档
```

---

## API 文档

项目集成 **Knife4j**（基于 OpenAPI 3.0）提供在线接口文档。

| 环境 | 地址 |
|------|------|
| Docker 部署 | http://localhost/doc.html |
| 本地开发 | http://localhost:8080/doc.html |

接口文档支持：
- 在线调试接口
- 查看请求/响应示例
- 全局 Token 认证
- 接口分类浏览

---

## 常见问题

### Q1: Docker 启动后后端服务无法连接数据库

**原因**：MySQL 容器尚未完成初始化。

**解决方案**：
```bash
# 查看 MySQL 容器日志，确认初始化完成
docker compose logs mysql

# 等待 MySQL 健康检查通过后再重启后端
docker compose restart backend
```

### Q2: 前端页面刷新后出现 404

**原因**：Nginx 未正确配置 Vue Router history 模式。

**解决方案**：确认 `nginx.conf` 中包含以下配置：
```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

### Q3: 文件上传失败

**原因**：文件大小超过 Nginx 或 Spring Boot 限制。

**解决方案**：
- Nginx 配置 `client_max_body_size 100m;`
- Spring Boot 配置 `spring.servlet.multipart.max-file-size=100MB`

### Q4: Redis 连接失败

**原因**：Redis 密码未正确配置。

**解决方案**：确认 `.env` 文件中 `REDIS_PASSWORD` 与 Redis 容器的 `requirepass` 一致。

### Q5: 如何查看服务日志

```bash
# 查看所有服务日志
docker compose logs -f

# 查看指定服务日志
docker compose logs -f backend
docker compose logs -f mysql
docker compose logs -f nginx

# 查看最近 100 行日志
docker compose logs --tail=100 backend
```

### Q6: 如何备份数据库

```bash
# 导出数据库
docker compose exec mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} digital_park > backup.sql

# 导入数据库
docker compose exec -T mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} digital_park < backup.sql
```

### Q7: 如何修改端口

编辑 `.env` 文件，修改对应端口变量：
```bash
BACKEND_PORT=9000       # 后端端口
NGINX_HTTP_PORT=8080    # Nginx HTTP 端口
NGINX_HTTPS_PORT=8443   # Nginx HTTPS 端口
MYSQL_PORT=3307         # MySQL 端口
REDIS_PORT=6380         # Redis 端口
```

修改后执行 `docker compose up -d` 重新启动服务。

### Q8: 如何配置 HTTPS

1. 将 SSL 证书文件放入 `nginx/ssl/` 目录
2. 修改 `nginx/conf.d/default.conf`，取消 SSL 相关配置的注释
3. 重启 Nginx 服务：`docker compose restart nginx`

---

## 许可证

本项目采用 [MIT License](LICENSE) 开源协议。
