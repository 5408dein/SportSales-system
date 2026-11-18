package com.salesystem.user.mapper;

import com.salesystem.user.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    List<Customer> findAll();

    List<Customer> findByIds(@Param("ids") List<Integer> ids);

    Customer findById(@Param("id") Integer id);

    Customer findByCode(@Param("customerCode") String customerCode);

    Customer login(@Param("customerCode") String customerCode,
                   @Param("password") String password);

    int insert(Customer customer);

    int update(Customer customer);

    int delete(@Param("id") Integer id);
}
