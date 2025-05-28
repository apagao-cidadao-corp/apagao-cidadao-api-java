package com.apagao.cidadao.controller;

import com.apagao.cidadao.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Autenticação de usuário e geração de token JWT")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.get("username"),
                request.get("password")
            )
        );

        String token = jwtService.generateToken(authentication.getName());
        return Map.of("token", token);
    }
}
