<template>
  <div class="container">
    <div class="box">
      <h2>📋 我的订单</h2>

      <!-- 未登录时提示 -->
      <div v-if="!user" style="text-align:center; padding:40px; color:#999;">
        <p style="font-size:18px;">🔐 请先登录查看订单</p>
        <button @click="$router.push('/login')" style="margin-top:16px; padding:10px 30px;">去登录</button>
      </div>

      <div v-else>
        <p style="color:#888; margin-bottom:16px;">👤 {{ user.customerName || user.username }} 的订单</p>
        <table v-if="orders.length > 0">
          <thead>
            <tr><th>订单号</th><th>收件人</th><th>电话</th><th>地址</th><th>总金额</th><th>支付方式</th><th>状态</th><th>操作</th></tr>
          </thead>
          <tbody>
            <tr v-for="item in orders" :key="item.orderId">
              <td><b>{{ item.orderNo }}</b></td>
              <td>{{ item.receiver }}</td><td>{{ item.phone }}</td><td>{{ item.address }}</td>
              <td><b class="price">¥{{ item.totalAmount }}</b></td>
              <td>{{ item.paymentMethod }}</td>
              <td><span :class="statusClass(item.paymentStatus)">{{ statusText(item.paymentStatus) }}</span></td>
              <td>
                <button v-if="item.paymentStatus === '0'" @click="doPay(item.orderId)"
                  style="background: #f39c12; font-size:12px;">💰 付款</button>
                <button @click="showDetail(item)" style="font-size:12px;">详情</button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-else style="text-align:center; padding:30px; color:#bbb;">暂无订单</div>
      </div>

      <!-- 订单详情弹窗 -->
      <div v-if="currentOrder" class="detail-panel">
        <h3>📄 订单详情 — {{ currentOrder.orderNo }}</h3>
        <p>状态：<span :class="statusClass(currentOrder.paymentStatus)">{{ statusText(currentOrder.paymentStatus) }}</span></p>
        <table style="margin-top:12px;">
          <thead><tr><th>商品名称</th><th>厂家</th><th>数量</th><th>总金额</th></tr></thead>
          <tbody>
            <tr v-for="d in details" :key="d.detail_id">
              <td>{{ d.productName }}</td><td>{{ d.manufacturerName }}</td>
              <td>{{ d.quantity }}</td><td><b>¥{{ d.total_amount }}</b></td>
            </tr>
          </tbody>
        </table>
        <button @click="currentOrder = null" class="btn-sm" style="background:#999; margin-top:15px;">收起</button>
      </div>

      <button @click="$router.push('/home')" style="margin-top:20px; background:#999;">返回商城</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "MyOrder",
  data() { return { user: null, orders: [], currentOrder: null, details: [] }; },
  mounted() {
    const u = JSON.parse(localStorage.getItem("user"));
    if (u && u.role === "CUSTOMER") { this.user = u; this.loadOrders(); }
  },
  methods: {
    loadOrders() {
      if (!this.user || !this.user.customerId) return;
      axios.get("/api/order/customer/" + this.user.customerId).then(res => { this.orders = res.data; });
    },
    doPay(orderId) {
      if (!confirm("确认付款？")) return;
      axios.put("/api/order/status", null, { params: { orderId, status: "1" } })
        .then(() => { alert("付款成功！"); this.loadOrders(); });
    },
    showDetail(order) {
      this.currentOrder = order;
      axios.get("/api/orderDetail/order/" + order.orderId).then(res => { this.details = res.data; });
    },
    statusText(s) {
      const m = { "0": "未付款", "1": "已付款", "2": "已发货", "3": "已完成" };
      return m[s] || "未知";
    },
    statusClass(s) {
      const m = { "0": "badge badge-red", "1": "badge badge-orange", "2": "badge badge-blue", "3": "badge badge-green" };
      return m[s] || "";
    }
  }
};
</script>

<style scoped>
.price { color: #e74c3c; font-weight: 700; }
.detail-panel { margin-top: 24px; padding: 20px; background: #fafbfc; border-radius: 12px; border-left: 4px solid #4a90d9; }
</style>
