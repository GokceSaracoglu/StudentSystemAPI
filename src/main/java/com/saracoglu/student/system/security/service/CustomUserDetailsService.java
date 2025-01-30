package com.saracoglu.student.system.security.service;

import com.saracoglu.student.system.security.entity.SecurityUser;
import com.saracoglu.student.system.security.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kullanıcıyı veritabanından alıyoruz
        SecurityUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // UserDetails objesini oluşturuyoruz ve geri döndürüyoruz
        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(SecurityUser user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()) // Spring Security User kullanıyoruz
                .password(user.getPassword())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))) // Rol ekleme
                .build();
    }
}
