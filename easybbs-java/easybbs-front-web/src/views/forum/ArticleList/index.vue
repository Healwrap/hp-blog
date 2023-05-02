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
      <div class="no-data">
        <el-empty v-if="!loading && articleInfo.list.length === 0" />
      </div>
      <div v-if="loading" class="skeleton">
        <el-skeleton :row="3" animated />
      </div>
      <div v-for="item in articleInfo.list" v-else :key="item.articleId">
        <ArticleListItem :data="item" />
      </div>
      <el-pagination
        v-model:current-page="currentPage"
        hide-on-single-page
        :total="articleInfo.totalCount"
        :page-size="articleInfo.pageSize"
        layout="prev, pager, next"
        @current-change="loadArticle(currentPage)"
      />
    </div>
  </div>
</template>

<script setup>
import forumApi from '@/api/forum'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import ArticleListItem from './components/ArticleListItem/ArticleListitem.vue'

const route = useRoute()
// 一级板块
const pBoardId = ref(0)
const boardId = ref(0)
const loading = ref(false)
const orderType = ref(0)
const currentPage = ref(0)
const articleInfo = ref({})
const loadArticle = async () => {
  loading.value = true
  const result = await forumApi.loadArticle(currentPage.value, orderType.value, boardId.value, pBoardId.value)
  if (!result) {
    return
  }
  articleInfo.value = result.data
  currentPage.value = result.data.pageNo
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
  margin: 10px auto;
  background: #fff;

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
