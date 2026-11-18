<template>
  <div class="container">
    <div class="box">
      <h2>🛒 购物车</h2>
      <table v-if="cart.length > 0">
        <thead>
          <tr><th>商品</th><th>单价</th><th>数量</th><th>小计</th><th>操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="item in cart" :key="item.productId">
            <td>{{ item.description }}</td>
            <td>¥{{ item.price }}</td>
            <td>
              <button class="btn-sm" @click="minus(item)" :disabled="item.quantity <= 1">−</button>
              <input type="number"
                     class="qty-input"
                     v-model.number="item.quantity"
                     @change="onQtyChange(item)"
                     min="1"
                     style="width:60px; text-align:center; margin:0 6px; padding:4px; border:1px solid #ddd; border-radius:4px;" />
              <button class="btn-sm" @click="plus(item)">+</button>
            </td>
            <td><b>¥{{ (item.price * item.quantity).toFixed(2) }}</b></td>
            <td><button class="btn-sm btn-danger" @click="remove(item.productId)">删除</button></td>
          </tr>
        </tbody>
      </table>
      <div v-else style="text-align:center; padding:50px; color:#bbb; font-size:18px;">
        🛒 购物车空空如也，去商城逛逛吧～
      </div>
      <div v-if="cart.length > 0" style="text-align:right; margin-top:24px; padding-top:20px; border-top:2px dashed #e0e4ea;">
        <p style="font-size:22px;">合计：<b style="color:#e74c3c;">¥{{ totalAmount.toFixed(2) }}</b></p>
        <div style="margin-top:14px;">
          <button @click="continueShopping" style="background:#999; padding:12px 32px; font-size:15px;">继续购物</button>
          <button @click="checkout" style="padding:12px 40px; font-size:15px; margin-left:12px;">去结算</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Cart",
  data() { return { cart: [] }; },
  mounted() { this.loadCart(); },
  computed: {
    totalAmount() { return this.cart.reduce((s, i) => s + i.price * i.quantity, 0); }
  },
  methods: {
    loadCart() { this.cart = JSON.parse(localStorage.getItem("cart")) || []; },
    saveCart() { localStorage.setItem("cart", JSON.stringify(this.cart)); },
    plus(item) { item.quantity++; this.saveCart(); },
    minus(item) { if (item.quantity > 1) { item.quantity--; this.saveCart(); } },
    onQtyChange(item) {
      if (!Number.isInteger(item.quantity) || item.quantity < 1) {
        item.quantity = 1;
      }
      this.saveCart();
    },
    remove(id) { this.cart = this.cart.filter(i => i.productId !== id); this.saveCart(); },
    continueShopping() { this.$router.push("/home"); },
    checkout() {
      if (this.cart.length === 0) { alert("购物车为空"); return; }
      this.$router.push("/checkout");
    }
  }
};
</script>
