<template>
  <div class="article-item">
    <div class="article-item-inner" @click="proxy.$router.push('/article/' + data.articleId)">
      <!-- TODO -->
      <div class="list-item">
        <div class="user-info">
          <div class="user">
            <user-avatar :user-id="data.userId" :src="proxy.$api.account.avatarUrl(data.userId)" />
            <router-link :to="'/user/' + data.userId">
              {{ data.nickName }}
            </router-link>
          </div>
          <div class="post-time">{{ data.postTime }}</div>
          <div class="address">{{ data.userIpAddress }}</div>
          <div class="board">
            <el-divider direction="vertical" />
            <router-link to="/">{{ data.pboardName }}</router-link>
            <template v-if="data.boardName">
              <span style="color: #999; font-size: 14px">&nbsp;&nbsp;/&nbsp;&nbsp;</span>
              <router-link to="/">{{ data.boardName }}</router-link>
            </template>
          </div>
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
          <div class="post-time">发布于：{{ data.postTime }}</div>
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
    @apply flex flex-col-reverse items-center bg-[var(--bg-transparency)] mb-[15px] rounded-[10px] border-b-[1px] border-[#eee] overflow-hidden cursor-pointer;
    @apply md:flex-row md:justify-between;
    transition: all 0.3s;

    &:hover {
      @apply bg-[var(--bg-transparency-hover)];
    }

    .list-item {
      @apply my-2 mx-4 w-full;

      .user-info {
        @apply flex mx-3 items-center;
        .user {
          @apply flex items-center justify-around w-32;
        }

        .post-time {
          @apply hidden ml-[10px] text-[var(--text-color-1)];
          @apply md:block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .address {
          @apply ml-[10px] w-16 text-[var(--text-color-1)];
        }

        .board {
          @apply w-48;
          a {
            @apply text-[var(--text-color-3)] decoration-0;
            transition: 0.3s all;

            &:hover {
              @apply text-[var(--color-primary)];
            }
          }
        }
      }

      .article-title {
        @apply text-xl font-bold m-[10px];

        a {
          @apply text-[var(--text-color)] decoration-0;
          transition: 0.3s all;

          &:hover {
            @apply text-[var(--color-primary)];
          }
        }
      }

      .article-summary {
        @apply ml-2 text-[var(--text-color-2)] text-sm;
      }

      .article-info {
        @apply mt-3 ml-2 text-[var(--text-color-1)] text-[12px];

        .iconfont {
          @apply mr-[10px] text-[14px] cursor-pointer;
          transition: 0.3s all;

          &:hover {
            @apply text-[var(--color-primary)];
          }
        }
        .post-time {
          @apply ml-2 my-1 block;
          @apply md:hidden;
        }
      }
    }

    .article-cover {
      @apply flex w-full h-auto justify-center;
      @apply md:h-full md:w-auto;
      img {
      }
    }
  }
}
</style>
