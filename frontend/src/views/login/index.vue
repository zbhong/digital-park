<template>
  <div class="login-container">
    <!-- 深色科技风格背景 -->
    <div class="login-bg">
      <!-- 顶部深蓝渐变 -->
      <div class="bg-gradient"></div>
      <!-- 科技网格线 -->
      <div class="bg-grid"></div>
      <!-- 科技线条装饰 -->
      <svg class="bg-tech-lines" viewBox="0 0 1920 1080" preserveAspectRatio="none">
        <defs>
          <linearGradient id="lineGrad1" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" style="stop-color:#409eff;stop-opacity:0" />
            <stop offset="50%" style="stop-color:#409eff;stop-opacity:0.3" />
            <stop offset="100%" style="stop-color:#409eff;stop-opacity:0" />
          </linearGradient>
          <linearGradient id="lineGrad2" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" style="stop-color:#67c23a;stop-opacity:0" />
            <stop offset="50%" style="stop-color:#67c23a;stop-opacity:0.2" />
            <stop offset="100%" style="stop-color:#67c23a;stop-opacity:0" />
          </linearGradient>
        </defs>
        <!-- 水平科技线 -->
        <line x1="-100" y1="200" x2="800" y2="200" stroke="url(#lineGrad1)" stroke-width="1.5" />
        <line x1="200" y1="350" x2="1200" y2="350" stroke="url(#lineGrad2)" stroke-width="1" />
        <line x1="-50" y1="500" x2="900" y2="500" stroke="url(#lineGrad1)" stroke-width="1" />
        <line x1="400" y1="650" x2="1920" y2="650" stroke="url(#lineGrad2)" stroke-width="1.5" />
        <line x1="100" y1="800" x2="1100" y2="800" stroke="url(#lineGrad1)" stroke-width="1" />
        <!-- 垂直连接线 -->
        <line x1="300" y1="200" x2="300" y2="500" stroke="url(#lineGrad2)" stroke-width="0.8" />
        <line x1="600" y1="350" x2="600" y2="650" stroke="url(#lineGrad1)" stroke-width="0.8" />
        <line x1="900" y1="200" x2="900" y2="500" stroke="url(#lineGrad2)" stroke-width="0.8" />
        <!-- 节点圆点 -->
        <circle cx="300" cy="200" r="4" fill="#409eff" opacity="0.6" />
        <circle cx="300" cy="500" r="4" fill="#409eff" opacity="0.6" />
        <circle cx="600" cy="350" r="4" fill="#67c23a" opacity="0.5" />
        <circle cx="600" cy="650" r="4" fill="#67c23a" opacity="0.5" />
        <circle cx="900" cy="200" r="4" fill="#67c23a" opacity="0.5" />
        <circle cx="900" cy="500" r="4" fill="#67c23a" opacity="0.5" />
      </svg>
      <!-- 底部城市剪影 -->
      <div class="bg-cityscape">
        <svg viewBox="0 0 1920 200" preserveAspectRatio="none" class="city-svg">
          <defs>
            <linearGradient id="cityGrad" x1="0%" y1="0%" x2="0%" y2="100%">
              <stop offset="0%" style="stop-color:#0a2a4a;stop-opacity:0.8" />
              <stop offset="100%" style="stop-color:#061a30;stop-opacity:0.3" />
            </linearGradient>
          </defs>
          <!-- 建筑群 -->
          <rect x="50" y="80" width="40" height="120" fill="url(#cityGrad)" />
          <rect x="100" y="40" width="50" height="160" fill="url(#cityGrad)" />
          <rect x="160" y="100" width="35" height="100" fill="url(#cityGrad)" />
          <rect x="210" y="20" width="60" height="180" fill="url(#cityGrad)" />
          <rect x="280" y="60" width="45" height="140" fill="url(#cityGrad)" />
          <rect x="340" y="90" width="55" height="110" fill="url(#cityGrad)" />
          <rect x="410" y="30" width="40" height="170" fill="url(#cityGrad)" />
          <rect x="460" y="70" width="50" height="130" fill="url(#cityGrad)" />
          <rect x="530" y="50" width="35" height="150" fill="url(#cityGrad)" />
          <rect x="580" y="80" width="60" height="120" fill="url(#cityGrad)" />
          <rect x="660" y="10" width="45" height="190" fill="url(#cityGrad)" />
          <rect x="720" y="60" width="40" height="140" fill="url(#cityGrad)" />
          <rect x="780" y="40" width="55" height="160" fill="url(#cityGrad)" />
          <rect x="850" y="90" width="35" height="110" fill="url(#cityGrad)" />
          <rect x="900" y="50" width="50" height="150" fill="url(#cityGrad)" />
          <rect x="970" y="70" width="60" height="130" fill="url(#cityGrad)" />
          <rect x="1050" y="20" width="40" height="180" fill="url(#cityGrad)" />
          <rect x="1100" y="60" width="55" height="140" fill="url(#cityGrad)" />
          <rect x="1170" y="40" width="35" height="160" fill="url(#cityGrad)" />
          <rect x="1220" y="80" width="50" height="120" fill="url(#cityGrad)" />
          <rect x="1290" y="30" width="45" height="170" fill="url(#cityGrad)" />
          <rect x="1350" y="70" width="60" height="130" fill="url(#cityGrad)" />
          <rect x="1430" y="50" width="40" height="150" fill="url(#cityGrad)" />
          <rect x="1480" y="90" width="55" height="110" fill="url(#cityGrad)" />
          <rect x="1550" y="20" width="35" height="180" fill="url(#cityGrad)" />
          <rect x="1600" y="60" width="50" height="140" fill="url(#cityGrad)" />
          <rect x="1670" y="40" width="60" height="160" fill="url(#cityGrad)" />
          <rect x="1750" y="80" width="40" height="120" fill="url(#cityGrad)" />
          <rect x="1810" y="50" width="55" height="150" fill="url(#cityGrad)" />
          <!-- 窗户灯光 -->
          <rect x="220" y="40" width="6" height="6" fill="#FFD54F" opacity="0.6" />
          <rect x="235" y="55" width="6" height="6" fill="#FFD54F" opacity="0.4" />
          <rect x="250" y="40" width="6" height="6" fill="#FFD54F" opacity="0.5" />
          <rect x="670" y="30" width="6" height="6" fill="#FFD54F" opacity="0.6" />
          <rect x="685" y="50" width="6" height="6" fill="#FFD54F" opacity="0.4" />
          <rect x="1060" y="40" width="6" height="6" fill="#FFD54F" opacity="0.5" />
          <rect x="1075" y="60" width="6" height="6" fill="#FFD54F" opacity="0.6" />
          <rect x="1300" y="50" width="6" height="6" fill="#FFD54F" opacity="0.4" />
          <rect x="1560" y="40" width="6" height="6" fill="#FFD54F" opacity="0.5" />
        </svg>
      </div>
      <!-- 光晕效果 -->
      <div class="bg-glow glow-1"></div>
      <div class="bg-glow glow-2"></div>
      <div class="bg-glow glow-3"></div>
    </div>

    <!-- 左侧品牌信息 -->
    <div class="login-brand">
      <div class="brand-logo">
        <svg viewBox="0 0 80 80" class="logo-icon">
          <circle cx="40" cy="40" r="36" fill="none" stroke="#409eff" stroke-width="2.5" opacity="0.9" />
          <circle cx="40" cy="40" r="28" fill="none" stroke="#409eff" stroke-width="1.5" opacity="0.5" />
          <!-- 闪电符号 -->
          <path d="M44 18 L34 42 L42 42 L36 62 L52 36 L43 36 Z" fill="#409eff" opacity="0.9" />
        </svg>
      </div>
      <h1 class="brand-title">银河数字园区一体化平台</h1>
      <p class="brand-subtitle">Digital Park Integrated Management Platform</p>
      <div class="brand-features">
        <div class="feature-item">
          <span class="feature-icon">&#9889;</span>
          <span class="feature-text">智慧能源管理</span>
        </div>
        <div class="feature-item">
          <span class="feature-icon">&#127793;</span>
          <span class="feature-text">安全应急联动</span>
        </div>
        <div class="feature-item">
          <span class="feature-icon">&#129302;</span>
          <span class="feature-text">AI智能分析</span>
        </div>
        <div class="feature-item">
          <span class="feature-icon">&#127981;</span>
          <span class="feature-text">驾驶舱全景</span>
        </div>
      </div>
    </div>

    <!-- 登录表单 -->
    <div class="login-card">
      <div class="login-header">
        <div class="login-logo">
          <svg viewBox="0 0 40 40" class="card-logo-icon">
            <circle cx="20" cy="20" r="18" fill="none" stroke="#409eff" stroke-width="1.5" />
            <path d="M22 9 L17 21 L21 21 L18 31 L27 19 L22 19 Z" fill="#409eff" />
          </svg>
        </div>
        <h2 class="login-title">系统登录</h2>
        <p class="login-subtitle">System Login</p>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        size="large"
      >
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            clearable
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
            clearable
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <!-- 记住密码 -->
        <el-form-item>
          <div class="login-options">
            <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
          </div>
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <p>Copyright &copy; 2024 银河数字园区 All Rights Reserved</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/modules'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 表单引用
const loginFormRef = ref(null)

