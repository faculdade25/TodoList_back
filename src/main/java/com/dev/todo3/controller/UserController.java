package com.dev.todo3.controller;

import com.dev.todo3.entity.User;
import com.dev.todo3.security.Login;
import com.dev.todo3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        return ResponseEntity.ok(userService.logar(login));
    }

    @PostMapping("/register")
    public ResponseEntity<String> cadastro(@RequestBody User usuario) {
        try {
            System.out.println(usuario.getEmail());
            System.out.println(usuario.getPassword());
            String token = userService.cadastro(usuario);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
