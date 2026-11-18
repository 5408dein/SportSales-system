<template>
  <div class="home-page">
    <div class="page-header">
      <h2>🏀 体育用品商城</h2>
      <button class="cart-btn" @click="goCart">
        🛒 购物车 <span class="cart-count" v-if="cartCount > 0">{{ cartCount }}</span>
      </button>
    </div>

    <div class="product-grid">
      <div class="product-card" v-for="item in products" :key="item.productId">
        <div class="card-img" :style="{ background: getBg(item.categoryCode) }">
          <span class="img-icon">{{ getIcon(item.categoryCode) }}</span>
          <span class="img-cat">{{ getCatName(item.categoryCode) }}</span>
        </div>
        <div class="card-body">
          <h4>{{ item.productName || item.description }}</h4>
          <p class="card-desc">{{ item.description }}</p>
          <div class="card-meta">
            <span class="meta-item">🏭 {{ item.manufacturerId }}</span>
            <span class="meta-item" :class="item.stock <= 5 ? 'low-stock' : 'in-stock'">
              📦 库存 {{ item.stock }}
            </span>
          </div>
          <div class="card-bottom">
            <span class="price">¥{{ item.price }}</span>
            <button
              @click="addCart(item)"
              :disabled="item.stock <= 0"
              :class="item.stock > 0 ? 'btn-buy' : 'btn-soldout'"
            >
              {{ item.stock > 0 ? '加入购物车' : '已售罄' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Home",
  data() { return { products: [], cartCount: 0 }; },
  mounted() { this.loadProducts(); this.loadCartCount(); },
  methods: {
    loadProducts() {
      axios.get("/api/product/list").then(res => { this.products = res.data; });
    },
    addCart(product) {
      let cart = JSON.parse(localStorage.getItem("cart")) || [];
      const idx = cart.findIndex(i => i.productId === product.productId);
      if (idx !== -1) { cart[idx].quantity++; }
      else { cart.push({ productId: product.productId, description: product.description, price: product.price, quantity: 1 }); }
      localStorage.setItem("cart", JSON.stringify(cart));
      this.loadCartCount();
      alert("已加入购物车");
    },
    loadCartCount() {
      let cart = JSON.parse(localStorage.getItem("cart")) || [];
      this.cartCount = cart.reduce((s, i) => s + i.quantity, 0);
    },
    goCart() { this.$router.push("/cart"); },
    getIcon(cat) {
      const icons = { BALL: "🏀", SHOE: "👟", CLOTH: "👕", EQUIP: "🏸", BAG: "🎒" };
      return icons[cat] || "📦";
    },
    getBg(cat) {
      const bgs = {
        BALL: "linear-gradient(135deg, #ff9a56 0%, #ff6b35 100%)",
        SHOE: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        CLOTH: "linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)",
        EQUIP: "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)",
        BAG: "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)"
      };
      return bgs[cat] || "linear-gradient(135deg, #a8c0ff 0%, #3f2b96 100%)";
    },
    getCatName(cat) {
      const names = { BALL: "球类", SHOE: "鞋类", CLOTH: "服装", EQUIP: "器材", BAG: "包袋" };
      return names[cat] || "商品";
    }
  }
};
</script>

<style scoped>
.page-header {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 20px;
}
.page-header h2 { border-bottom: none; margin-bottom: 0; }
.cart-btn {
  position: relative;
  background: #fff; color: #4a90d9; border: 2px solid #4a90d9;
  padding: 10px 22px; border-radius: 30px; font-size: 15px;
  font-weight: 600; cursor: pointer; transition: all 0.25s;
  box-shadow: 0 2px 8px rgba(39,174,96,0.15);
}
.cart-btn:hover { background: #4a90d9; color: #fff; transform: translateY(-2px); }
.cart-count {
  background: #e74c3c; color: #fff; border-radius: 20px;
  padding: 2px 8px; font-size: 12px; margin-left: 4px;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(258px, 1fr));
  gap: 22px;
}
.product-card {
  background: #fff; border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.06);
  overflow: hidden; transition: all 0.3s;
}
.product-card:hover { transform: translateY(-4px); box-shadow: 0 12px 36px rgba(0,0,0,0.12); }
.card-img {
  height: 160px; display: flex; flex-direction: column; align-items: center;
  justify-content: center; gap: 8px; position: relative; overflow: hidden;
}
.card-img::after {
  content: ""; position: absolute; bottom: -20px; right: -20px;
  width: 100px; height: 100px; background: rgba(255,255,255,0.15);
  border-radius: 50%;
}
.img-icon { font-size: 52px; filter: drop-shadow(0 4px 8px rgba(0,0,0,0.2)); z-index: 1; }
.img-cat { font-size: 12px; color: rgba(255,255,255,0.85); font-weight: 600; z-index: 1; }
.card-body { padding: 18px; }
.card-body h4 { font-size: 16px; font-weight: 600; color: #222; margin-bottom: 6px; }
.card-desc { font-size: 13px; color: #888; margin-bottom: 12px; }
.card-meta { display: flex; gap: 12px; margin-bottom: 14px; flex-wrap: wrap; }
.meta-item { font-size: 12px; color: #999; }
.in-stock { color: #27ae60; font-weight: 500; }
.low-stock { color: #e74c3c; font-weight: 600; }
.card-bottom { display: flex; justify-content: space-between; align-items: center; }
.price { font-size: 22px; font-weight: 700; color: #e74c3c; }
.price::before { content: "¥"; font-size: 14px; }
.btn-buy {
  padding: 8px 18px; border-radius: 8px; font-size: 13px; font-weight: 600;
  background: linear-gradient(135deg, #4a90d9, #357abd); color: #fff;
  border: none; cursor: pointer; transition: all 0.25s;
  box-shadow: 0 2px 8px rgba(74,144,217,0.25);
}
.btn-buy:hover { box-shadow: 0 6px 20px rgba(74,144,217,0.45); transform: translateY(-1px); }
.btn-soldout { padding: 8px 18px; border-radius: 8px; font-size: 13px; background: #c8cdd4; color: #fff; border: none; cursor: not-allowed; }
</style>
