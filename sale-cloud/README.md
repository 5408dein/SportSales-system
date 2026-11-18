# 体育用品销售系统 — 微服务版

原单体 `sale-system`（Spring Boot 单体）改造而成的微服务架构，接入 Nacos 注册/配置中心 + Spring Cloud Gateway 网关。

## 架构

```
                    Nacos (8848) 注册中心 + 配置中心
                          ▲ 注册/发现
                          │
前端 :8081 ──代理──▶ Gateway :9000 ──lb:// 路由──▶ user-service    :8101  (顾客/管理员/消息)
                                                    product-service :8102  (商品/厂商/进货/库存)
                                                    order-service   :8103  (订单/明细)
                          │
                   共享 MySQL sale_system（逻辑上各服务只访问自己的表）
```

- 对外 URL 全部保持 `/api/**` 不变，前端只改代理目标。
- 网关只做路由 + CORS，无业务逻辑。
- 跨服务内部接口走 `/internal/**`，不经过网关、不对前端暴露。

## 模块

| 模块 | 端口 | 职责 |
|---|---|---|
| `sale-common` | — | 统一返回体 `Result`、异常、跨服务 DTO |
| `user-service` | 8101 | 顾客 / 管理员 / 站内消息 |
| `product-service` | 8102 | 商品 / 厂商 / 进货 / 库存 |
| `order-service` | 8103 | 订单 / 明细 |
| `gateway` | 9000 | 路由 + CORS |

## 技术栈

- Spring Boot 4.0.6、Java 17
- Spring Cloud 2025.1.0、Spring Cloud Alibaba 2025.1.0.0（nacos-client 3.1.1）
- Spring Cloud Gateway（WebFlux）、OpenFeign、MyBatis、MySQL 8.4

## 启动顺序

1. **MySQL**（库 `sale_system`，8 张表，脚本在 `sale-system/sale_system.sql`）
2. **Nacos**：`D:\APP\nacos\bin\startup.cmd -m standalone`（端口 8848，鉴权已关闭）
3. **三个业务服务**（任选顺序）：
   ```
   java -jar user-service/target/user-service-1.0.0-SNAPSHOT.jar
   java -jar product-service/target/product-service-1.0.0-SNAPSHOT.jar
   java -jar order-service/target/order-service-1.0.0-SNAPSHOT.jar
   ```
4. **网关**：`java -jar gateway/target/gateway-1.0.0-SNAPSHOT.jar`
5. **前端**：`cd salesystem-ui && npm run serve`（访问 http://localhost:8081）

> ⚠️ 注意：Nacos 3.x 控制台默认占 **8080**，不要与原单体 `sale-system`（8080）同时启动。

## 关键设计

- **服务发现**：各服务注册到 Nacos，网关用 `lb://服务名` 路由，订单服务用 OpenFeign 走服务发现调用用户/商品服务。
- **订单创建 Saga**：`OrderCreateSaga` 依次「查商品快照 → 顾客 upsert → 批量扣库存 → 本地事务写订单+明细」，失败时补偿恢复库存。
- **防超卖**：库存扣减用原子 SQL `update product set stock = stock - qty where product_id = ? and stock >= qty`，批量扣减在一个事务内全成或全败。
- **配置中心**：各服务通过 `spring.config.import: optional:nacos:<服务名>.yaml` 接入，Nacos 中无该 dataId 也能启动（optional）。

## 测试账号

- 管理员：`admin / 123456`
- 顾客：`C001 / 123456`
