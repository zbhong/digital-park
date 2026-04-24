# ============================================================
# 阶段一：Node.js 构建
# ============================================================
FROM node:18-alpine AS builder

WORKDIR /build

# 设置 npm 镜像源（国内加速）
RUN npm config set registry https://registry.npmmirror.com

# 先复制 package.json，利用 Docker 缓存层加速依赖安装
COPY package.json package-lock.json* ./
RUN npm install

# 复制源代码并构建
COPY . .
RUN npm run build

# ============================================================
# 阶段二：Nginx 部署
# ============================================================
FROM nginx:1.25-alpine

LABEL maintainer="digital-park-team"
LABEL description="智慧园区综合管理平台 - 前端服务"

# 安装必要工具
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

# 删除默认 Nginx 配置
RUN rm -rf /usr/share/nginx/html/* \
    && rm /etc/nginx/conf.d/default.conf

# 从构建阶段复制构建产物
COPY --from=builder /build/dist /usr/share/nginx/html

# 复制自定义 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 创建日志目录
RUN mkdir -p /var/log/nginx \
    && chown -R nginx:nginx /usr/share/nginx/html /var/log/nginx

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
