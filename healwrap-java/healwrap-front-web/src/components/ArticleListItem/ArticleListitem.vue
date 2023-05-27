<template>
  <div class="article-item">
    <div class="article-item-inner">
      <div class="list-item">
        <div class="user-info">
          <user-avatar :user-id="data.userId" :src="accountApi.avatarUrl(data.userId)" />
          <router-link :to="'/user/' + data.userId">
            {{ data.nickName }}
          </router-link>
          <div class="post-time">{{ data.postTime }}</div>
          <div class="address">{{ data.userIpAddress }}</div>
          <el-divider direction="vertical" />
          <router-link to="/">{{ data.pBoardName }}</router-link>
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
        <el-image style="width: 100px; height: 100px" :src="filesApi.getImage(data.cover)" fit="cover" />
      </div>
    </div>
  </div>
</template>

<script setup>
import filesApi from '@/api/files'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import accountApi from '@/api/account'

defineProps({
  data: {
    type: Object
  }
})
</script>

<style lang="scss" scoped>
.article-item {
  .article-item-inner {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fff;
    padding: 5px 20px;
    transition: 0.3s all;
    border-bottom: 1px solid #eee;
    cursor: pointer;

    &:hover {
      background: #f5f5f5;
    }

    .list-item {
      padding: 10px 5px;

      .user-info {
        display: flex;
        align-items: center;

        .post-time {
          margin-left: 10px;
          color: #999;
        }

        .address {
          margin-left: 10px;
          color: #999;
        }

        a {
          color: #333;
          text-decoration: none;
          transition: 0.3s all;

          &:hover {
            color: #1e88e5;
          }
        }
      }

      .article-title {
        font-size: 18px;
        font-weight: bold;
        margin: 10px 5px;

        a {
          color: #333;
          text-decoration: none;
          transition: 0.3s all;

          &:hover {
            color: #1e88e5;
          }
        }
      }

      .article-summary {
        color: #666;
        font-size: 15px;
      }

      .article-info {
        margin-top: 10px;
        color: #999;
        font-size: 12px;

        .iconfont {
          margin-right: 10px;
          font-size: 14px;
          cursor: pointer;
          transition: 0.3s all;

          &:hover {
            color: #1e88e5;
          }
        }
      }
    }
  }
}
</style>
