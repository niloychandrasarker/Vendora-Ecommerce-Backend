package com.niloy.response;

import com.niloy.domain.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    public  String jwt;
    public String message;
    public USER_ROLE role;
}
