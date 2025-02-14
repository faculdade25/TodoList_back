package com.dev.todo3.service;

import com.dev.todo3.entity.User;
import com.dev.todo3.repository.UserRepository;
import com.dev.todo3.security.JwtServiceGenerator;
import com.dev.todo3.security.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtServiceGenerator jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       JwtServiceGenerator jwtService,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public String logar(Login login) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(),
                        login.getPassword()
                )
        );
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return jwtService.generateToken(user);
    }

    public String cadastro(User usuario) throws Exception {
        Optional<User> existingUser = userRepository.findByEmail(usuario.getEmail());
        if (existingUser.isPresent()) {
            throw new Exception("Usuário já cadastrado com este email");
        }

        User user = new User();
        user.setEmail(usuario.getEmail());
        user.setPassword(passwordEncoder.encode(usuario.getPassword()));
        user.setFirstName(usuario.getFirstName());
        user.setLastName(usuario.getLastName()); // Fixed: was using firstName instead of lastName

        userRepository.save(user);

        Login login = new Login();
        login.setEmail(usuario.getEmail());
        login.setPassword(usuario.getPassword());

        return logar(login);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado com o ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}