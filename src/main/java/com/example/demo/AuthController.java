package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@Valid @RequestBody AuthRequest request) {
        authService.register(request.getUsername(), request.getPassword());
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        AppUser user = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean matches = authService.checkPassword(request.getPassword(), user.getPassword());
        if (!matches) {
            throw new RuntimeException("Invalid password");
        }

        authService.generateOtp(user.getUsername());
        return "OTP sent! Check console.";
    }

    @PostMapping("/verify-otp")
    public AuthResponse verifyOtp(@RequestBody OtpRequest request) {
        boolean isValid = authService.verifyOtp(request.getUsername(), request.getOtp());
        if (!isValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        String token = jwtUtil.generateToken(request.getUsername());
        String sessionId = authService.generateSessionId(request.getUsername());
        String securityCode = authService.generateSecurityCode(request.getUsername());

        return new AuthResponse(token, sessionId, securityCode);
    }
}