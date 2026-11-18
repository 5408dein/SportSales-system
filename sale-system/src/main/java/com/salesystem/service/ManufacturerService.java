package com.salesystem.service;

import com.salesystem.entity.Manufacturer;

import java.util.List;

public interface ManufacturerService {

    List<Manufacturer> findAll();

    int insert(Manufacturer manufacturer);

    int update(Manufacturer manufacturer);

    int delete(Integer id);
}