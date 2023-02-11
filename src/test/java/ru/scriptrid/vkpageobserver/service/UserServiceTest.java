package ru.scriptrid.vkpageobserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import ru.scriptrid.vkpageobserver.exceptions.UsernameAlreadyExistsException;
import ru.scriptrid.vkpageobserver.model.dto.CreateUserDto;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    public UserServiceTest(UserService userService) {
        super(userService);
    }

    @Test
    void addUser() {
        CreateUserDto dto = new CreateUserDto("test", "12341234");
        userService.addUser(dto);
        UserDetails userDetails = userService.loadUserByUsername(dto.username());
        UserEntity userEntity = userService.getUser(userDetails);
        assertEquals(dto.username(), userEntity.getUsername());
    }

    @Test
    void throwsExceptionWhenUsernameAlreadyTaken() {
        CreateUserDto dto = new CreateUserDto("test", "12341234");
        userService.addUser(dto);
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.addUser(dto));
    }


}