package com.salesystem.order.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {

    private Integer orderId;
    private String orderNo;
    private Date orderDate;
    private Integer customerId;
    private String shippingRequirement;
    private String supplyFlag;
    private Date shippingDate;
    private BigDecimal totalWeight;
    private BigDecimal freight;
    private String paymentStatus;
    private String receiver;
    private String phone;
    private String address;
    private String invoiceFlag;
    private String paymentMethod;
    private String customerCode;
    private BigDecimal totalAmount;
    private List<OrderDetail> items;

    public List<OrderDetail> getItems() {
        return items;
    }

    public void setItems(List<OrderDetail> items) {
        this.items = items;
    }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public String getShippingRequirement() { return shippingRequirement; }
    public void setShippingRequirement(String shippingRequirement) { this.shippingRequirement = shippingRequirement; }
    public String getSupplyFlag() { return supplyFlag; }
    public void setSupplyFlag(String supplyFlag) { this.supplyFlag = supplyFlag; }
    public Date getShippingDate() { return shippingDate; }
    public void setShippingDate(Date shippingDate) { this.shippingDate = shippingDate; }
    public BigDecimal getTotalWeight() { return totalWeight; }
    public void setTotalWeight(BigDecimal totalWeight) { this.totalWeight = totalWeight; }
    public BigDecimal getFreight() { return freight; }
    public void setFreight(BigDecimal freight) { this.freight = freight; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getInvoiceFlag() { return invoiceFlag; }
    public void setInvoiceFlag(String invoiceFlag) { this.invoiceFlag = invoiceFlag; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
