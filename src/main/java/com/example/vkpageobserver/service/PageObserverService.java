package com.example.vkpageobserver.service;

import com.example.vkpageobserver.exсeptions.PageAlreadyExists;
import com.example.vkpageobserver.exсeptions.PageNotFoundException;
import com.example.vkpageobserver.model.dto.ChangeDto;
import com.example.vkpageobserver.model.dto.ObservingPageDto;
import com.example.vkpageobserver.model.entity.PageEntity;
import com.example.vkpageobserver.model.entity.UserEntity;
import com.example.vkpageobserver.repository.PageRepository;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageObserverService {
    private final UserService userService;
    private final VkApiClient vk;
    private final ServiceActor serviceActor;
    private final List<Fields> searchingFields = List.of(
            Fields.FIRST_NAME_NOM,
            Fields.LAST_NAME_NOM,
            Fields.BDATE,
            Fields.CITY
    );
    private final PageService pageService;
    private final PageRepository pageRepository;

    public PageObserverService(UserService userService, VkApiClient vk, ServiceActor serviceActor,
                               PageService pageService,
                               PageRepository pageRepository) {
        this.userService = userService;
        this.vk = vk;
        this.serviceActor = serviceActor;
        this.pageService = pageService;
        this.pageRepository = pageRepository;
    }

    public void addPageToUser(UserDetails userDetails, @Valid Integer id) {
        UserEntity user = userService.getUser(userDetails);
        if (pageService.pageExistsById(id)) {
            PageEntity page = pageService.getPage(id);
            if (userService.userHasAPage(user, page)) {
                throw new PageAlreadyExists();
            }
            setAssociation(user, page);
        } else {
            GetResponse response = executeRequest(String.valueOf(id));
            if (response.getFirstName().equals("DELETED")) {
                throw new PageNotFoundException();
            }
            PageEntity page = pageService.toEntity(user, response);
            setAssociation(user, page);
            pageService.addPage(page);
        }
    }

    public ObservingPageDto getObservingPage(UserDetails userDetails, @Valid Integer id) {
        if (!pageRepository.existsById(id)) {
            throw new PageNotFoundException();
        }
        UserEntity user = userService.getUser(userDetails);
        PageEntity page = pageService.getPage(id);
        if (!userService.userHasAPage(user, page)) {
            throw new PageNotFoundException();
        }
        return fromPageEntityToDto(page);

    }

    public void deletePageFromUser(UserDetails userDetails, Integer id) {
        if (!isValidUserAndPage(userDetails, id)) {
            throw new PageNotFoundException();
        } else {
            userService
                    .getUser(userDetails)
                    .getObservingPages()
                    .remove(pageService.getPage(id));
        }
    }

    private boolean isValidUserAndPage(UserDetails userDetails, Integer id) {
        if (!pageRepository.existsById(id)) {
            return false;
        }
        UserEntity user = userService.getUser(userDetails);
        PageEntity page = pageService.getPage(id);
        if (!userService.userHasAPage(user, page)) {
            return false;
        }
        return true;
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
                        .collect(Collectors.toSet())
        );

    }

    private void setAssociation(UserEntity user, PageEntity page) {
        user.addPage(page);
        page.addUser(user);
    }

    private GetResponse executeRequest(String id) {
        try {
            return vk
                    .users()
                    .get(serviceActor)
                    .userIds(String.valueOf(id))
                    .fields(searchingFields)
                    .execute()
                    .get(0);
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

}
