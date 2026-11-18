package com.salesystem.product.service.impl;

import com.salesystem.product.entity.Manufacturer;
import com.salesystem.product.mapper.ManufacturerMapper;
import com.salesystem.product.service.ManufacturerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Override
    public List<Manufacturer> findAll() {
        return manufacturerMapper.findAll();
    }

    @Override
    public int insert(Manufacturer manufacturer) {
        return manufacturerMapper.insert(manufacturer);
    }

    @Override
    public int update(Manufacturer manufacturer) {
        return manufacturerMapper.update(manufacturer);
    }

    @Override
    public int delete(Integer id) {
        return manufacturerMapper.delete(id);
    }
}
