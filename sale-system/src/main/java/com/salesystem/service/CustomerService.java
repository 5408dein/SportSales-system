package com.salesystem.service;

import com.salesystem.entity.Customer;
import java.util.List;

public interface CustomerService {

    List<Customer> findAll();

    List<Customer> fromOrders();

    Customer findById(Integer id);

    Customer findByCode(String customerCode);

    Customer login(String customerCode, String password);

    int insert(Customer customer);

    int update(Customer customer);

    int delete(Integer id);
}
