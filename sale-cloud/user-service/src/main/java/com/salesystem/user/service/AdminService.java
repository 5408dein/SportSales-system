package com.salesystem.user.service;

import com.salesystem.user.entity.Admin;

public interface AdminService {

    Admin login(String username, String password);
}
