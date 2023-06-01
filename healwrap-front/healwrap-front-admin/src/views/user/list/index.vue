<template>
  <div class="user-list-panel">
    <div class="search-form">
      <el-form :model="searchFormData" label-width="50px">
        <el-row>
          <el-col :span="4">
            <el-form-item label="内容" prop="contentFuzzy">
              <el-input v-model="searchFormData.contentFuzzy" placeholder="输入内容" clearable @change="loadDataList" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="昵称" prop="nickNameFuzzy">
              <el-input v-model="searchFormData.nickNameFuzzy" placeholder="请输入昵称" clearable @change="loadDataList" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="状态" prop="status">
              <el-select v-model="searchFormData.status" placeholder="请选择状态" clearable @change="loadDataList">
                <el-option label="已禁用" :value="-1" />
                <el-option label="正常" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="data-list">
      <Table
        ref="tableRef"
        class="table"
        :loading="tableLoading"
        :columns="columns"
        :data-source="tableData"
        :fetch="loadDataList"
        :options="tableOptions"
        @row-selected="setRowSelected"
      >
        <!--头像-->
        <template #avatar="{ row }">
          <user-avatar :user-id="row.userId" :width="50" />
        </template>
        <!--名称-->
        <template #nickName="{ row }">
          <user-link :username="row.nickName" :user-id="row.userId" />
          <span v-if="row.sex">{{ row.sex === 0 ? '女' : '男' }}</span>
        </template>
        <!--登录信息-->
        <template #loginInfo="{ row }">
          <div>最后登录时间：<br />&nbsp;&nbsp;-{{ row.lastLoginTime }}</div>
          <div>最后登录IP：{{ row.lastLoginIp }}</div>
          <div>最后登录IP地址：{{ row.lastLoginIpAddress }}</div>
        </template>
        <!--积分-->
        <template #integral="{ row }">
          <div>总积分：{{ row.totalIntegral }}</div>
          <div>当前积分：{{ row.currentIntegral }}</div>
        </template>
        <!--状态-->
        <template #status="{ row }">
          <el-tag v-if="row.status === 0" type="info">已禁用</el-tag>
          <el-tag v-if="row.status === 1">正常</el-tag>
        </template>
        <!--操作-->
        <template #options="{ row }">
          <div class="options" v-if="row.status !== -1">
            <el-dropdown trigger="click">
              <icon icon="More" style="cursor: pointer" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="updateUserStatus(row)">
                    {{ row.status === 1 ? '禁用' : '启用' }}
                  </el-dropdown-item>
                  <el-dropdown-item @click="sendMessage(row)">发送系统消息</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </Table>
    </div>
    <SendMessage ref="sendMessageRef" />
  </div>
</template>

<script setup>
import { getCurrentInstance, ref } from 'vue'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import UserLink from '@/components/UserLink/UserLink.vue'
import Icon from '@/components/Icon/Icon.vue'
import SendMessage from '@/views/user/list/components/SendMessage.vue'

const { proxy } = getCurrentInstance()
const sendMessageRef = ref(null)
const searchFormData = ref({})
const tableLoading = ref(true)
const columns = [
  {
    label: '头像',
    prop: 'avatar',
    width: 80,
    scopedSlots: 'avatar'
  },
  {
    label: '名称',
    prop: 'nickName',
    width: 100,
    scopedSlots: 'nickName'
  },
  {
    label: '邮箱',
    prop: 'email',
    width: 140
  },
  {
    label: '个人描述',
    prop: 'personDescription'
  },
  {
    label: '加入时间',
    prop: 'joinTime',
    width: 160
  },
  {
    label: '登录信息',
    prop: 'loginInfo',
    width: 220,
    scopedSlots: 'loginInfo'
  },
  {
    label: '积分',
    prop: 'integral',
    width: 120,
    scopedSlots: 'integral'
  },
  {
    label: '状态',
    prop: 'status',
    width: 60,
    scopedSlots: 'status'
  },
  {
    label: '操作',
    prop: 'options',
    width: 60,
    scopedSlots: 'options'
  }
]
const tableData = ref([])
const tableOptions = {}

const loadDataList = async () => {
  tableLoading.value = true
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize
  }
  Object.assign(params, searchFormData.value)
  const result = await proxy.$api.user.loadUserList(params)
  if (!result) {
    return
  }
  tableData.value = result.data
  tableLoading.value = false
}
const setRowSelected = () => {}
// 禁用用户
const updateUserStatus = row => {
  const title = row.status === 1 ? '禁用' : '启用'
  proxy.$confirm(`确定要${title}用户${row.nickName}`, async () => {
    const result = await proxy.$api.user.updateUserStatus(row.userId, row.status === 1 ? 0 : 1)
    if (!result) {
      return
    }
    loadDataList()
  })
}
// 发送系统消息
const sendMessage = row => {
  sendMessageRef.value.show(row)
}
</script>

<style lang="scss" scoped>
.user-list-panel {
  @apply h-[calc(100%-100px)] pt-[10px] px-6;
  .data-list {
    @apply h-full;
  }
}
</style>
