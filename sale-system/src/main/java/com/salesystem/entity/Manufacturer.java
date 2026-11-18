package com.salesystem.entity;

public class Manufacturer {

    private Integer manufacturerId;
    private String manufacturerCode;
    private String manufacturerName;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;

    public Integer getManufacturerId() { return manufacturerId; }
    public void setManufacturerId(Integer manufacturerId) { this.manufacturerId = manufacturerId; }
    public String getManufacturerCode() { return manufacturerCode; }
    public void setManufacturerCode(String manufacturerCode) { this.manufacturerCode = manufacturerCode; }
    public String getManufacturerName() { return manufacturerName; }
    public void setManufacturerName(String manufacturerName) { this.manufacturerName = manufacturerName; }
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
