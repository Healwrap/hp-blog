<template>
  <div class="message-center">
    <div class="top-banner">
      <!--顶部面板屑-->
      <div class="message-banner">
        <el-breadcrumb :separator-icon="ArrowRight">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>消息中心</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <router-link class="link nav-link" :to="`/user/${currentUserId}`">
        <el-icon class="icon">
          <ArrowLeft />
        </el-icon>
        <span>返回个人中心</span>
      </router-link>
    </div>
    <div class="message-panel">
      <el-tabs class="tabs-panel" :model-value="activeTagName" @tab-change="tabChange">
        <el-tab-pane :name="0">
          <template #label>
            <div class="tab-item">
              <span>系统消息</span>
              <span class="tag" v-if="messageCount.sys > 0">
                {{ messageCount.sys <= 99 ? messageCount.sys : '99+' }}
              </span>
            </div>
          </template>
        </el-tab-pane>
        <el-tab-pane :name="1">
          <template #label>
            <div class="tab-item">
              <span>回复我的</span>
              <span class="tag" v-if="messageCount.reply > 0">
                {{ messageCount.reply <= 99 ? messageCount.reply : '99+' }}
              </span>
            </div>
          </template>
        </el-tab-pane>
        <el-tab-pane :name="2">
          <template #label>
            <div class="tab-item">
              <span>赞了我的文章</span>
              <span class="tag" v-if="messageCount.likePost > 0">
                {{ messageCount.likePost <= 99 ? messageCount.likePost : '99+' }}
              </span>
            </div>
          </template>
        </el-tab-pane>
        <el-tab-pane :name="3">
          <template #label>
            <div class="tab-item">
              <span>赞了我的评论</span>
              <span class="tag" v-if="messageCount.likeComment > 0">
                {{ messageCount.likeComment <= 99 ? messageCount.likeComment : '99+' }}
              </span>
            </div>
          </template>
        </el-tab-pane>
        <el-tab-pane :name="4">
          <template #label>
            <div class="tab-item">
              <span>下载了我的附件</span>
              <span class="tag" v-if="messageCount.attachmentDownload > 0">
                {{ messageCount.attachmentDownload <= 99 ? messageCount.attachmentDownload : '99+' }}
              </span>
            </div>
          </template>
        </el-tab-pane>
      </el-tabs>
      <div class="message-list">
        <data-list :loading="loading" :data-source="messageListInfo" ikey="messageId" :rows="7" desc="暂无消息" @load-data="loadMessage">
          <template #default="{ data }">
            <!--系统消息-->
            <div v-if="data.messageType === 0" class="message-item">
              <div class="message-content">
                <span v-html="data.messageContent"></span>
                <span class="create-time">{{ data.createTime }}</span>
              </div>
            </div>
            <!--回复我的-->
            <div v-if="data.messageType === 1" class="message-item">
              <user-avatar :user="data.sendUserId" />
              <div class="message-content">
                <div>
                  <router-link class="link" :to="`/user/${data.sendUserId}`"
                    >{{ currentUserId === data.sendUserId ? '我' : `@${data.sendNickName}` }}
                  </router-link>
                  <span>对我的文章</span>
                  <span>【</span>
                  <router-link class="link" :to="`/article/${data.articleId}`">{{ data.articleTitle }}</router-link>
                  <span>】</span>
                  <span>发表了评论</span>
                  <span class="create-time">{{ data.createTime }}</span>
                </div>
                <div class="reply-content" v-html="data.messageContent"></div>
              </div>
            </div>
            <!--赞了我的文章-->
            <div v-if="data.messageType === 2" class="message-item">
              <user-avatar :user="data.sendUserId" />
              <div class="message-content">
                <div>
                  <router-link class="link" :to="`/user/${data.sendUserId}`"
                    >{{ currentUserId === data.sendUserId ? '我' : `@${data.sendNickName}` }}
                  </router-link>
                  <span>赞了我的文章</span>
                  <span>【</span>
                  <router-link class="link" :to="`/article/${data.articleId}`">{{ data.articleTitle }}</router-link>
                  <span>】</span>
                  <span class="create-time">{{ data.createTime }}</span>
                </div>
              </div>
            </div>
            <!--赞了我的评论-->
            <div v-if="data.messageType === 3" class="message-item">
              <user-avatar :user="data.sendUserId" />
              <div class="message-content">
                <div>
                  <router-link class="link" :to="`/user/${data.sendUserId}`"
                    >{{ currentUserId === data.sendUserId ? '我' : `@${data.sendNickName}` }}
                  </router-link>
                  <span>在</span>
                  <span>【</span>
                  <router-link class="link" :to="`/article/${data.articleId}`">{{ data.articleTitle }}</router-link>
                  <span>】</span>
                  <span>中赞了我的评论</span>
                  <span class="create-time">{{ data.createTime }}</span>
                </div>
              </div>
            </div>
            <!--下载了我的附件-->
            <div v-if="data.messageType === 4" class="message-item">
              <user-avatar :user="data.sendUserId" />
              <div class="message-content">
                <div>
                  <router-link class="link" :to="`/user/${data.sendUserId}`"
                    >{{ currentUserId === data.sendUserId ? '我' : `@${data.sendNickName}` }}
                  </router-link>
                  <span>在</span>
                  <span>【</span>
                  <router-link class="link" :to="`/article/${data.articleId}`">{{ data.articleTitle }}</router-link>
                  <span>】</span>
                  <span>中下载了我的附件</span>
                  <span class="create-time">{{ data.createTime }}</span>
                </div>
              </div>
            </div>
          </template>
        </data-list>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, getCurrentInstance, ref, watch } from 'vue'
