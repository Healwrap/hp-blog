<template>
  <div class="article-item">
    <div class="article-item-inner" @click="proxy.$router.push('/article/' + data.articleId)">
      <!-- TODO -->
      <div class="list-item">
        <div class="user-info">
          <user-avatar :user-id="data.userId" :src="proxy.$api.account.avatarUrl(data.userId)" />
          <router-link :to="'/user/' + data.userId">
            {{ data.nickName }}
          </router-link>
          <div class="post-time">{{ data.postTime }}</div>
          <div class="address">{{ data.userIpAddress }}</div>
          <el-divider direction="vertical" />
          <router-link to="/">{{ data.pboardName }}</router-link>
          <template v-if="data.boardName">
            <span style="color: #999; font-size: 14px">&nbsp;&nbsp;/&nbsp;&nbsp;</span>
            <router-link to="/">{{ data.boardName }}</router-link>
          </template>
        </div>
        <div class="article-title">
          <router-link :to="'/article/' + data.articleId">
            <el-tag v-if="data.topType === 1" dark>置顶</el-tag>
            {{ data.title }}
          </router-link>
        </div>
        <div class="article-summary">
          {{ data.summary }}
        </div>
        <div class="article-info">
          <span class="iconfont icon-eye-solid">
            {{ data.readCount === 0 ? '阅读' : data.readCount }}
          </span>
          <span class="iconfont icon-good">
            {{ data.goodCount === 0 ? '点赞' : data.goodCount }}
          </span>
          <span class="iconfont icon-comment">
            {{ data.commentCount === 0 ? '评论' : data.commentCount }}
          </span>
        </div>
      </div>
      <div v-if="data.cover" class="article-cover">
        <Image :src="proxy.$api.files.getImage(data.cover)" width="200px" fit="cover" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, getCurrentInstance } from 'vue'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import Image from '@/components/Image/Image.vue'

const { proxy } = getCurrentInstance()
defineProps({
  data: {
    type: Object
  }
})
</script>

<style lang="scss" scoped>
.article-item {
  .article-item-inner {
    @apply flex justify-between items-center bg-[var(--bg-transparency)] mb-[15px] rounded-[10px] border-b-[1px] border-[#eee] overflow-hidden cursor-pointer;
    transition: all 0.3s;

    &:hover {
      @apply bg-[var(--bg-transparency-hover)];
    }

    .list-item {
      @apply m-[10px];

      .user-info {
        @apply flex items-center;

        .post-time {
          @apply ml-[10px] text-[var(--text-color-1)];
        }

        .address {
          @apply ml-[10px] text-[var(--text-color-1)];
        }

        a {
          @apply text-[var(--text-color-3)] decoration-0;
          transition: 0.3s all;

          &:hover {
            @apply text-[var(--color-primary)];
          }
        }
      }

      .article-title {
        @apply text-[22px] font-bold m-[10px];

        a {
          @apply text-[var(--text-color)] decoration-0;
          transition: 0.3s all;

          &:hover {
            @apply text-[var(--color-primary)];
          }
        }
      }

      .article-summary {
        @apply text-[var(--text-color-2)] text-[15px];
      }

      .article-info {
        @apply mt-[10px] text-[var(--text-color-1)] text-[12px];

        .iconfont {
          @apply mr-[10px] text-[14px] cursor-pointer;
          transition: 0.3s all;

          &:hover {
            @apply text-[var(--color-primary)];
          }
        }
      }
    }

    .article-cover {
      @apply h-full;
    }
  }
}
</style>
