import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
//引入cookies
import VueCookies from 'vue-cookies'
// 引入全局样式
import '@/assets/base.scss' // 在element-plus之前引入，防止tailwind覆盖element-plus样式
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
import Api from '@/api'
// 全局组件
import Dialog from '@/components/Dialog/Dialog.vue'
import DataList from '@/components/DataList/DataList.vue'
import Table from '@/components/Table/Table.vue'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)
app.config.globalProperties.$router = router
app.config.globalProperties.$Verify = Verify
app.config.globalProperties.$Toast = Toast
app.config.globalProperties.$Request = Request
app.config.globalProperties.$confirm = Confirm
app.config.globalProperties.$VueCookies = VueCookies
app.config.globalProperties.$api = Api

app.component('Dialog', Dialog)
app.component('DataList', DataList)
app.component('Table', Table)
// 注册全局组件
Object.keys(Icons).forEach(key => {
  app.component(key, Icons[key])
})
app.mount('#app')
