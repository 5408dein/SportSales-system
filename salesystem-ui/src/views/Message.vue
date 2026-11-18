<template>
  <div class="container">
    <div class="box">
      <div class="msg-header">
        <h2>🔔 我的消息</h2>
        <button v-if="unreadCount > 0" @click="markAllRead" class="btn-sm" style="background:#999;">全部标为已读</button>
      </div>

      <div v-if="messages.length === 0" style="text-align:center; padding:50px; color:#bbb; font-size:16px;">
        📭 暂无消息
      </div>

      <div class="msg-list" v-else>
        <div v-for="m in messages" :key="m.messageId"
          :class="['msg-item', m.isRead === 0 ? 'unread' : '', 'type-' + m.msgType]"
          @click="readMsg(m)">
          <div class="msg-icon">{{ typeIcon(m.msgType) }}</div>
          <div class="msg-body">
            <div class="msg-top">
              <span class="msg-title">{{ m.title }}</span>
              <span v-if="m.isRead === 0" class="dot"></span>
            </div>
            <p class="msg-content">{{ m.content }}</p>
            <div class="msg-meta">
              <span v-if="m.orderNo" class="badge badge-blue">订单：{{ m.orderNo }}</span>
              <span class="msg-time">{{ formatTime(m.createTime) }}</span>
            </div>
          </div>
        </div>
      </div>

      <button @click="$router.push('/home')" style="margin-top:20px; background:#999;">返回商城</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "MessagePage",
  data() {
    return { messages: [], unreadCount: 0, user: null, timer: null };
  },
  mounted() {
    const u = JSON.parse(localStorage.getItem("user"));
    if (u && u.role === "CUSTOMER" && u.customerId) {
      this.user = u;
      this.loadMessages();
      this.loadUnread();
      // 每30秒轮询未读消息数
      this.timer = setInterval(() => { this.loadUnread(); }, 30000);
    }
  },
  beforeDestroy() { if (this.timer) clearInterval(this.timer); },
  methods: {
    loadMessages() {
      axios.get("/api/message/list", { params: { customerId: this.user.customerId } })
        .then(res => { this.messages = res.data; });
    },
    loadUnread() {
      axios.get("/api/message/unread", { params: { customerId: this.user.customerId } })
        .then(res => {
          this.unreadCount = res.data.count;
          localStorage.setItem("unreadMsg", res.data.count);
        });
    },
    readMsg(m) {
      if (m.isRead === 0) {
        axios.put("/api/message/read/" + m.messageId).then(() => { m.isRead = 1; this.loadUnread(); });
      }
    },
    markAllRead() {
      axios.put("/api/message/readAll", null, { params: { customerId: this.user.customerId } })
        .then(() => { this.messages.forEach(m => m.isRead = 1); this.loadUnread(); });
    },
    typeIcon(t) {
      const icons = { ship: "📦", shortage: "⚠️", system: "📢" };
      return icons[t] || "💬";
    },
    formatTime(t) {
      if (!t) return "";
      const d = new Date(t);
      return d.toLocaleDateString("zh-CN") + " " + d.toLocaleTimeString("zh-CN", { hour: "2-digit", minute: "2-digit" });
    }
  }
};
</script>

<style scoped>
.msg-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.msg-header h2 { margin-bottom: 0; }
.msg-list { display: flex; flex-direction: column; gap: 2px; }
.msg-item {
  display: flex; gap: 14px; padding: 16px 18px; border-radius: 12px;
  cursor: pointer; transition: all 0.2s; border-left: 4px solid transparent;
}
.msg-item:hover { background: #f6faf8; }
.msg-item.unread { background: #f0faf4; border-left-color: #27ae60; }
.msg-item.type-shortage.unread { background: #fff8f0; border-left-color: #f39c12; }
.msg-icon { font-size: 28px; min-width: 36px; text-align: center; }
.msg-body { flex: 1; }
.msg-top { display: flex; align-items: center; gap: 8px; margin-bottom: 4px; }
.msg-title { font-size: 15px; font-weight: 700; color: #1a3c2a; }
.dot { width: 8px; height: 8px; background: #e74c3c; border-radius: 50%; flex-shrink: 0; }
.msg-content { font-size: 13px; color: #777; margin: 4px 0; line-height: 1.5; }
.msg-meta { display: flex; gap: 10px; align-items: center; margin-top: 6px; }
.msg-time { font-size: 12px; color: #bbb; }
</style>
