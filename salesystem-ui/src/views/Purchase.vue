<template>
  <div class="container">
    <!-- 低库存预警 -->
    <div class="box alert-box" v-if="lowStockList.length > 0">
      <h3>⚠️ 低库存预警（库存 &lt; 10）—— 请及时联系厂家进货</h3>
      <div class="alert-list">
        <div class="alert-item" v-for="p in lowStockList" :key="p.productId">
          <span class="ai-name">{{ p.productName }}</span>
          <span class="ai-stock">库存仅剩 {{ p.stock }}</span>
          <button class="btn-sm" @click="quickPurchase(p)">📥 进货</button>
        </div>
      </div>
    </div>

    <div class="box" v-if="showForm">
      <h3>📝 创建进货单</h3>
      <div class="purchase-form">
        <div class="pf-row">
          <label>商品</label>
          <select v-model="form.productId" @change="onProductChange">
            <option value="">-- 选择商品 --</option>
            <option v-for="p in allProducts" :key="p.productId" :value="p.productId">{{ p.productName }}（库存：{{ p.stock }}）</option>
          </select>
        </div>
        <div class="pf-row">
          <label>厂家</label>
          <select v-model="form.manufacturerId" @change="onMfrChange">
            <option value="">-- 选择厂家 --</option>
            <option v-for="m in manufacturers" :key="m.manufacturerId" :value="m.manufacturerId">{{ m.manufacturerName }}（{{ m.contactPerson || '无联系人' }}）</option>
          </select>
        </div>

        <!-- 选中厂家后显示联系信息 -->
        <div class="mfr-card" v-if="selectedMfr">
          <div class="mfr-info">
            <h4>🏭 {{ selectedMfr.manufacturerName }}</h4>
            <p><b>联系人：</b>{{ selectedMfr.contactPerson || '未填写' }}</p>
            <p><b>📞 电话：</b><span class="mfr-phone">{{ selectedMfr.phone || '未填写' }}</span></p>
            <p><b>📧 邮箱：</b>{{ selectedMfr.email || '未填写' }}</p>
            <p><b>📍 地址：</b>{{ selectedMfr.address || '未填写' }}</p>
          </div>
        </div>

        <div class="pf-row">
          <label>数量</label>
          <input v-model.number="form.purchaseQuantity" type="number" min="1" style="width:100px;">
        </div>
        <div class="pf-row">
          <label>备注</label>
          <input v-model="form.notes" placeholder="如：急用，请尽快发货" style="width:300px;">
        </div>
        <div class="pf-actions">
          <button @click="submitPurchase" style="padding:10px 30px;">提交进货单</button>
          <button @click="showForm = false" style="background:#999; padding:10px 30px;">取消</button>
        </div>
      </div>
    </div>

    <button @click="showForm = true" v-if="!showForm" class="btn-add">+ 新建进货单</button>

    <!-- 进货单列表 -->
    <div class="box">
      <h2>📥 进货单管理</h2>
      <table>
        <thead>
          <tr><th>单号</th><th>商品</th><th>厂家</th><th>联系人</th><th>厂家电话</th><th>数量</th><th>日期</th><th>状态</th><th>备注</th><th>操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="p in purchaseList" :key="p.purchaseId">
            <td>{{ p.purchaseId }}</td>
            <td>{{ p.productName }}</td>
            <td>{{ p.manufacturerName }}</td>
            <td>-</td>
            <td>-</td>
            <td>{{ p.purchaseQuantity }}</td>
            <td>{{ formatDate(p.purchaseDate) }}</td>
            <td><span :class="p.status==='arrived'?'badge badge-green':'badge badge-orange'">{{ p.status==='arrived'?'已到货':'待收货' }}</span></td>
            <td>{{ p.notes || '-' }}</td>
            <td>
              <button v-if="p.status==='pending'" @click="confirmArrival(p.purchaseId)" class="btn-sm" style="background:#27ae60;">✅ 确认到货</button>
              <button @click="deletePurchase(p.purchaseId)" class="btn-sm btn-danger">删除</button>
            </td>
          </tr>
          <tr v-if="purchaseList.length===0"><td colspan="10">暂无进货单</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "PurchasePage",
  data() {
    return {
      lowStockList: [], purchaseList: [], allProducts: [], manufacturers: [],
      showForm: false, selectedMfr: null,
      form: { productId: "", manufacturerId: "", purchaseQuantity: 1, notes: "" }
    };
  },
  mounted() { this.loadLowStock(); this.loadPurchases(); this.loadProducts(); this.loadManufacturers(); },
  methods: {
    loadLowStock() { axios.get("/api/product/lowStock").then(res => { this.lowStockList = res.data; }); },
    loadPurchases() { axios.get("/api/purchase/list").then(res => { this.purchaseList = res.data; }); },
    loadProducts() { axios.get("/api/product/list").then(res => { this.allProducts = res.data; }); },
    loadManufacturers() { axios.get("/api/manufacturer/list").then(res => { this.manufacturers = res.data; }); },
    quickPurchase(product) {
      this.form.productId = product.productId;
      this.form.manufacturerId = product.manufacturerId || "";
      this.form.purchaseQuantity = 10;
      this.form.notes = "低库存自动补货";
      this.showForm = true;
      this.onMfrChange();
    },
    onProductChange() {
      const p = this.allProducts.find(p => p.productId == this.form.productId);
      if (p && p.manufacturerId) this.form.manufacturerId = p.manufacturerId;
      this.onMfrChange();
    },
    onMfrChange() {
      this.selectedMfr = this.manufacturers.find(m => m.manufacturerId == this.form.manufacturerId) || null;
    },
    submitPurchase() {
      if (!this.form.productId) { alert("请选择商品"); return; }
      if (!this.form.manufacturerId) { alert("请选择厂家"); return; }
      if (!this.form.purchaseQuantity || this.form.purchaseQuantity < 1) { alert("数量至少为1"); return; }
      axios.post("/api/purchase/add", { ...this.form, status: "pending" })
        .then(() => { alert("进货单已创建！请联系厂家确认发货。"); this.showForm = false; this.resetForm(); this.loadPurchases(); this.loadLowStock(); });
    },
    confirmArrival(id) {
      if (!confirm("确认已到货？库存将自动增加。")) return;
      axios.put("/api/purchase/arrive/" + id).then(() => { alert("已确认到货，库存已更新！"); this.loadPurchases(); this.loadLowStock(); });
    },
    deletePurchase(id) { if (confirm("确定删除？")) { axios.delete("/api/purchase/delete/" + id).then(() => { this.loadPurchases(); }); } },
    resetForm() { this.form = { productId: "", manufacturerId: "", purchaseQuantity: 1, notes: "" }; this.selectedMfr = null; },
    formatDate(d) { return d ? new Date(d).toLocaleDateString("zh-CN") : ""; }
  }
};
</script>

<style scoped>
.alert-box { border-left: 5px solid #e74c3c; background: #fff5f5; }
.alert-list { display: flex; flex-wrap: wrap; gap: 10px; margin-top: 12px; }
.alert-item { background: #fde8e8; padding: 8px 16px; border-radius: 20px; display: flex; align-items: center; gap: 10px; font-size: 13px; }
.ai-name { font-weight: 600; }
.ai-stock { color: #e74c3c; font-weight: 700; }
.btn-add { padding: 10px 28px; font-size: 15px; margin-bottom: 15px; }
.purchase-form { display: flex; flex-direction: column; gap: 14px; }
.pf-row { display: flex; align-items: center; gap: 12px; }
.pf-row label { width: 70px; font-weight: 600; font-size: 14px; color: #555; }
.pf-actions { display: flex; gap: 10px; margin-top: 6px; }
.mfr-card { background: #f0faf4; border: 2px solid #27ae60; border-radius: 12px; padding: 16px 20px; margin: 4px 0; }
.mfr-card h4 { color: #1a3c2a; margin-bottom: 8px; }
.mfr-card p { margin: 4px 0; font-size: 13px; color: #555; }
.mfr-phone { font-size: 16px; font-weight: 700; color: #27ae60; }
</style>
