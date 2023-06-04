<template>
  <div class="user-center">
    <!--顶部面板屑-->
    <div class="user-banner">
      <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>个人中心</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!--个人中心-->
    <div class="user-panel">
      <div class="user-info animate__animated animate__fadeIn">
        <el-affix :offset="30">
          <div class="user-side">
            <!--头像-->
            <div class="avatar-panel">
              <div class="edit-btn" v-if="userId === currentUserId">
                <span class="iconfont icon-edit" @click="updateUserInfo">&nbsp;编辑资料</span>
              </div>
              <div class="avatar">
                <img :src="proxy.$api.account.avatarUrl(userId)" />
              </div>
              <div class="nickname">
                <span>{{ userInfo.nickName }}</span>
                <span class="sex">
                  <span v-if="userInfo.sex === 0" class="iconfont icon-woman" style="color: #ff006a"></span>
                  <span v-if="userInfo.sex === 1" class="iconfont icon-man" style="color: #0080ff"></span>
                </span>
              </div>
              <div class="desc">
                <span>{{ userInfo.personDescription }}</span>
              </div>
            </div>
            <!--信息-->
            <div class="info-panel">
              <div class="info-item">
                <span class="label iconfont icon-integral">&nbsp;积分</span>
                <span class="value" v-if="userId === currentUserId" style="color: #1e88e5; cursor: pointer" @click="showIntegralRecord">{{
                  userInfo.currentIntegral
                }}</span>
                <span class="value" v-else>{{ userInfo.currentIntegral }}</span>
              </div>
              <div class="info-item">
                <span class="label iconfont icon-like">&nbsp;获赞</span>
                <span class="value">{{ userInfo.likeCount }}</span>
              </div>
              <div class="info-item">
                <span class="label iconfont icon-edit">&nbsp;发帖</span>
                <span class="value">{{ userInfo.postCount }}</span>
              </div>
              <div class="info-item">
                <span class="label iconfont icon-register">&nbsp;加入</span>
                <span class="value">{{ userInfo.joinTime }}</span>
              </div>
              <div class="info-item">
                <span class="label iconfont icon-login">&nbsp;最后登录</span>
                <span class="value">{{ userInfo.lastLoginTime }}</span>
              </div>
            </div>
          </div>
        </el-affix>
      </div>
      <div class="article-side">
        <!--发文详情图表-->
        <div class="profile-panel animate__animated animate__fadeIn">
          <div class="desc">简介.........简介</div>
          <echarts class="echarts" :option="option" :height="200" />
        </div>
        <el-tabs class="tabs animate__animated animate__fadeIn" v-model:model-value="activeTagName" @tab-change="changeTab">
          <el-tab-pane label="发帖" :name="0" />
          <el-tab-pane label="评论" :name="1" />
          <el-tab-pane label="点赞" :name="2" />
        </el-tabs>
        <div class="article-list">
          <data-list :data-source="articleInfoList" :loading="loading" :rows="7" @load-data="loadArticle" desc="暂无文章">
            <template #default="{ data }">
              <article-list-item :data="data" />
            </template>
          </data-list>
        </div>
      </div>
    </div>
    <!--编辑个人信息-->
    <user-info-editor ref="userInfoEditor" />
    <!--显示积分记录-->
    <user-integral-record ref="integralRecordRef" />
  </div>
</template>

