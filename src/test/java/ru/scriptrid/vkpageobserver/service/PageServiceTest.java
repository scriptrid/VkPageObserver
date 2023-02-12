package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.scriptrid.vkpageobserver.exceptions.PageAlreadyExistsException;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

class PageServiceTest extends BaseIntegrationTest {

    private final PageService pageService;


    @Autowired
    public PageServiceTest(PageService pageService, UserService userService) {
        super(userService);
        this.pageService = pageService;
    }

    @Test
    void addPage() {
        GetResponse response = getResponse();
        UserEntity user = getUser();
        pageService.addPage(response, user);
        PageEntity page = pageService.getPage(response.getId());
        assertEquals(response.getId(), page.getId());
        assertEquals(response.getFirstName(), page.getFirstName());
        assertEquals(response.getLastName(), page.getLastName());
        assertEquals(response.getBdate(), page.getBirthDate());
        assertEquals(response.getCity().getTitle(), page.getLocation());
    }

    @Test
    void addPageWithEmptyBDateAndCityToUser() {
        GetResponse response = getResponseWithEmptyBDateAndCity();
        UserEntity user = getUser();
        pageService.addPage(response, user);
        PageEntity page = pageService.getPage(response.getId());
        assertNull(page.getBirthDate());
        assertNull(page.getLocation());
    }

    @Test
    void throwsExceptionWhenPageAlreadyExists() {
        UserEntity user = getUser();
        pageService.addPage(getResponse(), user);
        assertThrows(PageAlreadyExistsException.class, () -> pageService.addPage(getResponse(), user));
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


}