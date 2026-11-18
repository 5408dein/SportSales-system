<template>
  <div class="container">
  <div class="box">
    <h2>顾客信息管理</h2>

    <button @click="loadFromOrders">
      从订单提取顾客
    </button>

    <button @click="loadData">
      加载全部顾客
    </button>

    <br><br>

    <table border="1" cellpadding="5">
      <tr>
        <th>代码</th>
        <th>顾客名称</th>
        <th>姓名</th>
        <th>地址</th>
        <th>邮编</th>
        <th>电话</th>
        <th>操作</th>
      </tr>

      <tr v-for="c in list" :key="c.customerId">
        <td>{{ c.customerCode }}</td>
        <td>{{ c.customerName }}</td>
        <td>{{ c.name }}</td>
        <td>{{ c.address }}</td>
        <td>{{ c.postcode }}</td>
        <td>{{ c.phone }}</td>
        <td>
          <button @click="editCustomer(c)">编辑</button>
          <button @click="deleteCustomer(c.customerId)">删除</button>
        </td>
      </tr>
    </table>

    <h3>新增/编辑顾客</h3>

    <div>
      <input v-model="form.customerCode" placeholder="顾客代码" />
      <input v-model="form.customerName" placeholder="顾客名称" />
      <input v-model="form.name" placeholder="姓名" />
      <input v-model="form.address" placeholder="地址" />
      <input v-model="form.postcode" placeholder="邮编" />
      <input v-model="form.phone" placeholder="电话" />

      <button @click="saveCustomer">保存</button>
    </div>

   </div>
</div>
</template>

<script>
export default {
  name: "CustomerPage",

  data() {
    return {
      list: [],
      form: {
        customerId: null,
        customerCode: "",
        customerName: "",
        name: "",
        address: "",
        postcode: "",
        phone: ""
      }
    };
  },

  created() {
    this.loadData();
  },

  methods: {

    loadData() {
      this.$axios
        .get("/api/customer/list")
        .then(res => {
          this.list = res.data;
        })
        .catch(err => {
          console.log(err);
          alert("加载顾客列表失败");
        });
    },

    loadFromOrders() {
      this.$axios
        .get("/api/customer/fromOrders")
        .then(res => {
          this.list = res.data;
        })
        .catch(err => {
          console.log(err);
          alert("获取订单客户失败");
        });
    },

    saveCustomer() {
      if (!this.form.customerCode || !this.form.customerName) {
        alert("请填写顾客代码和顾客名称");
        return;
      }
      if (this.form.customerId) {
        this.$axios
          .put("/api/customer/update", this.form)
          .then(() => {
            alert("更新成功");
            this.loadData();
            this.resetForm();
          })
          .catch(err => {
            console.log(err);
            alert("更新失败");
          });
      } else {
        this.$axios
          .post("/api/customer/add", this.form)
          .then(() => {
            alert("添加成功");
            this.loadData();
            this.resetForm();
          })
          .catch(err => {
            console.log(err);
            alert("添加失败");
          });
      }
    },

    editCustomer(c) {
      this.form = {
        customerId: c.customerId,
        customerCode: c.customerCode,
        customerName: c.customerName,
        name: c.name || "",
        address: c.address || "",
        postcode: c.postcode || "",
        phone: c.phone || ""
      };
    },

    deleteCustomer(id) {
      if (!confirm("确定删除该顾客吗？")) return;
      this.$axios
        .delete("/api/customer/delete/" + id)
        .then(() => {
          alert("删除成功");
          this.loadData();
        })
        .catch(err => {
          console.log(err);
          alert("删除失败");
        });
    },

    resetForm() {
      this.form = {
        customerId: null,
        customerCode: "",
        customerName: "",
        name: "",
        address: "",
        postcode: "",
        phone: ""
      };
    }
  }
};
</script>
