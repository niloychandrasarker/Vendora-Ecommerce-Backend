package com.niloy.service;

import com.niloy.modal.User;

public interface UserService {
    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
    
}
