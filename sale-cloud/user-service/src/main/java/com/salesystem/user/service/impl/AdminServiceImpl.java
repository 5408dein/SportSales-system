package com.salesystem.user.service.impl;

import com.salesystem.user.entity.Admin;
import com.salesystem.user.mapper.AdminMapper;
import com.salesystem.user.service.AdminService;
import com.salesystem.user.util.EncryptUtil;

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

        // 明文不匹配，尝试 BCrypt 验证
        Admin adminByUser = adminMapper.findByUsername(username);
        if (adminByUser != null && encryptUtil.matchPassword(password, adminByUser.getPassword())) {
            return adminByUser;
        }
        return null;
    }
}
