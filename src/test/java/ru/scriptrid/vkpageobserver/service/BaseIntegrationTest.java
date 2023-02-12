package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.base.City;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.scriptrid.vkpageobserver.model.dto.CreateUserDto;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BaseIntegrationTest {
    protected final UserService userService;

    protected BaseIntegrationTest(UserService userService) {
        this.userService = userService;
    }

    protected GetResponse getResponse() {
        GetResponse response = new GetResponse();

        response.setId(1111111);
        response.setFirstName("testFirstName");
        response.setLastName("testLastName");
        response.setBdate("01.01.1970");
        response.setCity(new City().setTitle("testCity"));

        return response;
    }

    protected GetResponse getResponseWithEmptyBDateAndCity() {
        GetResponse response = new GetResponse();
        response.setId(1111111);
        response.setFirstName("testFirstName");
        response.setLastName("testLastName");
        return response;
    }

    protected UserEntity getUser(String username) {
        return userService.addUser(new CreateUserDto(username, "11111111"));
    }

    protected UserEntity getUser() {
        return getUser("test");
    }
}
