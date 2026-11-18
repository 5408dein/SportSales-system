package com.salesystem.product.mapper;

import com.salesystem.product.entity.Purchase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PurchaseMapper {

    List<Purchase> findAll();

    Purchase findById(@Param("id") Integer id);

    int insert(Purchase purchase);

    int updateStatus(@Param("purchaseId") Integer purchaseId,
                     @Param("status") String status,
                     @Param("arrivalDate") java.util.Date arrivalDate);

    int delete(@Param("id") Integer id);
}
