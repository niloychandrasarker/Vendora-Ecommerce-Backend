package com.niloy.service;

import com.niloy.response.SignupRequest;

public interface AuthService {
    String createUser(SignupRequest req);
}
