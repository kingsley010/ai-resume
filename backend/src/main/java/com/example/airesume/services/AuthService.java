package com.example.airesume.services;

import com.example.airesume.entities.User;
import com.example.airesume.repositories.UserRepository;
import com.example.airesume.security.JwtTokenProvider;
import com.example.airesume.utils.PasswordUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtProvider;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public String signup(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordUtils.hash(password));
        userRepository.save(user);
        return jwtProvider.generateToken(user.getId().toString());
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!PasswordUtils.verify(password, user.getPasswordHash()))
            throw new RuntimeException("Invalid password");
        return jwtProvider.generateToken(user.getId().toString());
    }
}
