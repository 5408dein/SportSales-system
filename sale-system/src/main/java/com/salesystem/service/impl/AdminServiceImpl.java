package com.salesystem.service.impl;

import com.salesystem.entity.Admin;
import com.salesystem.mapper.AdminMapper;
import com.salesystem.service.AdminService;
import com.salesystem.util.EncryptUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private EncryptUtil encryptUtil;

    @Override
    public Admin login(String username, String password) {
        // 先直接查数据库（兼容明文旧密码）
        Admin admin = adminMapper.login(username, password);
        if (admin != null) return admin;

        // 明文不匹配，尝试BCrypt验证（加分项6：密码加密）
        // 查找用户后用BCrypt比对
        Admin adminByUser = adminMapper.findByUsername(username);
        if (adminByUser != null && encryptUtil.matchPassword(password, adminByUser.getPassword())) {
            return adminByUser;
        }
        return null;
    }
}
