package com.salesystem.mapper;

import com.salesystem.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {

    Admin login(@Param("username") String username,
                @Param("password") String password);

    Admin findByUsername(@Param("username") String username);
}