package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.base.City;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.scriptrid.vkpageobserver.model.dto.CreateUserDto;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PageServiceTest {

    private final UserService userService;
    private final PageService pageService;
    private final PageInteractionService pageInteractionService;


    @Autowired
    public PageServiceTest(PageService pageService, UserService userService, PageInteractionService pageInteractionService) {
        this.pageService = pageService;
        this.userService = userService;
        this.pageInteractionService = pageInteractionService;
    }

    @Test
    void addPage() {
        GetResponse response = getResponse();
        UserEntity user = getUser();
        pageService.addPage(response, user);
        PageEntity page = pageService.getPage(response.getId());
        assertTrue(pageInteractionService.userHasAPage(user, page));
    }


    @Test
    void pageExistsById() {
        pageService.addPage(getResponse(), getUser());
        assertTrue(pageService.pageExistsById(getResponse().getId()));
    }

    @Test
    void deletePage() {
        PageEntity page = pageService.addPage(getResponse(), getUser());
        pageService.deletePage(page);
        assertFalse(pageService.pageExistsById(page.getId()));
    }

    @Test
    void updatePages() {
        GetResponse response = getResponse();
        PageEntity page = pageService.addPage(response, getUser());

        response.setFirstName("newFirstName");
        response.setLastName("newLastName");
        response.setBdate("01.01.1111");
        response.setCity(new City().setTitle("newCity"));

        pageService.updatePage(page, response);
        assertEquals(page.getFirstName(), response.getFirstName());
    }

    private GetResponse getResponse() {
        GetResponse response = new GetResponse();

        response.setId(1111111);
        response.setFirstName("testFirstName");
        response.setLastName("testLastName");
        response.setBdate("01.01.1970");
        response.setCity(new City().setTitle("testCity"));

        return response;
    }

    private UserEntity getUser() {
        return userService.addUser(new CreateUserDto("test","11111111"));
    }
}