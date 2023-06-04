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
    <el-form :model="formData" @submit.prevent>
      <!-- TODO form内只有一个输入框时,按回车会自动提交 -->
      <el-form-item prop="keyword">
        <el-input v-model="formData.keyword" size="large" placeholder="请输入想要搜索的文章" clearable @input="search">
          <template #prefix>
            <span class="iconfont icon-search"></span>
          </template>
        </el-input>
      </el-form-item>
    </el-form>
    <data-list :loading="loading" :data-source="dataList" desc="未搜索到结果" @load-data="search">
      <template #default="{ data }">
        <div class="search-item" @click="proxy.$router.push({ path: `/article/${data.articleId}` })">
          <div class="title">{{ data.title }}</div>
          <div class="userInfo">
            <div class="author">@{{ data.nickName }}</div>
            <div class="post-time">{{ data.postTime }}</div>
          </div>
          <div class="desc">{{ data.summary }}</div>
        </div>
      </template>
    </data-list>
  </Dialog>
</template>

<script setup>
import { getCurrentInstance, reactive, ref } from 'vue'
import DataList from '@/components/DataList/DataList.vue'

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
  loading.value = true
  if (!formData.value.keyword) {
    return
  }
  const result = await proxy.$api.forum.search(formData.value.keyword, dataList.value.pageNo)
  if (!result) {
    return
  }
  dataList.value = result.data
  loading.value = false
}
// 监听到路由变化时关闭搜索框
proxy.$router.afterEach(() => {
  close()
})
</script>

<style lang="scss" scoped>
.search-item {
  @apply py-[10px] cursor-pointer;
  border-bottom: 1px solid #ebebeb;

  .title {
    @apply text-[16px] font-bold mb-[10px] text-[var(--text-color-3)];
  }

  .userInfo {
    @apply flex items-center mb-[10px];

    .author {
      @apply text-[12px] text-[var(--text-color-2)] mr-[10px];
    }

    .post-time {
      @apply text-[12px] text-[var(--text-color-2)];
    }
  }

  .desc {
    @apply text-[14px] text-[var(--text-color-2)];
  }
}
</style>
