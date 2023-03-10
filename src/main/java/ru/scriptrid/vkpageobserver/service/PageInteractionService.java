package ru.scriptrid.vkpageobserver.service;

import ru.scriptrid.vkpageobserver.exceptions.UserDoesNotHaveAPageException;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.scriptrid.vkpageobserver.exceptions.PageAlreadyExistsException;
import ru.scriptrid.vkpageobserver.exceptions.PageNotFoundInDatabaseException;
import ru.scriptrid.vkpageobserver.exceptions.PageNotFoundInVkException;
import ru.scriptrid.vkpageobserver.model.dto.ObservingPageDto;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;
import ru.scriptrid.vkpageobserver.model.mapper.PageMapper;

@Service
public class PageInteractionService {
    public static final String NON_EXISTENT_USERNAME = "DELETED";

    private final UserService userService;

    private final PageService pageService;
    private final VkApiService vkApiService;
    private final PageMapper pageMapper;


    public PageInteractionService(UserService userService,
                                  PageService pageService, VkApiService vkApiService, PageMapper pageMapper) {
        this.userService = userService;
        this.pageService = pageService;
        this.vkApiService = vkApiService;
        this.pageMapper = pageMapper;
    }

    @Transactional
    public void addPageToUser(UserDetails userDetails, Integer pageId) {
        UserEntity user = userService.getUser(userDetails);
        if (pageService.pageExistsById(pageId)) {
            PageEntity page = pageService.getPage(pageId);
            if (userHasAPage(user, page)) {
                throw new PageAlreadyExistsException();
            }
            user.getObservingPages().add(page);
            page.addUser(user);
        } else {
            GetResponse response = vkApiService.requestPage(String.valueOf(pageId));
            if (response.getFirstName().equals(NON_EXISTENT_USERNAME)) {
                throw new PageNotFoundInVkException();
            }
            pageService.addPage(response, user);
        }
    }

    public ObservingPageDto getObservingPage(UserDetails userDetails, Integer pageId) {
        if (!pageService.pageExistsById(pageId)) {
            throw new PageNotFoundInDatabaseException();
        }
        UserEntity user = userService.getUser(userDetails);
        PageEntity page = pageService.getPage(pageId);
        if (!userHasAPage(user, page)) {
            throw new UserDoesNotHaveAPageException();
        }
        return pageMapper.toDto(page);
    }
    @Transactional
    public void deletePageFromUser(UserDetails userDetails, Integer pageId) {
        if (!isValidUserAndPage(userDetails, pageId)) {
            throw new UserDoesNotHaveAPageException();
        } else {
            UserEntity user = userService.getUser(userDetails);
            PageEntity page = pageService.getPage(pageId);
            page.getUsers().remove(user);
            user.getObservingPages().remove(page);
            if (page.getUsers().isEmpty()) {
                pageService.deletePage(page);
            }
        }

    }

    private boolean isValidUserAndPage(UserDetails userDetails, Integer pageId) {
        if (!pageService.pageExistsById(pageId)) {
            throw new PageNotFoundInDatabaseException();
        }
        UserEntity user = userService.getUser(userDetails);
        PageEntity page = pageService.getPage(pageId);
        return userHasAPage(user, page);
    }


    public boolean userHasAPage(UserEntity user, PageEntity page) {
        return user.getObservingPages().contains(page);
    }

}
