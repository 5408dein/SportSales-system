<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <span class="login-icon">🏀</span>
        <h1>悦动体育商城</h1>
        <p>YueDong Sports Mall</p>
      </div>

      <!-- 标签切换 -->
      <div class="tabs">
        <button :class="['tab', tab==='customer'?'tab-active':'']" @click="tab='customer'">🛒 顾客登录</button>
        <button :class="['tab', tab==='register'?'tab-active':'']" @click="tab='register'">📝 顾客注册</button>
        <button :class="['tab', tab==='admin'?'tab-active':'']" @click="tab='admin'">🔐 管理员</button>
        <button :class="['tab', tab==='guest'?'tab-active':'']" @click="goGuest">👀 游客浏览</button>
      </div>

      <div class="login-body">
        <!-- 顾客登录 -->
        <div v-if="tab==='customer'">
          <div class="input-group"><label>顾客代码</label><input v-model="form.username" placeholder="如：C001"></div>
          <div class="input-group"><label>密码</label><input v-model="form.password" type="password" placeholder="默认密码 123456" @keyup.enter="doLogin('CUSTOMER')"></div>
          <button class="btn-login btn-cus" @click="doLogin('CUSTOMER')">顾客登录</button>
        </div>

        <!-- 顾客注册 -->
        <div v-if="tab==='register'">
          <div class="input-group"><label>顾客代码 *</label><input v-model="reg.customerCode" placeholder="如：C006"></div>
          <div class="input-group"><label>顾客名称 *</label><input v-model="reg.customerName" placeholder="如：XX体育用品店"></div>
          <div class="input-group"><label>姓名</label><input v-model="reg.name" placeholder="联系人姓名"></div>
          <div class="input-group"><label>电话</label><input v-model="reg.phone" placeholder="联系电话"></div>
          <div class="input-group"><label>地址</label><input v-model="reg.address" placeholder="收货地址"></div>
          <div class="input-group"><label>密码</label><input v-model="reg.password" type="password" placeholder="设置登录密码"></div>
          <button class="btn-login btn-reg" @click="doRegister">注 册</button>
        </div>

        <!-- 管理员登录 -->
        <div v-if="tab==='admin'">
          <div class="input-group"><label>账号</label><input v-model="form.username" placeholder="管理员账号"></div>
          <div class="input-group"><label>密码</label><input v-model="form.password" type="password" placeholder="管理员密码" @keyup.enter="doLogin('ADMIN')"></div>
          <button class="btn-login" @click="doLogin('ADMIN')">管理员登录</button>
        </div>

        <p class="tip">💡 测试顾客：C001 / 123456 | 管理员：admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      tab: "customer",
      form: { username: "", password: "" },
      reg: { customerCode: "", customerName: "", name: "", phone: "", address: "", password: "" }
    };
  },
  methods: {
    goGuest() {
      localStorage.setItem("user", JSON.stringify({ role: "GUEST" }));
      this.$router.push("/home");
    },
    doLogin(role) {
      if (!this.form.username || !this.form.password) { alert("请填写完整"); return; }
      axios.post("/api/login", { username: this.form.username, password: this.form.password, role: role })
        .then(res => {
          if (res.data.code === 200) {
            const user = res.data.user;
            user.role = res.data.role;
            localStorage.setItem("user", JSON.stringify(user));
            this.$router.push(role === "ADMIN" ? "/admin" : "/home");
          } else { alert(res.data.msg); }
        }).catch(() => alert("网络错误"));
    },
    doRegister() {
      if (!this.reg.customerCode || !this.reg.customerName) { alert("请填写顾客代码和名称"); return; }
      axios.post("/api/register", this.reg).then(res => {
        if (res.data.code === 200) {
          alert("注册成功！请登录");
          this.form.username = this.reg.customerCode;
          this.tab = "customer";
          this.reg = { customerCode: "", customerName: "", name: "", phone: "", address: "", password: "" };
        } else { alert(res.data.msg); }
      }).catch(() => alert("网络错误"));
    }
  }
};
</script>

<style scoped>
.login-page {
  min-height: 100vh; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #1a3c2a 0%, #1e4a33 40%, #0d3320 70%, #155d35 100%);
}
.login-card { background: #fff; border-radius: 20px; box-shadow: 0 24px 80px rgba(0,0,0,0.3); width: 440px; overflow: hidden; }
.login-header { background: linear-gradient(135deg, #27ae60, #219a52); padding: 28px 32px 20px; text-align: center; color: #fff; }
.login-icon { font-size: 42px; display: block; margin-bottom: 8px; }
.login-header h1 { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.login-header p { font-size: 12px; opacity: 0.85; }
.tabs { display: flex; }
.tab { flex: 1; padding: 12px 4px; border: none; background: #f0f3f8; color: #666; font-size: 13px; font-weight: 600; cursor: pointer; box-shadow: none; border-radius: 0; transition: all 0.2s; }
.tab:hover { background: #e4e8f0; transform: none; box-shadow: none; }
.tab-active { background: #fff; color: #27ae60; border-bottom: 3px solid #27ae60; }
.login-body { padding: 24px 28px; }
.input-group { margin-bottom: 14px; }
.input-group label { display: block; font-weight: 600; font-size: 13px; color: #555; margin-bottom: 5px; }
.input-group input { width: 100%; padding: 10px 14px; border: 2px solid #e0e4ea; border-radius: 8px; font-size: 14px; outline: none; background: #fafbfc; }
.input-group input:focus { border-color: #27ae60; background: #fff; }
.btn-login { width: 100%; padding: 13px; background: linear-gradient(135deg, #27ae60, #219a52); color: #fff; font-size: 16px; font-weight: 700; border: none; border-radius: 10px; cursor: pointer; box-shadow: 0 4px 16px rgba(74,144,217,0.3); margin-top: 6px; }
.btn-cus { background: linear-gradient(135deg, #27ae60, #219a52); box-shadow: 0 4px 16px rgba(39,174,96,0.3); }
.btn-reg { background: linear-gradient(135deg, #f39c12, #d68910); box-shadow: 0 4px 16px rgba(243,156,18,0.3); }
.tip { margin-top: 14px; font-size: 12px; color: #bbb; text-align: center; line-height: 1.8; }
</style>
