package com.project.web4.controllers;

import com.project.web4.DTO.UserDTO;
import com.project.web4.config.jwt.JWTProvider;
import com.project.web4.model.User;
import com.project.web4.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log
@RestController
@RequestMapping(path = "/api/user")
@AllArgsConstructor
public class UserController {
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthenticationManager authManager;
    @Autowired
    private final JWTProvider jwtProvider;

    @PostMapping("/check-user")
    private ResponseEntity<String> login(@Valid @RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, userDTO.getPassword()));
            String token = jwtProvider.generateToken(username);
            log.info("User passed verification");
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            log.severe("Failed user authentication: " + e.getMessage());
            return new ResponseEntity<>("Пользователь не найден или пароль введен не верно", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/new-user")
    private ResponseEntity<String> register(@Valid @RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = bCryptPasswordEncoder.encode(userDTO.getPassword());
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if (userService.saveUser(user)) {
            String token = jwtProvider.generateToken(username);
            log.info("Add new user");
            return ResponseEntity.ok(token);
        } else {
            log.severe("Failed adding new user");
            return new ResponseEntity<>("Пользователь с данным логином уже существует", HttpStatus.BAD_REQUEST);
        }
    }
}
