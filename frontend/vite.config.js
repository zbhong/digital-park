import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())

  return {
    // 基础路径
    base: '/',

    // 开发服务器配置
    server: {
      port: 3000,
      host: '0.0.0.0',
      open: false,
      // 代理配置
      proxy: {
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        }
      }
    },

    // 路径别名
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src')
      },
      // 导入时省略扩展名
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },

    // CSS 配置
    css: {
      preprocessorOptions: {
        scss: {
          api: 'legacy',
          additionalData: ''
        }
      }
    },

    // 插件配置
    plugins: [
      vue(),
      // Element Plus 按需自动导入
      AutoImport({
        resolvers: [ElementPlusResolver()],
        imports: ['vue', 'vue-router', 'pinia'],
        dts: 'src/auto-imports.d.ts'
      }),
      Components({
        resolvers: [ElementPlusResolver()],
        dts: 'src/components.d.ts'
      })
    ],

    // 构建配置
    build: {
      outDir: 'dist',
      assetsDir: 'assets',
      sourcemap: false,
      // 小于此阈值的导入内联为 base64 编码
      assetsInlineLimit: 4096,
      // 代码分割策略
      rollupOptions: {
        output: {
          // 分包策略
          manualChunks: {
            'vue-vendor': ['vue', 'vue-router', 'pinia'],
            'element-plus': ['element-plus', '@element-plus/icons-vue'],
            'echarts': ['echarts']
          },
          // 文件命名
          chunkFileNames: 'assets/js/[name]-[hash].js',
          entryFileNames: 'assets/js/[name]-[hash].js',
          assetFileNames: 'assets/[ext]/[name]-[hash].[ext]'
        }
      },
      // 启用 gzip 压缩大小报告
      reportCompressedSize: true,
      // chunk 大小警告阈值
      chunkSizeWarningLimit: 2000
    }
  }
})
