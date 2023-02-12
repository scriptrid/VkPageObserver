package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.scriptrid.vkpageobserver.exceptions.PageAlreadyExistsException;
import ru.scriptrid.vkpageobserver.exceptions.PageNotFoundInVkException;
import ru.scriptrid.vkpageobserver.model.UserDetailsImpl;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ru.scriptrid.vkpageobserver.service.PageInteractionService.NON_EXISTENT_USERNAME;


class PageInteractionServiceTest extends BaseIntegrationTest {
    @MockBean
    private final VkApiService vkApiService;

    private final PageInteractionService pageInteractionService;

    @SpyBean
    private final PageService pageService;


    @Autowired
    public PageInteractionServiceTest(UserService userService, VkApiService vkApiService, PageInteractionService pageInteractionService, PageService pageService) {
        super(userService);
        this.vkApiService = vkApiService;
        this.pageInteractionService = pageInteractionService;
        this.pageService = pageService;
    }

    @Test
    void addPageToUserWhenPageNotExistsInDatabase() {
        UserEntity user = getUser();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        GetResponse response = getResponse();

        when(vkApiService.requestPage(anyString())).thenReturn(response);

        pageInteractionService.addPageToUser(userDetails, response.getId());

        verify(pageService).addPage(any(), any());

        PageEntity page = pageService.getPage(response.getId());

        assertTrue(pageInteractionService.userHasAPage(user, page));
    }

    @Test
    void addPageToUserWhenPageExistsInDatabase() {
        UserEntity user = getUser();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        GetResponse response = getResponse();

        when(vkApiService.requestPage(anyString())).thenReturn(response);

        pageInteractionService.addPageToUser(userDetails, response.getId());

        verify(pageService).addPage(any(), any());

        UserEntity otherUser = getUser("otherUser");
        UserDetailsImpl otherUserDetails = new UserDetailsImpl(otherUser);
        PageEntity page = pageService.getPage(response.getId());

        pageInteractionService.addPageToUser(otherUserDetails, page.getId());

        verify(pageService, times(1)).addPage(any(), any());
        assertTrue(pageInteractionService.userHasAPage(otherUser, page));
    }

    @Test
    void throwsPageAlreadyExistsException() {
        UserEntity user = getUser();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        GetResponse response = getResponse();

        when(vkApiService.requestPage(anyString())).thenReturn(response);

        pageInteractionService.addPageToUser(userDetails, response.getId());

        assertThrows(PageAlreadyExistsException.class, () -> pageInteractionService.addPageToUser(userDetails, response.getId()));
    }

    @Test
    void throwsPageNotFoundInVkException() {
        GetResponse response = new GetResponse();
        response.setFirstName(NON_EXISTENT_USERNAME);
        response.setId(111);
        UserEntity user = getUser();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        when(vkApiService.requestPage(anyString())).thenReturn(response);

        assertThrows(PageNotFoundInVkException.class, () -> pageInteractionService.addPageToUser(userDetails, response.getId()));
    }

    @Test
    void getObservingPage() {

    }

    @Test
    void deletePageFromUser() {
    }

    @Test
    void userHasAPage() {
    }


}