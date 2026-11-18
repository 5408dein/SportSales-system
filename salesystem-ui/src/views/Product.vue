<template>
  <div class="container">
    <!-- 低库存预警卡片 -->
    <div class="box alert-box" v-if="lowStockList.length > 0">
      <h3>⚠️ 低库存预警（库存 &lt; 10）—— 请及时进货</h3>
      <div class="alert-list">
        <span class="alert-item" v-for="item in lowStockList" :key="item.productId">
          {{ item.productName }} <b class="text-red">({{ item.stock }})</b>
        </span>
      </div>
    </div>

    <div class="box">
      <h2>商品库存管理</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th><th>编号</th><th>分类</th><th>名称</th>
            <th>厂家</th><th>说明</th><th>单价</th><th>库存</th>
            <th>状态</th><th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in products" :key="item.productId">
            <td>{{ item.productId }}</td>
            <td>{{ item.productCode }}</td>
            <td><span class="badge badge-blue">{{ item.categoryCode }}</span></td>
            <td><b>{{ item.productName }}</b></td>
            <td>{{ item.manufacturerId }}</td>
            <td>{{ item.description }}</td>
            <td><span class="price">¥{{ item.price }}</span></td>
            <td>
              <input v-model.number="item.stock" type="number" min="0"
                style="width:65px; text-align:center;"
                :style="{ borderColor: item.stock < 10 ? '#e74c3c' : '#e0e4ea' }">
            </td>
            <td>
              <span :class="item.status === 'ON' ? 'badge badge-green' : 'badge badge-red'">
                {{ item.status === 'ON' ? '已上架' : '已下架' }}
              </span>
            </td>
            <td>
              <button :class="item.status === 'ON' ? 'btn-warning' : 'btn-success'"
                @click="toggle(item)" style="font-size:12px; padding:5px 12px;">
                {{ item.status === 'ON' ? '下架' : '上架' }}
              </button>
              <button class="btn-sm" @click="saveStock(item)">保存库存</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() { return { products: [], lowStockList: [] }; },
  mounted() { this.load(); },
  methods: {
    load() { this.loadProducts(); this.loadLowStock(); },
    loadProducts() {
      axios.get("/api/product/list").then(res => { this.products = res.data; });
    },
    loadLowStock() {
      axios.get("/api/product/lowStock").then(res => { this.lowStockList = res.data; });
    },
    toggle(item) {
      const status = item.status === "ON" ? "DOWN" : "ON";
      axios.put("/api/product/status", { productId: item.productId, status: status })
        .then(() => { alert("状态已更新"); this.load(); });
    },
    saveStock(item) {
      axios.put("/api/product/stock", { productId: item.productId, stock: item.stock })
        .then(() => { alert("库存已更新"); this.load(); });
    }
  }
};
</script>

<style scoped>
.alert-box { border-left: 5px solid #e74c3c; background: #fff5f5; }
.alert-list { display: flex; flex-wrap: wrap; gap: 10px; margin-top: 12px; }
.alert-item {
  background: #fde8e8; padding: 6px 14px; border-radius: 20px;
  font-size: 13px; color: #c0392b;
}
.price { color: #e74c3c; font-weight: 700; }
</style>
