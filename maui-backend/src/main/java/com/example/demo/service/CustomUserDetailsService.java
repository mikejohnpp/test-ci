package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.social.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        org.social.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        System.out.println("user: "+user);
        List<UserRole> roles = userRoleRepository.findByUserID(user);

//        System.out.println(roles);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRoleID().getRoleName()))
                .toList();
//        System.out.println("authorities: " + authorities);
        // Trả về UserDetails "chuẩn" của Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // mật khẩu đã BCrypt hash
                .authorities(authorities)     // hoặc lấy role từ DB
                .build();
    }
}

