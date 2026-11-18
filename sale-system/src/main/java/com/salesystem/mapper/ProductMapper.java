package com.salesystem.mapper;

import com.salesystem.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAll();

    Product findById(@Param("id") Integer id);

    void insert(Product product);

    void update(Product product);

    void delete(@Param("id") Integer id);

    void updateStatus(Product product);

    void updateStock(Product product);

    List<Product> lowStock();

    int reduceStock(@Param("productId") Integer productId,
                    @Param("quantity") Integer quantity);
}