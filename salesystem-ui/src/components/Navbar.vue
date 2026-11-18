<template>
  <div class="navbar">
    <div class="nav-left">
      <div class="logo">
        <span class="logo-icon">🏀</span>
        <span class="logo-text">体育用品销售系统</span>
      </div>
      <nav class="nav-links">
        <template v-if="userRole === 'CUSTOMER'">
          <router-link to="/home">🛒 商品中心</router-link>
          <router-link to="/cart">🛍 购物车</router-link>
          <router-link to="/myOrder">📋 我的订单</router-link>
          <router-link to="/messages" class="msg-link">🔔 消息
            <span class="msg-badge" v-if="unreadMsg > 0">{{ unreadMsg > 99 ? '99+' : unreadMsg }}</span>
          </router-link>
        </template>
        <template v-if="userRole === 'ADMIN'">
          <router-link to="/admin/product">📦 商品管理</router-link>
          <router-link to="/admin/customer">👥 顾客管理</router-link>
          <router-link to="/admin/order">📝 订单管理</router-link>
          <router-link to="/admin/manufacturer">🏭 厂家管理</router-link>
          <router-link to="/admin/purchase">📥 进货管理</router-link>
        </template>
      </nav>
    </div>
    <div class="nav-right">
      <div class="user-badge" v-if="userName">
        <span class="avatar">👤</span>
        <span class="user-name">{{ userName }}</span>
        <span class="role-tag" :class="userRole === 'ADMIN' ? 'role-admin' : 'role-customer'">
          {{ userRole === 'ADMIN' ? '管理员' : '顾客' }}
        </span>
      </div>
      <button class="btn-logout" @click="logout">退出</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "Navbar",
  data() { return { userRole: "", userName: "", unreadMsg: 0, timer: null }; },
  mounted() { this.loadUser(); this.startPolling(); },
  beforeDestroy() { if (this.timer) clearInterval(this.timer); },
  watch: { $route() { this.loadUser(); } },
  methods: {
    loadUser() {
      const user = JSON.parse(localStorage.getItem("user"));
      if (user) {
        this.userRole = user.role;
        this.userName = user.customerName || user.username || "";
        if (user.role === "CUSTOMER" && user.customerId) {
          this.checkUnread(user.customerId);
        }
      }
    },
    startPolling() {
      this.timer = setInterval(() => {
        const user = JSON.parse(localStorage.getItem("user"));
        if (user && user.role === "CUSTOMER" && user.customerId) {
          this.checkUnread(user.customerId);
        }
      }, 30000);
    },
    checkUnread(customerId) {
      this.$axios.get("/api/message/unread", { params: { customerId } })
        .then(res => { this.unreadMsg = res.data.count; });
    },
    logout() {
      localStorage.removeItem("user");
      localStorage.removeItem("cart");
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #1a3c2a 0%, #1e4a33 50%, #0d3320 100%);
  padding: 0 28px;
  height: 62px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  position: sticky;
  top: 0;
  z-index: 100;
  margin-bottom: 28px;
}
.nav-left { display: flex; align-items: center; gap: 24px; }
.logo { display: flex; align-items: center; gap: 10px; }
.logo-icon { font-size: 28px; }
.logo-text { font-size: 18px; font-weight: 700; color: #fff; letter-spacing: 1px; white-space: nowrap; }
.nav-links { display: flex; gap: 2px; }
.nav-links a {
  color: rgba(255,255,255,0.75) !important;
  padding: 8px 14px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
  text-decoration: none;
  white-space: nowrap;
}
.nav-links a:hover { color: #fff !important; background: rgba(255,255,255,0.12); }
.nav-links a.router-link-exact-active,
.nav-links a.router-link-active {
  color: #fff !important;
  background: rgba(255,255,255,0.18);
  box-shadow: 0 0 12px rgba(39,174,96,0.3);
}
.nav-right { display: flex; align-items: center; gap: 16px; }
.user-badge { display: flex; align-items: center; gap: 8px; }
.avatar { font-size: 22px; }
.user-name { color: #fff; font-weight: 500; font-size: 14px; }
.role-tag { font-size: 11px; padding: 2px 10px; border-radius: 12px; font-weight: 600; }
.role-admin { background: #f39c12; color: #fff; }
.role-customer { background: #27ae60; color: #fff; }
.btn-logout {
  background: rgba(255,255,255,0.1);
  border: 1px solid rgba(255,255,255,0.2);
  color: rgba(255,255,255,0.8);
  padding: 6px 16px;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.25s;
  box-shadow: none;
}
.btn-logout:hover { background: #e74c3c; border-color: #e74c3c; color: #fff; box-shadow: 0 4px 12px rgba(231,76,60,0.4); transform: translateY(-1px); }
.msg-link { position: relative; }
.msg-badge {
  position: absolute; top: -4px; right: -2px;
  background: #e74c3c; color: #fff; font-size: 10px; font-weight: 700;
  min-width: 18px; height: 18px; border-radius: 9px;
  display: flex; align-items: center; justify-content: center;
  padding: 0 4px; box-shadow: 0 2px 6px rgba(231,76,60,0.5);
}
</style>
