<template>
  <Dialog
    :show="dialogConfig.show"
    :title="dialogConfig.title"
    :width="dialogConfig.width"
    :buttons="dialogConfig.buttons"
    :show-close="dialogConfig.showClose"
    :draggable="dialogConfig.draggable"
    @close="close"
  >
    <el-form ref="formDataRef" :model="formData" :rules="rules">
      <el-form-item label="标题" prop="title">
        {{ formData.title }}
      </el-form-item>
      <el-form-item label="板块" prop="boardIds">
        <el-cascader v-model="formData.boardIds" :options="boardList" :props="boardProps" placeholder="请选择板块" clearable />
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script setup>
import { getCurrentInstance, nextTick, reactive, ref } from 'vue'

const { proxy } = getCurrentInstance()
const formDataRef = ref(null)
const boardList = ref([])
const boardProps = {
  multiple: false,
  checkStrictly: true,
  value: 'boardId',
  label: 'boardName'
}
const formData = ref({})
const rules = ref({
  boardIds: [{ required: true, message: '请选择板块', trigger: 'change' }]
})
const dialogConfig = reactive({
  show: false,
  title: '修改板块',
  showClose: false,
  width: '28%',
  draggable: true,
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: () => {
        submitForm()
      }
    }
  ]
})
// 加载板块列表
const loadBoardList = async () => {
  const result = await proxy.$api.board.loadBoard()
  if (!result) {
    return
  }
  boardList.value = result.data
}
loadBoardList()
const emit = defineEmits(['update'])
// 提交表单
const submitForm = () => {
  let params = {
    articleId: formData.value.articleId
  }

  if (formData.value.boardIds) {
    if (formData.value.boardIds.length === 1) {
      params.pboardId = formData.value.boardIds[0]
    } else if (formData.value.boardIds.length === 2) {
      params.pboardId = formData.value.boardIds[0]
      params.boardId = formData.value.boardIds[1]
    }
  }
  delete params.boardIds
  formDataRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    const result = await proxy.$api.forum.updateBoard(params)
    console.log(result)
    if (!result) {
      return
    }
    dialogConfig.show = false
    proxy.$message.success('修改成功')
    emit('update')
  })
}
// 显示与关闭弹窗
const close = () => {
  dialogConfig.show = false
}
const show = data => {
  dialogConfig.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    data.boardIds = []
    data.boardIds.push(data.pboardId)
    if (data.boardId !== 0) {
      data.boardIds.push(data.boardId)
    }
    formData.value = data
  })
}
defineExpose({
  show
})
</script>

<style scoped></style>
