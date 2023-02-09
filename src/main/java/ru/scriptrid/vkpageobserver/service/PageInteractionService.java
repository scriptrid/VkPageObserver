package ru.scriptrid.vkpageobserver.service;

import ru.scriptrid.vkpageobserver.exceptions.PageAlreadyExists;
import ru.scriptrid.vkpageobserver.exceptions.PageNotFoundException;
import ru.scriptrid.vkpageobserver.model.dto.ChangeDto;
import ru.scriptrid.vkpageobserver.model.dto.ObservingPageDto;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

@Service
public class PageInteractionService {
    private static final String NON_EXISTENT_USERNAME = "DELETED";

    private final UserService userService;

    private final PageService pageService;
    private final VkApiService vkApiService;


    public PageInteractionService(UserService userService,
                                  PageService pageService, VkApiService vkApiService) {
        this.userService = userService;
        this.pageService = pageService;
        this.vkApiService = vkApiService;
    }

    @Transactional
    public void addPageToUser(UserDetails userDetails, Integer pageId) {
        UserEntity user = userService.getUser(userDetails);
        if (pageService.pageExistsById(pageId)) {
            PageEntity page = pageService.getPage(pageId);
            if (userHasAPage(user, page)) {
                throw new PageAlreadyExists();
            }
            user.getObservingPages().add(page);
            page.addUser(user);
        } else {
            GetResponse response = vkApiService.requestPage(String.valueOf(pageId));
            if (response.getFirstName().equals(NON_EXISTENT_USERNAME)) {
                throw new PageNotFoundException();
            }
            pageService.addPage(response, user);
        }
    }

    public ObservingPageDto getObservingPage(UserDetails userDetails, Integer pageId) {
        if (!pageService.pageExistsById(pageId)) {
            throw new PageNotFoundException();
        }
        UserEntity user = userService.getUser(userDetails);
        PageEntity page = pageService.getPage(pageId);
        if (!userHasAPage(user, page)) {
            throw new PageNotFoundException();
        }
        return fromPageEntityToDto(page);

    }
    @Transactional
    public void deletePageFromUser(UserDetails userDetails, Integer pageId) {
        if (!isValidUserAndPage(userDetails, pageId)) {
            throw new PageNotFoundException();
        } else {
            UserEntity user = userService.getUser(userDetails);
            PageEntity page = pageService.getPage(pageId);
            page.getUsers().remove(user);
            if (page.getUsers().isEmpty()) {
                pageService.deletePage(page);
            }
        }

    }

    private boolean isValidUserAndPage(UserDetails userDetails, Integer pageId) {
        if (!pageService.pageExistsById(pageId)) {
            return false;
        }
        UserEntity user = userService.getUser(userDetails);
        PageEntity page = pageService.getPage(pageId);
        return userHasAPage(user, page);
    }

    private ObservingPageDto fromPageEntityToDto(PageEntity page) {
        return new ObservingPageDto(
                page.getFirstName(),
                page.getLastName(),
                page.getBirthDate(),
                page.getLocation(),
                page.getChanges()
                        .stream()
                        .map(c -> new ChangeDto(
                                c.getTimeOfChange(),
                                c.getBefore(),
                                c.getAfter()
                        ))
                        .sorted(Comparator.comparing(ChangeDto::timeOfChange).reversed())
                        .toList()
        );

    }

    public boolean userHasAPage(UserEntity user, PageEntity page) {
        return user.getObservingPages().contains(page);
    }

}
