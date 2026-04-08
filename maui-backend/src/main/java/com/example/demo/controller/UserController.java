package com.example.demo.controller;

import org.social.Dtos.LoginRequest;
import org.social.Dtos.response.LoginResponse;
import org.social.config.ResponseApi;
import org.social.handler.ResponseStatus;
import com.example.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    @PostMapping("/login")
    public ResponseEntity<ResponseApi> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Tìm user theo email
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            System.out.println(userDetails);
//            return ResponseEntity.ok(new ResponseApi(ResponseStatus.SUCCESS, userDetails));

            // 2. Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseApi(ResponseStatus.UNAUTHORIZED, null));
            }
            // 3. Tạo claims cho JWT (có thể thêm thông tin khác nếu cần)
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", userDetails.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .collect(Collectors.toList()));

            // 4. Generate JWT token
            String token = jwtService.generateToken(claims, userDetails.getUsername());

            // 5. Tạo response với token và thông tin user
            LoginResponse loginResponse = new LoginResponse(
                    token,
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(auth -> auth.getAuthority())
                            .collect(Collectors.toList())
            );

            return ResponseEntity.ok(new ResponseApi(ResponseStatus.SUCCESS, loginResponse));

        } catch (UsernameNotFoundException e) {
             throw new ResourceNotFoundException();
        }
    }
}
