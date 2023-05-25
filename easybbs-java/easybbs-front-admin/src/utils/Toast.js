import { ElMessage } from "element-plus";

const showToast = (msg, callback, type) => {
  ElMessage({
    type: type || "normal",
    message: msg,
    duration: 2000,
    onClose: callback,
  });
};

const Toast = {
  error: (msg, callback) => {
    showToast(msg, callback, "error");
  },
  success: (msg, callback) => {
    showToast(msg, callback, "success");
  },
  warning: (msg, callback) => {
    showToast(msg, callback, "warning");
  },
};

export default Toast;
