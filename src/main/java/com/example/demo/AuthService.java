package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, String> otpStore = new HashMap<>();

    // Notun: session data store korar jonno (username -> [sessionId, securityCode])
    private Map<String, String> sessionIdStore = new HashMap<>();
    private Map<String, String> securityCodeStore = new HashMap<>();

    public AppUser register(String username, String rawPassword) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        return appUserRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generateOtp(String username) {
        Random random = new Random();
        String otp = String.valueOf(100000 + random.nextInt(900000));
        otpStore.put(username, otp);

        System.out.println("=== OTP Generated ===");
        System.out.println("Username: " + username);
        System.out.println("OTP: " + otp);

        return otp;
    }

    public boolean verifyOtp(String username, String otp) {
        String storedOtp = otpStore.get(username);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(username);
            return true;
        }
        return false;
    }

    // Notun: Session ID + Security Code generate kore, store kore
    public String generateSessionId(String username) {
        String sessionId = "SESS-" + UUID.randomUUID().toString().substring(0, 8);
        sessionIdStore.put(username, sessionId);
        return sessionId;
    }

    public String generateSecurityCode(String username) {
        Random random = new Random();
        String code = "SEC-" + (1000 + random.nextInt(9000));
        securityCodeStore.put(username, code);
        return code;
    }

    // Notun: Session ID ar Security Code thik ache kina check kore
    public boolean isSessionValid(String username, String sessionId) {
        String stored = sessionIdStore.get(username);
        return stored != null && stored.equals(sessionId);
    }

    public boolean isSecurityCodeValid(String username, String code) {
        String stored = securityCodeStore.get(username);
        return stored != null && stored.equals(code);
    }
}