import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
// 引入permission
import '@/permission/index.js'
// 引入katex
import katex from '@/plugins/Katex'
//引入cookies
import VueCookies from 'vue-cookies'
// 引入全局样式
import '@/assets/base.scss'
//引入element plus
import ElementPlus from 'element-plus'
import * as Icons from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
//图标 图标在附件中
import '@/assets/icon/iconfont.css'
// animate.css
import 'animate.css'
// 全局方法
import Verify from '@/utils/Verify'
import Toast from '@/utils/Toast'
import Confirm from '@/utils/Confirm'
import Request from '@/utils/Request'
import store from '@/store'
import Api from '@/api'
// 全局组件
import Dialog from '@/components/Dialog/Dialog.vue'
import DataList from '@/components/DataList/DataList.vue'
import ImageViewer from '@/components/ImageViewer/ImageViewer.vue'
import ImageUpload from '@/components/ImageUpload/ImageUpload.vue'
import FileUpload from '@/components/FileUpload/FileUpload.vue'
import HtmlEditor from '@/components/HtmlEditor/HtmlEditor.vue'
import MarkdownEditor from '@/components/MarkdownEditor/MarkdownEditor.vue'
import Echarts from '@/components/Echarts/Echarts.vue'
import Icon from '@/components/Icon/Icon.vue'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)
app.config.globalProperties.$router = router
app.config.globalProperties.$Verify = Verify
app.config.globalProperties.$message = Toast
app.config.globalProperties.$Request = Request
app.config.globalProperties.$confirm = Confirm
app.config.globalProperties.$store = store
app.config.globalProperties.$VueCookies = VueCookies
app.config.globalProperties.$api = Api

app.component('HtmlEditor', HtmlEditor)
app.component('MarkdownEditor', MarkdownEditor)
app.component('Dialog', Dialog)
app.component('DataList', DataList)
app.component('ImageViewer', ImageViewer)
app.component('ImageUpload', ImageUpload)
app.component('FileUpload', FileUpload)
app.component('Echarts', Echarts)
app.component('Icon', Icon)
// 注册全局组件
Object.keys(Icons).forEach(key => {
  app.component(key, Icons[key])
})
app.directive('katex', katex)
app.mount('#app')
