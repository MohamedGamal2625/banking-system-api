package com.global.bankingsystemapi.controller;

import com.global.bankingsystemapi.dto.RequestLoginDto;
import com.global.bankingsystemapi.dto.RequestRegisterDto;
import com.global.bankingsystemapi.security.AppUser;
import com.global.bankingsystemapi.security.AppUserRepository;
import com.global.bankingsystemapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RequestRegisterDto request) {

        String username = request.getUsername();
        String password = request.getPassword();

        // Check username not already taken
        if (appUserRepository
                .findByUsername(username)
                .isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Username already taken!");
        }
        // Build and save new user
        AppUser newUser = AppUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .build();
        appUserRepository.save(newUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Registered successfully!");

    }

    // ─────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody RequestLoginDto request) {

        try {
            // Verify username + password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Load user to get role
            AppUser user = appUserRepository
                    .findByUsername(request.getUsername())
                    .orElseThrow();

            // Generate token
            String token = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    Map.of("token", token));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error",
                            "Invalid credentials!"));
        }
    }
}

