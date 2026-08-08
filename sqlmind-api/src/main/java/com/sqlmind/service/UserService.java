package com.sqlmind.service;

import com.sqlmind.dto.request.LoginRequest;
import com.sqlmind.dto.request.RegisterRequest;
import com.sqlmind.dto.response.LoginResponse;
import com.sqlmind.dto.response.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