// 加载状态
const loading = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 表单校验规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 20, message: '密码长度在 5 到 20 个字符', trigger: 'blur' }
  ]
}

/**
 * 处理登录
 */
function handleLogin() {
  loginFormRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      await userStore.login(loginForm)
      // 获取重定向地址
      const redirect = route.query.redirect || '/'
      router.push(redirect)
      ElMessage.success('登录成功')
    } catch (error) {
      ElMessage.error(error.message || '登录失败，请检查用户名和密码')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.login-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  background: #0a1929;
  position: relative;
  overflow: hidden;
}

// ===== 背景层 =====
.login-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
}

// 深蓝渐变底色
.bg-gradient {
  position: absolute;
  width: 100%;
  height: 100%;
  background:
    radial-gradient(ellipse at 20% 50%, rgba(0, 75, 151, 0.4) 0%, transparent 60%),
    radial-gradient(ellipse at 80% 20%, rgba(64, 158, 255, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 50% 100%, rgba(0, 40, 80, 0.6) 0%, transparent 60%),
    linear-gradient(180deg, #061a30 0%, #0a2a4a 40%, #0d3358 70%, #0a1929 100%);
}

// 科技网格
.bg-grid {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image:
    linear-gradient(rgba(64, 158, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(64, 158, 255, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
}

// 科技线条 SVG
.bg-tech-lines {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  animation: techPulse 8s ease-in-out infinite;
}

@keyframes techPulse {
  0%, 100% { opacity: 0.6; }
  50% { opacity: 1; }
}

// 城市剪影
.bg-cityscape {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 200px;

  .city-svg {
    width: 100%;
    height: 100%;
  }
}

// 光晕
.bg-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
}

.glow-1 {
  width: 400px;
  height: 400px;
  background: rgba(64, 158, 255, 0.08);
  top: 10%;
  left: 15%;
  animation: glowFloat 10s ease-in-out infinite;
}

.glow-2 {
  width: 300px;
  height: 300px;
  background: rgba(0, 75, 151, 0.12);
  bottom: 20%;
  left: 40%;
  animation: glowFloat 12s ease-in-out infinite reverse;
}

.glow-3 {
  width: 250px;
  height: 250px;
  background: rgba(103, 194, 58, 0.06);
  top: 40%;
  right: 30%;
  animation: glowFloat 8s ease-in-out infinite;
}

@keyframes glowFloat {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(20px, -15px); }
}

// ===== 左侧品牌信息 =====
.login-brand {
  position: absolute;
  left: 8%;
  top: 50%;
  transform: translateY(-50%);
  z-index: 1;
  color: #fff;
}

.brand-logo {
  margin-bottom: 24px;

  .logo-icon {
    width: 80px;
    height: 80px;
    filter: drop-shadow(0 0 20px rgba(64, 158, 255, 0.4));
  }
}

.brand-title {
  font-size: 36px;
  font-weight: 700;
  color: #ffffff;
  margin: 0 0 12px;
  letter-spacing: 4px;
  text-shadow: 0 2px 20px rgba(0, 0, 0, 0.5);
}

.brand-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  margin: 0 0 48px;
  letter-spacing: 2px;
  font-weight: 300;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 8px;
  backdrop-filter: blur(4px);
  transition: all 0.3s;

  &:hover {
    background: rgba(64, 158, 255, 0.1);
    border-color: rgba(64, 158, 255, 0.3);
    transform: translateX(4px);
  }
}

.feature-icon {
  font-size: 20px;
  width: 28px;
  text-align: center;
}

.feature-text {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.75);
  letter-spacing: 1px;
}

// ===== 登录卡片 =====
.login-card {
  width: 420px;
  margin-right: 10%;
  padding: 40px;
  background: rgba(10, 30, 55, 0.85);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  box-shadow:
    0 20px 60px rgba(0, 0, 0, 0.5),
    0 0 80px rgba(0, 75, 151, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.login-logo {
  margin-bottom: 16px;

  .card-logo-icon {
    width: 48px;
    height: 48px;
    filter: drop-shadow(0 0 12px rgba(64, 158, 255, 0.5));
  }
}

.login-title {
  font-size: 22px;
  font-weight: 600;
  color: #ffffff;
  margin: 0 0 8px;
  letter-spacing: 4px;
}

.login-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
  margin: 0;
  letter-spacing: 2px;
  text-transform: uppercase;
}

.login-form {
  :deep(.el-input) {
    .el-input__wrapper {
      border-radius: 8px;
      padding: 4px 12px;
      background: rgba(255, 255, 255, 0.06);
      border: 1px solid rgba(255, 255, 255, 0.1);
      box-shadow: none;
      transition: all 0.3s;

      &:hover {
        border-color: rgba(64, 158, 255, 0.4);
        background: rgba(255, 255, 255, 0.08);
      }

      &.is-focus {
        border-color: #409eff;
        background: rgba(64, 158, 255, 0.08);
        box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.15);
      }
    }

    .el-input__inner {
      color: #ffffff;

      &::placeholder {
        color: rgba(255, 255, 255, 0.35);
      }
    }

    .el-input__prefix {
      color: rgba(255, 255, 255, 0.4);
    }

    .el-input__suffix {
      color: rgba(255, 255, 255, 0.4);
    }
  }

  :deep(.el-form-item) {
    margin-bottom: 22px;
  }

  :deep(.el-checkbox) {
    .el-checkbox__label {
      color: rgba(255, 255, 255, 0.6);
    }

    .el-checkbox__inner {
      background: rgba(255, 255, 255, 0.06);
      border-color: rgba(255, 255, 255, 0.2);
    }

    &.is-checked {
      .el-checkbox__inner {
        background: #409eff;
        border-color: #409eff;
      }

      .el-checkbox__label {
        color: #409eff;
      }
    }
  }
}

.login-options {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.login-btn {
  width: 100%;
  height: 46px;
  border-radius: 8px;
  font-size: 16px;
  letter-spacing: 6px;
  background: linear-gradient(135deg, #409eff 0%, #3a8ee6 50%, #2563c0 100%);
  border: none;
  box-shadow: 0 4px 20px rgba(64, 158, 255, 0.3);
  transition: all 0.3s;

  &:hover,
  &:focus {
    background: linear-gradient(135deg, #66b1ff 0%, #409eff 50%, #3a8ee6 100%);
    box-shadow: 0 6px 30px rgba(64, 158, 255, 0.4);
    transform: translateY(-1px);
  }

  &:active {
    transform: translateY(0);
    box-shadow: 0 2px 10px rgba(64, 158, 255, 0.3);
  }
}

.login-footer {
  text-align: center;
  margin-top: 28px;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);

  p {
    font-size: 11px;
    color: rgba(255, 255, 255, 0.25);
    margin: 0;
    letter-spacing: 1px;
  }
}

// ===== 响应式适配 =====
@media screen and (max-width: 1200px) {
  .login-brand {
    left: 5%;
  }

  .brand-title {
    font-size: 28px;
  }

  .login-card {
    margin-right: 5%;
  }
}

@media screen and (max-width: 900px) {
  .login-container {
    justify-content: center;
  }

  .login-brand {
    display: none;
  }

  .login-card {
    margin-right: 0;
    width: 90%;
    max-width: 420px;
  }
}
</style>
