# 悦动体育商城

基于 **Spring Cloud Alibaba 微服务架构** 的体育用品进销存电商平台。原单体 `sale-system` 重构为「用户 / 商品 / 订单」3 个微服务 + 统一网关，接入 Nacos 实现服务注册发现与配置管理，前端 Vue 单页应用与后端接口无缝对接。

## 技术栈

| 层 | 技术 |
|---|---|
| 微服务框架 | Spring Boot 4.0.6、Spring Cloud 2025.1.0、Spring Cloud Alibaba 2025.1.0.0 |
| 服务治理 | Nacos（注册中心 + 配置中心）、Spring Cloud Gateway（WebFlux）、OpenFeign、LoadBalancer |
| 数据层 | MyBatis、MySQL 8.4 |
| 前端 | Vue 2、vue-router、axios |
| 构建 | Maven 多模块、Java 17 |

## 架构

```
                    Nacos :8848（注册中心 + 配置中心）
                          ▲ 注册 / 发现
                          │
前端 :8081 ──代理──▶ Gateway :9000 ──lb:// 路由──▶ user-service    :8101  (顾客/管理员/消息)
                                                    product-service :8102  (商品/厂商/进货/库存)
                                                    order-service   :8103  (订单/明细)
                          │
                   共享 MySQL sale_system（8 张表）
```

- 对外接口统一 `/api/**`，前端只对接网关一个入口。
- 跨服务内部接口走 `/internal/**`，不经过网关、不对前端暴露。
- 数据源等共享配置由 Nacos 配置中心统一管理。

## 功能特性

| 角色 | 功能 |
|---|---|
| 顾客 | 注册登录、浏览商品、下单结算、查看订单、收站内消息（发货/缺货通知） |
| 管理员 | 商品上架下架、订单发货/缺货标记、顾客管理、厂商管理、进货管理 |

业务闭环：**进货到货 → 自动加库存；顾客下单 → 自动扣库存；库存不足 → 低库存预警 + 缺货提醒**。

## 核心设计

1. **微服务拆分**：按业务域拆分，同一份数据的事务留在同一服务内（如进货到货改进货单 + 加库存都在商品服务）。
2. **Saga 分布式事务**：下单跨商品/订单服务，采用「扣库存 → 建订单」两阶段 + 失败补偿回滚库存，保证最终一致性。
3. **防超卖**：库存扣减用原子 SQL 条件更新（`where stock >= qty`），批量扣减单事务内全成或全败。
4. **幂等设计**：顾客建档 `upsert` 先查后改，重复调用不产生重复数据。
5. **配置中心**：数据源等共享配置放 Nacos `share-datasource.yaml`，多服务共享、改一处全生效。

## 目录结构

```
悦动体育商城/
├── sale-system/         # 原单体（对照/回滚基线）
├── salesystem-ui/       # 前端 Vue 应用
└── sale-cloud/          # 微服务工程（Maven 多模块）
    ├── pom.xml          # 父工程：统一管依赖版本
    ├── sale-common/     # 公共模块：Result / 全局异常 / 跨服务 DTO
    ├── user-service/    # 用户服务 :8101
    ├── product-service/ # 商品服务 :8102
    ├── order-service/   # 订单服务 :8103
    └── gateway/         # 网关 :9000
```

## 快速开始

### 环境要求

- JDK 17+、Maven 3.8+
- MySQL 8.4（已初始化 `sale_system` 库，脚本见 `sale-system/sale_system.sql`）
- Nacos（本机 standalone，端口 8848）
- Node.js（前端）

### 启动顺序

1. **MySQL**（库 `sale_system`）
2. **Nacos**：`startup.cmd -m standalone`（鉴权已关闭，端口 8848）
3. **配置中心**：在 Nacos 创建共享数据源配置 `share-datasource.yaml`（group `DEFAULT_GROUP`）：

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://127.0.0.1:3306/sale_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8
       username: root
       password: 881011
       driver-class-name: com.mysql.cj.jdbc.Driver
   ```

4. **构建**：

   ```bash
   cd sale-cloud
   mvn clean install -DskipTests
   ```

5. **启动三个业务服务**（任选顺序）：

   ```bash
   java -jar user-service/target/user-service-1.0.0-SNAPSHOT.jar
   java -jar product-service/target/product-service-1.0.0-SNAPSHOT.jar
   java -jar order-service/target/order-service-1.0.0-SNAPSHOT.jar
   ```

6. **启动网关**：

   ```bash
   java -jar gateway/target/gateway-1.0.0-SNAPSHOT.jar
   ```

7. **启动前端**：

   ```bash
   cd salesystem-ui
   npm install
   npm run serve
   ```

访问前端：http://localhost:8081

## 端口规划

| 组件 | 端口 |
|---|---|
| Nacos | 8848（API）/ 9848（gRPC）/ 8080（控制台） |
| gateway | 9000 |
| user-service | 8101 |
| product-service | 8102 |
| order-service | 8103 |
| 前端 devServer | 8081 |
| MySQL | 3306 |

> ⚠️ 注意：Nacos 3.x 控制台默认占 8080，不要与原单体 `sale-system`（8080）同时启动。

## 测试账号

- 管理员：`admin / 123456`
- 顾客：`C001 / 123456`
