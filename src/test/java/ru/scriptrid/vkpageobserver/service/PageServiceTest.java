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
    private final PageInteractionService pageInteractionService;


    @Autowired
    public PageServiceTest(PageService pageService, UserService userService, PageInteractionService pageInteractionService) {
        super(userService);
        this.pageService = pageService;
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
    void addPageWithEmptyBDateAndCity() {
        GetResponse response = getResponseWithEmptyBDateAndCity();
        UserEntity user = getUser();
        pageService.addPage(response, user);
        PageEntity page = pageService.getPage(response.getId());
        assertTrue(pageInteractionService.userHasAPage(user, page));
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