import DataList from '@/components/DataList/DataList.vue'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'

const { proxy } = getCurrentInstance()
const loading = ref(true)
const types = ['system', 'reply', 'articleLike', 'commentLike', 'attachmentDownload']
const activeTagName = ref()
const messageCount = ref({})
const messageListInfo = ref({})
// tab切换
const tabChange = type => {
  proxy.$router.push(`/user/message/${types[type]}`)
}
// 加载消息列表
const loadMessage = async () => {
  loading.value = true
  const result = await proxy.$api.user.getMessageList(types[activeTagName.value], messageListInfo.value.pageNo)
  if (!result) {
    return
  }
  messageListInfo.value = result.data
  loading.value = false
}
const currentUserId = computed(() => {
  return proxy.$store.getters.userId
})
// 监听路由变化
watch(
  () => proxy.$route.params.type,
  newVal => {
    console.log(newVal)
    activeTagName.value = types.indexOf(newVal)
    if (activeTagName.value === -1) {
      return
    }
    loadMessage()
  },
  { immediate: true, deep: true }
)
// 监听消息数量
watch(
  () => proxy.$store.getters.messageCount,
  newVal => {
    messageCount.value = newVal || {}
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss" scoped>
.message-center {
  @apply py-[20px];

  .top-banner {
    @apply flex justify-between items-center;

    .user-banner {
      @apply py-[10px] px-[20px];
    }

    .nav-link {
      @apply flex items-center mt-[10px];

      .icon {
        @apply mr-[5px];
      }
    }
  }

  .message-panel {
    @apply my-[10px] py-[10px] px-[5px] rounded-[10px] bg-[var(--bg-transparency)] shadow-2xl;

    .tabs-panel {
      @apply my-[5px] mx-[10px];

      .tag {
        @apply relative inline-block top-[-5px] min-w-[16px] min-h-[16px] ml-[5px] leading-[16px] rounded-full bg-[#ff4d4f] text-[#fff] text-[12px] text-center;
      }
    }

    .message-list {
      @apply min-h-[340px];

      .message-item {
        @apply flex justify-start items-center py-[10px] pb-[20px];
        border-bottom: 1px solid #ddd;

        .message-content {
          @apply w-full ml-[10px] text-[14px];

          .create-time {
            @apply ml-[20px] text-[#999];
          }

          .reply-content {
            @apply mt-[5px] py-[5px] px-[10px] bg-[#f5f5f580];
            border-left: 2px solid var(--color-primary);
          }
        }
      }
    }
  }
}
</style>
