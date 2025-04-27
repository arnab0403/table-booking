package com.major.k1.resturant.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.k1.resturant.DTO.DtoRegUser;
import com.major.k1.resturant.DTO.LoginDto;
import com.major.k1.resturant.DTO.LoginRequest;
import com.major.k1.resturant.DTO.UserChangePassDto;
import com.major.k1.resturant.Repository.UserRepository;
import com.major.k1.resturant.Security.JwtUtil;
import com.major.k1.resturant.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/register-temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerTemp(@RequestPart("user") String userJson,
                                               @RequestPart("image") MultipartFile file) {
        try {
            userService.registerTemp(userJson, file);
            return ResponseEntity.ok("OTP sent to email.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        try {
            boolean success = userService.verifyOtp(email, otp, passwordEncoder, userRepository);
            if (success) {
                return ResponseEntity.ok("OTP verified. User registered.");
            } else {
                return ResponseEntity.badRequest().body("Invalid OTP or no registration found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username=authentication.getName();
        String token = jwtUtil.generateToken(username);
        return  ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Login successful"

        ));

    }








    @PutMapping("/changepassword")
    public String changepassword(@RequestBody UserChangePassDto userChangePassDto , Authentication authentication){
        String username=authentication.getName();
        boolean i=userService.changepassword(username,userChangePassDto.getOldpassword(),userChangePassDto.getNewpasssword());
        if (i){
            return "Success";
        }
        else {
            return "failed";
        }
    }


}
