package com.example.vkpageobserver.service;

import com.example.vkpageobserver.exeptions.UsernameAlreadyExistsException;
import com.example.vkpageobserver.model.UserDetailsImpl;
import com.example.vkpageobserver.model.dto.CreateUserDto;
import com.example.vkpageobserver.model.entity.UserEntity;
import com.example.vkpageobserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return new UserDetailsImpl(entity);
    }

    @Transactional
    public void addUser(CreateUserDto dto) {
        if (userRepository.existsByUsernameIgnoreCase(dto.username())) {
            throw new UsernameAlreadyExistsException();
        }
        userRepository.save(toEntity(dto));
    }


    public UserEntity getUser(UserDetails details) {
        return userRepository.findByUsername(details.getUsername()).orElseThrow();
    }

    private UserEntity toEntity(CreateUserDto dto) {
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.username());
        entity.setPassword(passwordEncoder.encode(dto.password()));
        return entity;
    }
}
