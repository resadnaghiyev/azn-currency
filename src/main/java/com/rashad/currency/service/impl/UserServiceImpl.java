package com.rashad.currency.service.impl;

import com.rashad.currency.entity.User;
import com.rashad.currency.jwt.JwtUtils;
import com.rashad.currency.model.JwtResponse;
import com.rashad.currency.model.RegisterRequest;
import com.rashad.currency.repository.UserRepository;
import com.rashad.currency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("user: " + request.getUsername() +
                    " adlı istifadəçi artıq bazada mövcuddur. " +
                    "Zəhmət olmasa basqa istifadəçi adı seçin.");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return request.getUsername() + " adlı istifadəçi uğurla register oldu. " +
                "Artıq siz bu istifadəçi adıyla login ola bilərsiz.";
    }

    @Override
    public JwtResponse login(RegisterRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()));
        String access_token = jwtUtils.generateAccessToken(userDetails);
        String refresh_token = jwtUtils.generateRefreshToken(userDetails);
        return new JwtResponse(access_token, refresh_token);
    }

}
