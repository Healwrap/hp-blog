<template>
  <div class="article-list">
    <div class="top-tab">
      <span :class="['tab', orderType === 0 ? 'active' : '']" @click="orderType = 0">热榜</span>
      <el-divider direction="vertical" />
      <span :class="['tab', orderType === 1 ? 'active' : '']" @click="orderType = 1">发布时间</span>
      <el-divider direction="vertical" />
      <span :class="['tab', orderType === 2 ? 'active' : '']" @click="orderType = 2">最新</span>
    </div>
    <div class="article-list-panel">
      <data-list :data-source="articleInfo" :loading="loading" @load-data="loadArticle" desc="暂无文章">
        <template #default="{ data }">
          <article-list-item :data="data" />
        </template>
      </data-list>
    </div>
  </div>
</template>

<script setup>
import forumApi from '@/api/forum'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import ArticleListItem from '@/components/ArticleListItem/ArticleListitem.vue'
import DataList from '@/components/DataList/DataList.vue'

const route = useRoute()
// 一级板块
const pBoardId = ref(null)
const boardId = ref(null)
const loading = ref(false)
const orderType = ref(null)
const articleInfo = ref({})
const loadArticle = async () => {
  loading.value = true
  const result = await forumApi.loadArticle(articleInfo.value.pageNo, orderType.value, boardId.value, pBoardId.value)
  if (!result) {
    return
  }
  articleInfo.value = result.data
  loading.value = false
}
loadArticle()
// 监听排序方式
watch(orderType, () => {
  loadArticle()
})
// 监听路由变化
watch(
  () => route.params,
  newVal => {
    pBoardId.value = newVal.pBoardId
    boardId.value = newVal.boardId
    loadArticle()
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss" scoped>
.article-list {
  position: relative;
  margin: 12px auto;
  padding: 5px;
  background: #fff;
  border-radius: 10px;

  .article-list-panel {
    padding: 5px 5px;
  }

  .top-tab {
    padding: 5px 10px;

    span {
      color: #333;
      font-size: 15px;
      transition: 0.3s all;
      cursor: pointer;

      &:hover {
        color: #409eff;
      }
    }

    span.active {
      color: #409eff;
    }
  }
}
</style>
