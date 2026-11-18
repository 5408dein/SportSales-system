package com.salesystem.service;

import com.salesystem.entity.Admin;

public interface AdminService {

    Admin login(String username, String password);
}
