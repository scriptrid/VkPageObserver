package ru.scriptrid.vkpageobserver.service;

import ru.scriptrid.vkpageobserver.exceptions.UsernameAlreadyExistsException;
import ru.scriptrid.vkpageobserver.model.UserDetailsImpl;
import ru.scriptrid.vkpageobserver.model.dto.CreateUserDto;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;
import ru.scriptrid.vkpageobserver.repository.UserRepository;
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

    public boolean userHasAPage(UserEntity user, PageEntity page) {
        return user.getObservingPages().contains(page);
    }
}
