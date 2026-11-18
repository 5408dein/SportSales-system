package com.salesystem.user.service;

import com.salesystem.common.dto.CustomerUpsertRequest;
import com.salesystem.user.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> findAll();

    List<Customer> findByIds(List<Integer> ids);

    Customer findById(Integer id);

    Customer findByCode(String customerCode);

    Customer login(String customerCode, String password);

    Customer upsert(CustomerUpsertRequest request);

    int insert(Customer customer);

    int update(Customer customer);

    int delete(Integer id);
}
