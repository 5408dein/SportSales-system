import Vue from "vue";
import Router from "vue-router";

import Login from "@/views/Login.vue";

// 布局
import FrontendLayout from "@/views/FrontendLayout.vue";
import AdminLayout from "@/views/AdminLayout.vue";

// 前台
import Home from "@/views/Home.vue";
import Cart from "@/views/Cart.vue";
import Checkout from "@/views/Checkout.vue";
import MyOrder from "@/views/MyOrder.vue";
import Message from "@/views/Message.vue";

// 后台
import Product from "@/views/Product.vue";
import Customer from "@/views/Customer.vue";
import Order from "@/views/Order.vue";
import Manufacturer from "@/views/Manufacturer.vue";
import Purchase from "@/views/Purchase.vue";

Vue.use(Router);

const router = new Router({

  mode: "history",

  routes: [

    { path: "/login", component: Login },

    // 前台（顾客免登录）
    {
      path: "/",
      component: FrontendLayout,
      redirect: "/home",
      children: [
        { path: "home", component: Home },
        { path: "cart", component: Cart },
        { path: "checkout", component: Checkout },
        { path: "myOrder", component: MyOrder },
        { path: "messages", component: Message }
      ]
    },

    // 后台（管理员需登录）
    {
      path: "/admin",
      component: AdminLayout,
      redirect: "/admin/product",
      children: [
        { path: "product", component: Product },
        { path: "customer", component: Customer },
        { path: "order", component: Order },
        { path: "manufacturer", component: Manufacturer },
        { path: "purchase", component: Purchase }
      ]
    }

  ]

});

// 路由拦截：仅后台需要管理员
router.beforeEach((to, from, next) => {
  if (to.path === "/login") { next(); return; }
  if (to.path.startsWith("/admin")) {
    const user = JSON.parse(localStorage.getItem("user"));
    if (!user || user.role !== "ADMIN") { next("/login"); return; }
  }
  next();
});

export default router;
