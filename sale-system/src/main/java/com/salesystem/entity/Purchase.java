package com.salesystem.entity;

import java.util.Date;

public class Purchase {

    private Integer purchaseId;
    private Integer productId;
    private Integer manufacturerId;
    private Integer purchaseQuantity;
    private Date purchaseDate;
    private Date arrivalDate;
    private String status;
    private String notes;

    // 关联查询用
    private String productName;
    private String manufacturerName;
    private Integer currentStock;

    public Integer getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Integer purchaseId) { this.purchaseId = purchaseId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getManufacturerId() { return manufacturerId; }
    public void setManufacturerId(Integer manufacturerId) { this.manufacturerId = manufacturerId; }
    public Integer getPurchaseQuantity() { return purchaseQuantity; }
    public void setPurchaseQuantity(Integer purchaseQuantity) { this.purchaseQuantity = purchaseQuantity; }
    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
    public Date getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(Date arrivalDate) { this.arrivalDate = arrivalDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getManufacturerName() { return manufacturerName; }
    public void setManufacturerName(String manufacturerName) { this.manufacturerName = manufacturerName; }
    public Integer getCurrentStock() { return currentStock; }
    public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }
}
