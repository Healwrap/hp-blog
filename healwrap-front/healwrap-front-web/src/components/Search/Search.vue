<template>
  <Dialog
    :show="dialogConfig.show"
    :title="dialogConfig.title"
    :top="dialogConfig.top"
    :width="dialogConfig.width"
    :buttons="dialogConfig.buttons"
    :show-close="dialogConfig.showClose"
    :show-cancel="dialogConfig.showCancel"
    :draggable="dialogConfig.draggable"
    @close="close"
  >
    <el-form :model="formData">
      <el-form-item prop="keyword">
        <el-input v-model="formData.keyword" size="large" placeholder="请输入想要搜索的文章" clearable @change="search">
          <template #prefix>
            <span class="iconfont icon-search"></span>
          </template>
        </el-input>
      </el-form-item>
    </el-form>
    <data-list :loading="loading" :data-source="dataList" desc="未搜索到结果" @load-data="search">
      <template #default="{ data }">
        <article-list-item :data="data" />
      </template>
    </data-list>
  </Dialog>
</template>

<script setup>
import { getCurrentInstance, reactive, ref } from 'vue'
import DataList from '@/components/DataList/DataList.vue'
import ArticleListItem from '@/components/ArticleListItem/ArticleListitem.vue'

const { proxy } = getCurrentInstance()
const formData = ref({})
const dataList = ref({ list: [], pageNO: 1 })
const loading = ref(true)
const dialogConfig = reactive({
  show: false,
  title: '搜索',
  showClose: true,
  showCancel: false,
  top: '8vh',
  width: '48%',
  draggable: false,
  buttons: []
})
// 显示与关闭
const close = () => {
  formData.value = {}
  dataList.value = { list: [], pageNo: 1 }
  console.log(dataList.value)
  console.log(formData.value)
  dialogConfig.show = false
}
const show = () => {
  dialogConfig.show = true
}
defineExpose({
  show
})
// 搜索
const search = async () => {
  if (!formData.value.keyword) {
    return
  }
  const result = await proxy.$api.forum.search(formData.value.keyword, dataList.value.pageNo)
  if (!result) {
    return
  }
  dataList.value = result.data
}
// 监听到路由变化时关闭搜索框
proxy.$router.afterEach(() => {
  close()
})
</script>

<style scoped></style>
