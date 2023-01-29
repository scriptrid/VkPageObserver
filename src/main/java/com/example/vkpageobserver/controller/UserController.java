package com.example.vkpageobserver.controller;

import com.example.vkpageobserver.model.dto.CreateUserDto;
import com.example.vkpageobserver.model.dto.LoginUserDto;
import com.example.vkpageobserver.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void newUser(HttpServletRequest req,@RequestBody @Valid CreateUserDto dto) {
        userService.addUser(dto);
        doLogin(req, dto.username(), dto.password());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest req, @RequestBody LoginUserDto dto) {
        if (!doLogin(req, dto.username(), dto.password())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    private boolean doLogin(HttpServletRequest req, String username, String password) {
        try {
            req.login(username, password);
        } catch (ServletException e) {
            log.error("Error while login", e);
            return false;
        }
        return true;
    }

}
