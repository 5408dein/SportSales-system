package com.salesystem.product.mapper;

import com.salesystem.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAll();

    Product findById(@Param("id") Integer id);

    List<Product> findByIds(@Param("ids") List<Integer> ids);

    void insert(Product product);

    void update(Product product);

    void delete(@Param("id") Integer id);

    void updateStatus(Product product);

    void updateStock(Product product);

    List<Product> lowStock();

    int reduceStock(@Param("productId") Integer productId,
                    @Param("quantity") Integer quantity);

    int increaseStock(@Param("productId") Integer productId,
                      @Param("quantity") Integer quantity);
}
