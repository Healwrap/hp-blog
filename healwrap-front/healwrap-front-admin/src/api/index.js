/**
 * api接口的统一出口
 */
import account from './account'
import user from './user'
import forum from './forum'
import files from './files'
import board from './board'
import comment from './comment'
import setting from './setting'

export default {
  account,
  user,
  forum,
  files,
  board,
  comment,
  setting
}
