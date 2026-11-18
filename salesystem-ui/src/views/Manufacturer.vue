<template>
  <div class="container">
    <div class="box">
      <h2>🏭 厂家管理</h2>
      <table>
        <thead>
          <tr><th>ID</th><th>代码</th><th>名称</th><th>联系人</th><th>电话</th><th>邮箱</th><th>地址</th><th>操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="m in list" :key="m.manufacturerId">
            <td>{{ m.manufacturerId }}</td>
            <td><span class="badge badge-blue">{{ m.manufacturerCode }}</span></td>
            <td><b>{{ m.manufacturerName }}</b></td>
            <td>{{ m.contactPerson || '-' }}</td>
            <td>{{ m.phone || '-' }}</td>
            <td>{{ m.email || '-' }}</td>
            <td style="max-width:180px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;" :title="m.address">{{ m.address || '-' }}</td>
            <td>
              <button class="btn-sm btn-warning" @click="edit(m)">编辑</button>
              <button class="btn-sm" @click="contact(m)">📞 联系</button>
              <button class="btn-sm btn-danger" @click="del(m.manufacturerId)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>

      <h3>{{ editId ? '编辑厂家' : '新增厂家' }}</h3>
      <div class="form-grid">
        <input v-model="form.manufacturerCode" placeholder="厂家代码 *">
        <input v-model="form.manufacturerName" placeholder="厂家名称 *" style="width:180px;">
        <input v-model="form.contactPerson" placeholder="联系人">
        <input v-model="form.phone" placeholder="联系电话">
        <input v-model="form.email" placeholder="邮箱">
        <input v-model="form.address" placeholder="厂家地址" style="width:280px;">
        <button @click="save">{{ editId ? '更新' : '添加厂家' }}</button>
        <button v-if="editId" @click="resetForm" style="background:#999;">取消</button>
      </div>
    </div>

    <!-- 联系厂家弹窗 -->
    <div class="modal-overlay" v-if="showContact" @click.self="showContact = false">
      <div class="modal-box">
        <h3>📞 联系厂家 — {{ contactMfr.manufacturerName }}</h3>
        <div class="contact-info">
          <div class="ci-item"><span class="ci-label">🏢 厂家名称</span><span>{{ contactMfr.manufacturerName }}</span></div>
          <div class="ci-item"><span class="ci-label">👤 联系人</span><span>{{ contactMfr.contactPerson || '未填写' }}</span></div>
          <div class="ci-item"><span class="ci-label">📞 电话</span><span class="ci-phone">{{ contactMfr.phone || '未填写' }}</span></div>
          <div class="ci-item"><span class="ci-label">📧 邮箱</span><span>{{ contactMfr.email || '未填写' }}</span></div>
          <div class="ci-item"><span class="ci-label">📍 地址</span><span>{{ contactMfr.address || '未填写' }}</span></div>
        </div>
        <div class="ci-actions">
          <button @click="goPurchase(contactMfr)" style="background:#27ae60;">📥 创建进货单</button>
          <button @click="showContact = false" style="background:#999;">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      list: [], editId: null,
      form: { manufacturerCode: "", manufacturerName: "", contactPerson: "", phone: "", email: "", address: "" },
      showContact: false, contactMfr: {}
    };
  },
  mounted() { this.load(); },
  methods: {
    load() { axios.get("/api/manufacturer/list").then(res => { this.list = res.data; }); },
    save() {
      if (!this.form.manufacturerCode || !this.form.manufacturerName) { alert("请填写代码和名称"); return; }
      const url = this.editId ? "/api/manufacturer/update" : "/api/manufacturer/add";
      const data = this.editId ? { manufacturerId: this.editId, ...this.form } : this.form;
      axios({ method: this.editId ? "put" : "post", url, data })
        .then(() => { alert(this.editId ? "更新成功" : "添加成功"); this.load(); this.resetForm(); });
    },
    edit(m) {
      this.editId = m.manufacturerId;
      this.form = { manufacturerCode: m.manufacturerCode, manufacturerName: m.manufacturerName, contactPerson: m.contactPerson || "", phone: m.phone || "", email: m.email || "", address: m.address || "" };
    },
    del(id) { if (confirm("确定删除？")) { axios.delete("/api/manufacturer/delete/" + id).then(() => { alert("已删除"); this.load(); }); } },
    resetForm() { this.editId = null; this.form = { manufacturerCode: "", manufacturerName: "", contactPerson: "", phone: "", email: "", address: "" }; },
    contact(m) { this.contactMfr = m; this.showContact = true; },
    goPurchase(m) { this.showContact = false; this.$router.push("/admin/purchase"); }
  }
};
</script>

<style scoped>
.form-grid { display: flex; flex-wrap: wrap; gap: 8px; align-items: center; }
.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 200; }
.modal-box { background: #fff; border-radius: 16px; padding: 32px; width: 480px; max-width: 90vw; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }
.modal-box h3 { margin-top: 0; }
.contact-info { display: flex; flex-direction: column; gap: 14px; margin: 20px 0; }
.ci-item { display: flex; align-items: center; gap: 12px; padding: 10px 14px; background: #f5f8f6; border-radius: 10px; }
.ci-label { font-weight: 700; font-size: 14px; color: #666; min-width: 90px; }
.ci-phone { font-size: 18px; font-weight: 700; color: #27ae60; }
.ci-actions { display: flex; gap: 10px; margin-top: 20px; justify-content: flex-end; }
</style>
