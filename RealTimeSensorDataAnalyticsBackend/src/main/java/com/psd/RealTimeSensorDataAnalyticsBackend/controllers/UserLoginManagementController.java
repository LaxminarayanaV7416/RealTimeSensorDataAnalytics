package com.psd.RealTimeSensorDataAnalyticsBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.psd.RealTimeSensorDataAnalyticsBackend.models.Users;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.TokenModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.UserRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;
import com.psd.RealTimeSensorDataAnalyticsBackend.constants.UserEnum;

@RestController
@RequestMapping
public class UserLoginManagementController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody Users user){
        Users searchedUser = userRepository.findByUsername(user.getUsername());
        Map<String, String> resultResponse = new HashMap<>();
        if (searchedUser == null){
            String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setUserType(UserEnum.IS_USER.toString());
            if (userRepository.save(user).getId()>0){
                resultResponse.put("message", "User Registered Succesfully");
                return ResponseEntity.ok(resultResponse);
            }
            resultResponse.put("message", "User Not Saved, Internal Server Error. Please Try Again");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultResponse);
        }
        resultResponse.put("message", "User Not Saved, User already exsists");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(resultResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register-admin")
    public ResponseEntity<Object> registerAdminUser(@RequestBody Users user){
        Users searchedUser = userRepository.findByUsername(user.getUsername());
        Map<String, String> resultResponse = new HashMap<>();
        if (searchedUser == null){
            String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setUserType(UserEnum.IS_ADMIN.toString());
            if (userRepository.save(user).getId()>0){
                resultResponse.put("message", "User Registered Succesfully");
                return ResponseEntity.ok(resultResponse);
            }
            resultResponse.put("message", "User Not Saved, Internal Server Error. Please Try Again");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultResponse);
        }
        resultResponse.put("message", "User Not Saved, User already exsists");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(resultResponse);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<Object> generateToken(@RequestBody TokenModel tokenReqRes){
        Users databaseUser = userRepository.findByUsername(tokenReqRes.getUsername());
        Map<String, String> resultResponse = new HashMap<>();
        if (databaseUser == null){
            resultResponse.put("message", "Sorry, User Does Not Exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultResponse);
        }
        if (new BCryptPasswordEncoder().matches(tokenReqRes.getPassword(), databaseUser.getPassword())){
            String tokenString = tokenReqRes.getUsername() + " <> " + databaseUser.getId() + " <> " + databaseUser.getUserType();
            String token = jwtTokenUtil.generateToken(tokenString);
            tokenReqRes.setToken(token);
            tokenReqRes.setExpirationTime("60 Min");
            resultResponse.put("username", tokenReqRes.getUsername());
            resultResponse.put("expiryTime", tokenReqRes.getExpirationTime());
            resultResponse.put("token", tokenReqRes.getToken());
            return ResponseEntity.ok(resultResponse);
        }else {
            resultResponse.put("message", "Password Doesn't Match. Verify");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultResponse);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody TokenModel tokenReqRes){
        return ResponseEntity.ok(jwtTokenUtil.validateToken(tokenReqRes.getToken()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/test-login")
    public  ResponseEntity<Object> getAllFruits(@RequestHeader(value = "Authorization", required = false) String token){
        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Is required to Proceed");
        }else{
            System.out.println("TOKEN IS --> "+token);
            String realToken = token.substring(7);
            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);
            String username = jwtTokenUtil.getUsernameFromToken(realToken);
            System.out.println("USERNAME --> "+username);
            if (tokenCheckResult){
                List<String> fruits = List.of("Mango", "Banana", "Orange","Watermellon","Grapes", "Appple", "Berries");
                return new ResponseEntity<>(fruits, HttpStatus.OK);
            }else{
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unautorixed dur to: " + tokenCheckResult);
            }
        }
    }


}