import {fileURLToPath, URL} from 'node:url'
import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import eslintPlugin from 'vite-plugin-eslint'

// https://vitejs.dev/config/
export default defineConfig(({ command, mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  return {
    // 需要添加eslintPlugin否则无效果
    plugins: [
      vue(),
      // 增加下面的配置项,这样在运行时就能检查eslint规范
      eslintPlugin({
        include: ['src/**/*.js', 'src/**/*.vue', 'src/*.js', 'src/*.vue']
      })
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    server: {
      hmr: true,
      port: env.VITE_APP_PORT,
      proxy: {
        '/api': {
          target: env.VITE_API_URL,
          changeOrigin: true,
          pathRewrite: {
            '^/api': '/api'
          }
        }
      }
    },
    define: {
      __APP_ENV__: env.APP_ENV
    }
  }
})
