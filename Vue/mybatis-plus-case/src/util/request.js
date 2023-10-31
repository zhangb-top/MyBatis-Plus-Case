// 导入axios包 
import axios from "axios"
import { Loading } from "element-ui"

// 创建axios对象，配置请求的根路径
const instance = axios.create({
  baseURL: 'http://localhost:8888'
})

let loading = null;
// 添加请求拦截器
instance.interceptors.request.use(
  config => {
    loading = Loading.service({
      lock: true,
      text: '加载中...',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器（注意：响应拦截器也应该绑定给 instance 实例）
instance.interceptors.response.use(
  response => {
    // 隐藏 loading 效果
    loading.close();
    return response
  },
  error => {
    return Promise.reject(error)
  }
)

export default instance