import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import axios from "axios";
import './assets/css/global.css'

Vue.config.productionTip = false;

Vue.prototype.$axios = axios;

axios.defaults.baseURL = "/";

// 全局请求拦截器
axios.interceptors.request.use(
  config => {
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 全局响应拦截器
axios.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    console.error("请求出错：", error);
    if (error.response) {
      const status = error.response.status;
      if (status === 401) {
        localStorage.removeItem("user");
        router.push("/login");
        alert("登录已过期，请重新登录");
      } else if (status >= 500) {
        alert("服务器错误，请稍后重试");
      }
    } else {
      alert("网络连接失败，请检查网络");
    }
    return Promise.reject(error);
  }
);

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
