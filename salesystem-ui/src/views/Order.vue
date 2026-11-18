<template>
  <div class="container">
    <div class="box">
      <h2>订单管理（管理员）</h2>

      <!-- 快捷标签切换 -->
      <div style="margin: 10px 0;">
        <button @click="load" :style="tab === 'all' ? btnActive : {}">全部订单</button>
        <button @click="showPaid" :style="tab === 'paid' ? btnActive : {}">已完成订单</button>
        <button @click="showUnpaid" :style="tab === 'unpaid' ? btnActive : {}">未付款订单</button>
        <button @click="showUnshipped" :style="tab === 'unshipped' ? btnActive : {}">未发货订单</button>
        <button @click="showShortage" :style="tab === 'shortage' ? btnActive : {}">缺货订单</button>
      </div>

      <hr>

      <table border="1" width="100%" cellspacing="0">
        <tr>
          <th>订单号</th>
          <th>下单日期</th>
          <th>顾客ID</th>
          <th>收件人</th>
          <th>电话</th>
          <th>地址</th>
          <th>运费</th>
          <th>供货</th>
          <th>支付状态</th>
          <th>运输日期</th>
          <th>发票</th>
          <th>支付方式</th>
          <th>操作</th>
        </tr>

        <tr v-for="o in list" :key="o.orderId">
          <td>{{ o.orderNo }}</td>
          <td>{{ formatDate(o.orderDate) }}</td>
          <td>{{ o.customerId }}</td>
          <td>{{ o.receiver }}</td>
          <td>{{ o.phone }}</td>
          <td>{{ o.address }}</td>
          <td><b style="color:#e74c3c;">¥{{ o.totalAmount }}</b></td>
          <td>
            <span :style="{ color: o.supplyFlag === 'N' ? '#f56c6c' : '#67c23a' }">
              {{ o.supplyFlag === 'N' ? '缺货' : '可供货' }}
            </span>
          </td>
          <td>{{ formatStatus(o.paymentStatus) }}</td>
          <td>{{ formatDate(o.shippingDate) }}</td>
          <td>{{ o.invoiceFlag === 'Y' ? '需要' : '不要' }}</td>
          <td>{{ o.paymentMethod }}</td>
          <td style="min-width: 200px;">
            <!-- 付款状态操作 -->
            <button @click="changeStatus(o.orderId, '1')" v-if="o.paymentStatus === '0'">确认付款</button>
            <button @click="changeStatus(o.orderId, '3')" v-if="o.paymentStatus === '2'">确认完成</button>

            <!-- 发货操作 -->
            <button @click="doShip(o.orderId)" v-if="o.paymentStatus === '1' && o.supplyFlag === 'Y'"
              style="background-color: #67c23a;">发货</button>

            <!-- 缺货操作 -->
            <button @click="doShortage(o.orderId)" v-if="o.supplyFlag === 'Y' && o.paymentStatus !== '2' && o.paymentStatus !== '3'"
              style="background-color: #e6a23c;">标记缺货</button>
            <button @click="doRestore(o.orderId)" v-if="o.supplyFlag === 'N'"
              style="background-color: #67c23a;">恢复可供货</button>

            <br>

            <button @click="printInvoice(o)">打印发票</button>
            <button @click="printShipping(o)">邮寄单</button>
            <button @click="del(o.orderId)" style="background-color: #f56c6c;">删除</button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      list: [],
      tab: "all",
      btnActive: {
        backgroundColor: "#409eff",
        color: "#fff",
        fontWeight: "bold"
      }
    };
  },

  mounted() {
    this.load();
  },

  methods: {
    load() {
      this.tab = "all";
      axios.get("/api/order/list").then(res => { this.list = res.data; });
    },

    showPaid() {
      this.tab = "paid";
      axios.get("/api/order/report/paid").then(res => { this.list = res.data; });
    },

    showUnpaid() {
      this.tab = "unpaid";
      axios.get("/api/order/report/unpaid").then(res => { this.list = res.data; });
    },

    showUnshipped() {
      this.tab = "unshipped";
      axios.get("/api/order/report/unshipped").then(res => { this.list = res.data; });
    },

    showShortage() {
      this.tab = "shortage";
      axios.get("/api/order/report/shortage").then(res => { this.list = res.data; });
    },

    changeStatus(orderId, status) {
      axios.put("/api/order/status", null, {
        params: { orderId: orderId, status: status }
      }).then(() => {
        alert("状态已更新");
        this.refreshTab();
      });
    },

    doShip(orderId) {
      if (!confirm("确认发货？")) return;
      axios.put("/api/order/ship", null, {
        params: { orderId: orderId }
      }).then(() => {
        alert("发货成功");
        this.refreshTab();
      });
    },

    doShortage(orderId) {
      if (!confirm("标记为缺货？标记后可去进货管理页面创建进货单。")) return;
      axios.put("/api/order/shortage", null, {
        params: { orderId: orderId }
      }).then(() => {
        alert("已标记缺货");
        this.refreshTab();
      });
    },

    doRestore(orderId) {
      if (!confirm("恢复为可供货？")) return;
      axios.put("/api/order/restore", null, {
        params: { orderId: orderId }
      }).then(() => {
        alert("已恢复可供货");
        this.refreshTab();
      });
    },

    del(id) {
      if (!confirm("确定删除该订单？")) return;
      axios.delete("/api/order/delete/" + id).then(() => {
        alert("已删除");
        this.refreshTab();
      });
    },

    refreshTab() {
      const tabActions = {
        all: this.load,
        paid: this.showPaid,
        unpaid: this.showUnpaid,
        unshipped: this.showUnshipped,
        shortage: this.showShortage
      };
      (tabActions[this.tab] || this.load).call(this);
    },

    formatStatus(status) {
      switch (status) {
        case "0": return "未付款";
        case "1": return "已付款";
        case "2": return "已发货";
        case "3": return "已完成";
        default: return "未知";
      }
    },

    formatDate(date) {
      if (!date) return "";
      return new Date(date).toLocaleDateString("zh-CN");
    },

    printInvoice(o) {
      const win = window.open("");
      win.document.write(`
        <h2>销售发票</h2>
        <p>订单号：${o.orderNo}</p>
        <p>下单日期：${this.formatDate(o.orderDate)}</p>
        <p>顾客ID：${o.customerId}</p>
        <p>收件人：${o.receiver}</p>
        <p>总金额：¥${o.totalAmount}</p>
        <p>支付方式：${o.paymentMethod}</p>
        <p>供货状态：${o.supplyFlag === 'N' ? '缺货' : '可供货'}</p>
        <p>支付状态：${this.formatStatus(o.paymentStatus)}</p>
      `);
      win.print();
      win.close();
    },

    printShipping(o) {
      const win = window.open("");
      win.document.write(`
        <h2>邮寄单</h2>
        <p>订单号：${o.orderNo}</p>
        <p>收件人：${o.receiver}</p>
        <p>联系电话：${o.phone}</p>
        <p>收货地址：${o.address}</p>
        <p>运输要求：${o.shippingRequirement || '无'}</p>
        <p>货物重量：${o.totalWeight} KG</p>
      `);
      win.print();
      win.close();
    }
  }
};
</script>
