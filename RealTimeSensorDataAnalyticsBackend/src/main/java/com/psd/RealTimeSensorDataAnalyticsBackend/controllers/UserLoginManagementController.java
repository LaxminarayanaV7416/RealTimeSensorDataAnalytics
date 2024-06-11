package com.psd.RealTimeSensorDataAnalyticsBackend.controllers;

import com.psd.RealTimeSensorDataAnalyticsBackend.models.TokenModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.Users;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.UserRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserLoginManagementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User Registered Successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<Map<String, String>> registerAdminUser(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin User Registered Successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody TokenModel tokenModel) {
        Users user = userRepository.findByUsername(tokenModel.getUsername());
        if (user != null && bCryptPasswordEncoder.matches(tokenModel.getPassword(), user.getPassword())) {
            String token = jwtTokenUtil.generateToken(user.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestBody TokenModel tokenModel) {
        if (jwtTokenUtil.validateToken(tokenModel.getToken())) {
            return ResponseEntity.ok("valid");
        } else {
            return ResponseEntity.status(401).body("invalid");
        }
    }
}
