package com.salesystem.product.mapper;

import com.salesystem.product.entity.Manufacturer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManufacturerMapper {

    List<Manufacturer> findAll();

    Manufacturer findById(Integer id);

    int insert(Manufacturer manufacturer);

    int update(Manufacturer manufacturer);

    int delete(Integer id);
}
