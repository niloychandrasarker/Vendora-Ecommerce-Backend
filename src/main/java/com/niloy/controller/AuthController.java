package com.niloy.controller;

import com.niloy.domain.USER_ROLE;
import com.niloy.modal.User;
import com.niloy.modal.VerificationCode;
import com.niloy.repository.UserRepository;
import com.niloy.response.ApiResponse;
import com.niloy.response.AuthResponse;
import com.niloy.response.SignupRequest;
import com.niloy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {

        String  jwt = authService.createUser(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("register successful");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);


        return ResponseEntity.ok(res);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse>sentOtpHandler(@RequestBody VerificationCode req) throws Exception {

        authService.sendLoginOtp(req.getEmail());
        ApiResponse res = new ApiResponse();
        res.setMessage("Otp sent successfully");



        return ResponseEntity.ok(res);
    }
}
