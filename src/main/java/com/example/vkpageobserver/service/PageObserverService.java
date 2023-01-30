package com.example.vkpageobserver.service;

import com.example.vkpageobserver.exсeptions.PageNotFoundException;
import com.example.vkpageobserver.model.entity.PageEntity;
import com.example.vkpageobserver.model.entity.UserEntity;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageObserverService {
    private final UserService userService;
    private final VkApiClient vk;
    private final ServiceActor serviceActor;
    private final List<Fields> searchingFields = List.of(Fields.FIRST_NAME_NOM, Fields.LAST_NAME_NOM, Fields.BDATE, Fields.CITY);
    private final PageService pageService;

    public PageObserverService(UserService userService, VkApiClient vk, ServiceActor serviceActor,
                               PageService pageService) {
        this.userService = userService;
        this.vk = vk;
        this.serviceActor = serviceActor;
        this.pageService = pageService;
    }

    public void addPageToUser(UserDetails userDetails, Integer id) {
        UserEntity user = userService.getUser(userDetails);
        if (pageService.pageExistsById(id)) {
            user.getObservingPages().add(pageService.getPage(id));
            pageService.getPage(id).getUsers().add(user);
        } else {
            GetResponse response = getSearchResponse(String.valueOf(id));
            if (response.getFirstName().equals("DELETED")) {
                throw new PageNotFoundException();
            }
            PageEntity page = pageService.toEntity(user, response);
            user.getObservingPages().add(pageService.getPage(id));
            pageService.getPage(id).getUsers().add(user);
            pageService.addPage(page);
        }
    }

    private GetResponse getSearchResponse(String id) {
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
