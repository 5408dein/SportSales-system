package com.salesystem.user.service.impl;

import com.salesystem.common.dto.CustomerUpsertRequest;
import com.salesystem.user.entity.Customer;
import com.salesystem.user.mapper.CustomerMapper;
import com.salesystem.user.service.CustomerService;

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
    public List<Customer> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return customerMapper.findByIds(ids);
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

    /**
     * 幂等建/更顾客：按 customerCode 查找，不存在则建档，存在则更新联系方式。
     * 供订单服务在创建订单时调用。
     */
    @Override
    public Customer upsert(CustomerUpsertRequest req) {
        Customer customer = customerMapper.findByCode(req.getCustomerCode());
        if (customer == null) {
            customer = new Customer();
            customer.setCustomerCode(req.getCustomerCode());
            customer.setCustomerName(req.getCustomerName() != null ? req.getCustomerName()
                    : (req.getName() != null ? req.getName() : "新顾客"));
            customer.setName(req.getName());
            customer.setPhone(req.getPhone());
            customer.setAddress(req.getAddress());
            customer.setPostcode(req.getPostcode());
            customer.setPassword("123456");
            customerMapper.insert(customer);
        } else {
            if (req.getCustomerName() != null) customer.setCustomerName(req.getCustomerName());
            if (req.getName() != null) customer.setName(req.getName());
            if (req.getPhone() != null) customer.setPhone(req.getPhone());
            if (req.getAddress() != null) customer.setAddress(req.getAddress());
            if (req.getPostcode() != null) customer.setPostcode(req.getPostcode());
            customerMapper.update(customer);
        }
        return customer;
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
