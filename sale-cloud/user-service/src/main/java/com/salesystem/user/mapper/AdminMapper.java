package com.salesystem.user.mapper;

import com.salesystem.user.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {

    Admin login(@Param("username") String username,
                @Param("password") String password);

    Admin findByUsername(@Param("username") String username);
}
