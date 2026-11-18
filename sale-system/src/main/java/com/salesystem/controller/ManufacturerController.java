package com.salesystem.controller;

import com.salesystem.entity.Manufacturer;
import com.salesystem.service.ManufacturerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
@CrossOrigin
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping("/list")
    public List<Manufacturer> list() {
        return manufacturerService.findAll();
    }

    @PostMapping("/add")
    public String add(@RequestBody Manufacturer manufacturer) {
        manufacturerService.insert(manufacturer);
        return "success";
    }

    @PutMapping("/update")
    public String update(@RequestBody Manufacturer manufacturer) {
        manufacturerService.update(manufacturer);
        return "success";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        manufacturerService.delete(id);
        return "success";
    }
}