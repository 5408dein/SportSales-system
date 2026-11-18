package com.salesystem.mapper;

import com.salesystem.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    List<Customer> findAll();

    List<Customer> fromOrders();

    Customer findById(@Param("id") Integer id);

    Customer findByCode(@Param("customerCode") String customerCode);

    Customer login(@Param("customerCode") String customerCode,
                   @Param("password") String password);

    int insert(Customer customer);

    int update(Customer customer);

    int delete(@Param("id") Integer id);
}