<script setup>
import { ArrowRight } from '@element-plus/icons-vue'
import { getCurrentInstance, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import router from '@/router'
import { getCssVar } from '@/utils/Utils'
import ArticleListItem from '@/components/ArticleListItem/ArticleListItem.vue'
import UserInfoEditor from '@/views/User/Center/components/UserInfoEditor.vue'
import UserIntegralRecord from './components/UserIntegralRecord.vue'

const { proxy } = getCurrentInstance()

function getVirtualData(year) {
  const startDate = new Date(`${year}-01-01`)
  const endDate = new Date(`${+year + 1}-01-01`)
  const dayTime = 3600 * 24 * 1000
  const data = []
  for (let time = startDate.getTime(); time < endDate.getTime(); time += dayTime) {
    const date = new Date(time)
    const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
    data.push([formattedDate, Math.floor(Math.random() * 10)])
  }
  return data
}

// 图表配置
const option = reactive({
  title: {
    top: 10,
    text: '发文详情',
    textStyle: {
      color: getCssVar('--text-color')
    }
  },
  tooltip: {
    trigger: 'item'
  },
  visualMap: {
    min: 0,
    max: 10,
    type: 'piecewise',
    orient: 'horizontal',
    left: 'center',
    bottom: 20,
    inRange: {
      color: [getCssVar('--heatmap-color-01'), getCssVar('--heatmap-color-02'), getCssVar('--heatmap-color-03')]
    },
    textStyle: {
      color: getCssVar('--text-color')
    }
  },
  calendar: {
    top: 60,
    left: 30,
    right: 30,
    cellSize: ['auto', 13],
    range: '2022',
    itemStyle: {
      borderWidth: 0.5
    },
    yearLabel: {
      show: false,
      textStyle: {
        color: getCssVar('--text-color')
      }
    }
  },
  series: {
    type: 'heatmap',
    coordinateSystem: 'calendar',
    data: getVirtualData('2022')
  }
})
const integralRecordRef = ref(null)
const route = useRoute()
const currentUserId = ref(null)
const userId = ref(null)
const userInfo = ref({})
const loading = ref(true)
const articleInfoList = ref([])
const activeTagName = ref(0)
const userInfoEditor = ref(null)
const loadUserInfo = async () => {
  const result = await proxy.$api.user.getUserInfo(userId.value, () => {
    setTimeout(() => {
      router.push('/')
    }, 1500)
  })
  if (!result) {
    return
  }
  userInfo.value = result.data
}
// 切换tab
const changeTab = () => {
  loadArticle()
}
// 加载文章
const loadArticle = async () => {
  loading.value = true
  const result = await proxy.$api.user.getUserArticleList(userId.value, activeTagName.value, articleInfoList.value.pageNo)
  if (!result) {
    return
  }
  articleInfoList.value = result.data
  loading.value = false
}
// 更新用户信息
const updateUserInfo = () => {
  userInfoEditor.value.showEditUserInfoDialog(userInfo.value)
}
const showIntegralRecord = () => {
  integralRecordRef.value.showIntegralRecordDialog()
}
onMounted(() => {
  userId.value = route.params.userId
  loadUserInfo()
  loadArticle()
})
// 监听用户id变化
watch(
  () => proxy.$store.getters.userId,
  newVal => {
    currentUserId.value = newVal
  },
  {
    immediate: true,
    deep: true
  }
)
</script>

<style lang="scss" scoped>
.user-center {
  @apply py-[20px];

  .user-banner {
    @apply py-[10px] px-[20px];
  }

  .user-panel {
    @apply flex;

    .user-side {
      @apply w-[200px] h-[300px] mx-[10px];

      .avatar-panel {
        @apply bg-[var(--bg-transparency)] p-[10px] rounded-[10px];

        .edit-btn {
          @apply text-right p-[10px] pt-[0];

          span {
            @apply text-[12px] text-[#999] cursor-pointer;
            transition: color 0.3s;

            &:hover {
              @apply text-[#1890ff0];
            }
          }
        }

        .avatar {
          @apply w-[100px] h-[100px] mx-auto rounded-full overflow-hidden;

          img {
            @apply h-full w-full object-cover;
          }
        }

        .nickname {
          @apply mt-[10px] text-center text-[16px] font-bold;
        }

        .desc {
          @apply m-[5px] mr-[0] text-[12px] text-[#999];
        }
      }

      .info-panel {
        @apply mt-[10px] bg-[var(--bg-transparency)] p-[10px] rounded-[10px];

        .info-item {
          @apply flex items-center justify-between py-[5px] px-[10px];

          .label {
            @apply text-[12px] text-[#999];
          }

          .value {
            @apply text-[14px] text-[#333];
          }
        }
      }
    }

    .article-side {
      @apply flex-1 mx-[10px] p-[10px];
      .profile-panel {
        @apply bg-[var(--bg-transparency)] mb-[15px] p-[10px] rounded-[10px];

        .desc {
          @apply text-[12px] text-[#999] mb-[10px];
        }

        .echarts {
          @apply w-full bg-transparent;
        }
      }

      .tabs {
        @apply bg-[var(--bg-transparency)] mb-[10px] p-[5px] rounded-[10px];
      }
    }
  }
}
</style>
