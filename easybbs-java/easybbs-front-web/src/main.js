import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
//引入cookies
import VueCookies from 'vue-cookies'
//引入element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/base.scss'
//图标 图标在附件中
import '@/assets/icon/iconfont.css'
// 全局方法
import Verify from '@/utils/Verify'
import Toast from '@/utils/Toast'
import Request from '@/utils/Request'
import store from '@/store'
// 全局组件
import Dialog from '@/components/Dialog/Dialog.vue'
import ImageViewer from '@/components/ImageViewer/ImageViewer.vue'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)
app.config.globalProperties.Verify = Verify
app.config.globalProperties.Toast = Toast
app.config.globalProperties.Request = Request
app.config.globalProperties.store = store
app.config.globalProperties.VueCookies = VueCookies

app.config.globalProperties.globalInfo = {
  headerHeight: '60px',
  bodyWidth: '1300px'
}

app.component('Dialog', Dialog)
app.component('ImageViewer', ImageViewer)

app.mount('#app')
