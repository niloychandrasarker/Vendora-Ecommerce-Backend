package com.niloy.service;

import com.niloy.response.SignupRequest;

public interface AuthService {
    void sendLoginOtp(String email) throws Exception;
    String createUser(SignupRequest req) throws Exception;
}
