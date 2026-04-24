import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'
import store from './store'
import directive from './directive'

// 导入全局样式
import '@/styles/index.scss'

// 创建应用实例
const app = createApp(App)

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册 Element Plus（中文语言包）
app.use(ElementPlus, {
  locale: zhCn,
  size: 'default'
})

// 注册路由
app.use(router)

// 注册 Pinia 状态管理
app.use(store)

// 注册自定义指令（作为Vue插件）
app.use(directive)

// 挂载应用
app.mount('#app')
