package com.salesystem.service.impl;

import com.salesystem.entity.Customer;
import com.salesystem.mapper.CustomerMapper;
import com.salesystem.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> findAll() {
        return customerMapper.findAll();
    }

    @Override
    public List<Customer> fromOrders() {
        return customerMapper.fromOrders();
    }

    @Override
    public Customer findById(Integer id) {
        return customerMapper.findById(id);
    }

    @Override
    public Customer findByCode(String customerCode) {
        return customerMapper.findByCode(customerCode);
    }

    @Override
    public Customer login(String customerCode, String password) {
        return customerMapper.login(customerCode, password);
    }

    @Override
    public int insert(Customer customer) {
        return customerMapper.insert(customer);
    }

    @Override
    public int update(Customer customer) {
        return customerMapper.update(customer);
    }

    @Override
    public int delete(Integer id) {
        return customerMapper.delete(id);
    }
}
