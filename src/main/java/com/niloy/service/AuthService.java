package com.niloy.service;

import com.niloy.domain.USER_ROLE;
import com.niloy.request.LoginRequest;
import com.niloy.response.AuthResponse;
import com.niloy.response.SignupRequest;

public interface AuthService {
    void sendLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;
    AuthResponse singIn(LoginRequest req) throws Exception;
}
