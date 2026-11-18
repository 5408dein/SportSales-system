<template>
  <div class="container">
    <div class="box">
      <h2>📝 订单结算</h2>
      <div class="checkout-grid">
        <div class="checkout-form">
          <h3>收货信息</h3>
          <div class="form-row"><label>顾客代码</label><input v-model="form.customerCode" placeholder="输入顾客代码自动关联（新顾客自动注册）"></div>
          <div class="form-row"><label>收件人</label><input v-model="form.receiver" placeholder="请输入收件人姓名"></div>
          <div class="form-row"><label>电话</label><input v-model="form.phone" placeholder="请输入联系电话"></div>
          <div class="form-row"><label>地址</label><input v-model="form.address" placeholder="请输入收货地址" style="width:100%"></div>
          <div class="form-row"><label>发票</label>
            <select v-model="form.invoiceFlag"><option value="Y">需要发票</option><option value="N">不需要发票</option></select>
          </div>
          <div class="form-row"><label>支付方式</label>
            <select v-model="form.paymentMethod">
              <option value="支付宝">支付宝</option><option value="微信">微信</option><option value="银行卡">银行卡</option>
            </select>
          </div>
        </div>
        <div class="checkout-summary">
          <h3>订单摘要</h3>
          <table>
            <tr><th>商品</th><th>数量</th><th>金额</th></tr>
            <tr v-for="item in cart" :key="item.productId">
              <td>{{ item.description }}</td><td>{{ item.quantity }}</td><td>¥{{ (item.price * item.quantity).toFixed(2) }}</td>
            </tr>
          </table>
          <div class="summary-total">
            <p>商品金额：<b>¥{{ totalAmount }}</b></p>
            <p>重量：<b>{{ totalWeight }} KG</b></p>
            <p>运费：<b>¥{{ freight }}</b></p>
            <p class="final-price">应付：<b>¥{{ (Number(totalAmount) + freight).toFixed(2) }}</b></p>
          </div>
        </div>
      </div>
      <button class="btn-submit" @click="submitOrder">确认下单</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Checkout",
  data() {
    return {
      cart: [],
      form: { customerCode: "", receiver: "", phone: "", address: "", invoiceFlag: "Y", paymentMethod: "支付宝" }
    };
  },
  mounted() {
    this.cart = JSON.parse(localStorage.getItem("cart")) || [];
    // 已登录顾客自动填充信息
    const user = JSON.parse(localStorage.getItem("user"));
    if (user && user.role === "CUSTOMER") {
      this.form.customerCode = user.customerCode || "";
      this.form.receiver = user.name || user.customerName || "";
      this.form.phone = user.phone || "";
      this.form.address = user.address || "";
    }
  },
  computed: {
    totalWeight() { return this.cart.reduce((s, i) => s + Number(i.quantity), 0); },
    freight() { return this.totalWeight * 5; },
    totalAmount() { return this.cart.reduce((s, i) => s + i.price * i.quantity, 0).toFixed(2); }
  },
  methods: {
    submitOrder() {
      if (!this.form.receiver || !this.form.phone || !this.form.address) { alert("请填写完整信息"); return; }
      axios.post("/api/order/create", {
        orderNo: "ORD" + Date.now(),
        customerCode: this.form.customerCode || null,
        receiver: this.form.receiver, phone: this.form.phone, address: this.form.address,
        invoiceFlag: this.form.invoiceFlag, paymentMethod: this.form.paymentMethod,
        paymentStatus: "0", supplyFlag: "Y",
        totalWeight: this.totalWeight, freight: this.freight,
        items: this.cart
      }).then(() => {
        alert("下单成功！");
        localStorage.removeItem("cart");
        this.$router.push("/myOrder");
      }).catch(err => { console.error(err); alert("下单失败"); });
    }
  }
};
</script>

<style scoped>
.checkout-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin-bottom: 24px; }
@media (max-width: 768px) { .checkout-grid { grid-template-columns: 1fr; } }
.checkout-form, .checkout-summary { background: #fafbfc; padding: 20px; border-radius: 12px; }
.form-row { margin-bottom: 14px; display: flex; align-items: center; gap: 10px; flex-wrap: wrap; }
.form-row label { width: 80px; font-weight: 600; font-size: 14px; color: #555; }
.form-row input, .form-row select { flex: 1; min-width: 150px; }
.summary-total { margin-top: 18px; padding-top: 14px; border-top: 2px dashed #e0e4ea; }
.summary-total p { margin: 6px 0; font-size: 15px; }
.final-price { font-size: 22px !important; color: #e74c3c; }
.btn-submit {
  width: 100%; padding: 16px; font-size: 18px; font-weight: 700;
  background: linear-gradient(135deg, #27ae60, #219a52); color: #fff;
  border: none; border-radius: 12px; cursor: pointer;
  box-shadow: 0 4px 16px rgba(39,174,96,0.3); transition: all 0.25s;
}
.btn-submit:hover { box-shadow: 0 8px 24px rgba(39,174,96,0.5); transform: translateY(-2px); }
</style>
