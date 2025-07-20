package com.niloy.service.Impl;

import com.niloy.config.JwtProvider;
import com.niloy.domain.USER_ROLE;
import com.niloy.modal.Cart;
import com.niloy.modal.Seller;
import com.niloy.modal.User;
import com.niloy.modal.VerificationCode;
import com.niloy.repository.CartRepository;
import com.niloy.repository.SellerRepository;
import com.niloy.repository.UserRepository;
import com.niloy.repository.VerificationCodeRepository;
import com.niloy.request.LoginRequest;
import com.niloy.response.AuthResponse;
import com.niloy.response.SignupRequest;
import com.niloy.service.AuthService;
import com.niloy.service.EmailService;
import com.niloy.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;

    @Override
    public void sendLoginOtp(String email, USER_ROLE role) throws Exception {

        String SIGNING_PREFIX = "signing_";

        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());
        }

        if (role.equals(USER_ROLE.ROLE_SELLER)) {
            Seller seller = sellerRepository.findByEmail(email);
            if (seller == null) {
                throw new Exception("seller not found");
            }
        } else {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new Exception("user not exist with provided email");
            }
        }
        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }
        String otp = OtpUtils.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a"));

        String subject = "OTP for Vendora login";

        String text = "<html>" +
                "<body style='margin: 0; padding: 0; background-color: #f4f4f7; font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif;'>" +
                "<div style='max-width: 600px; margin: 40px auto; padding: 20px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>" +

                // Logo
                "<div style='text-align: center; padding-bottom: 10px;'>" +
                "<img src='https://cdn.pixabay.com/photo/2025/07/15/12/42/12-42-29-494__340.png' alt='Vendora Logo' style='width: 120px; margin-bottom: 10px;'/>" +
                "<h2 style='color: #2c3e50; margin: 0;'>Vendora</h2>" +
                "<p style='color: #888888; font-size: 14px; margin: 4px 0;'>Secure OTP Verification</p>" +
                "<p style='color: #aaaaaa; font-size: 12px; margin-top: 0;'>" + currentTime + "</p>" +
                "</div>" +

                "<hr style='border: none; border-top: 1px solid #eee;'>" +

                // Message
                "<div style='padding: 20px 0;'>" +
                "<p style='font-size: 16px; color: #333;'>Hi there,</p>" +
                "<p style='font-size: 16px; color: #333;'>Please use the following One-Time Password (OTP) to complete your login:</p>" +
                "<div style='font-size: 32px; font-weight: bold; color: #e67e22; background-color: #fef6ec; padding: 12px 24px; text-align: center; border-radius: 8px; margin: 20px 0;'>" +
                otp +
                "</div>" +
                "<p style='font-size: 14px; color: #555;'>This OTP is valid for <strong>10 minutes</strong>. If you did not request this, you can safely ignore this email.</p>" +
                "</div>" +

                "<hr style='border: none; border-top: 1px solid #eee;'>" +

                // Footer
                "<div style='text-align: center; color: #999; font-size: 12px; margin-top: 20px;'>" +
                "<p>Thank you for using Vendora.</p>" +
                "<p style='margin-top: 8px;'>© 2025 Vendora Inc. All rights reserved.</p>" +
                "</div>" +

                "</div>" +
                "</body></html>";


        emailService.sendVerificationOtpEmail(email, otp, subject, text);



    }

    @Override
    public String createUser(SignupRequest req) throws Exception {


        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("Wrong OTP.....");
        }

        User user = userRepository.findByEmail(req.getEmail());
        if(user ==null){
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullname(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("01836709000");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse singIn(LoginRequest req) throws Exception {
        String  username = req.getEmail();
        String otp = req.getOtp();

        Authentication authentication = authenticate(username,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(token);
        authResponse.setMessage("Login successful");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName= authorities.isEmpty()?null: authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        String SELLER_PREFIX = "seller_";

        if(username.startsWith(SELLER_PREFIX)){

            username = username.substring(SELLER_PREFIX.length());

        }

        if(userDetails == null){
            throw new BadCredentialsException("User not found with username: " + username);
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong otp");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }
}
