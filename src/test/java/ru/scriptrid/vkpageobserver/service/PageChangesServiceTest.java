package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.base.City;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


class PageChangesServiceTest extends BaseIntegrationTest{


    private final PageService pageService;
    private final PageChangesService pageChangesService;

    @MockBean
    private final VkApiService vkApiService;


    @Autowired
    public PageChangesServiceTest(UserService userService, PageService pageService, PageChangesService pageChangesService, VkApiService vkApiService) {
        super(userService);
        this.pageService = pageService;
        this.pageChangesService = pageChangesService;
        this.vkApiService = vkApiService;
    }

    @Test
    void updatePages() {
        GetResponse initialResponse = getResponse();
        PageEntity page = pageService.addPage(initialResponse, getUser());

        GetResponse newResponse = getResponse();
        newResponse.setFirstName("newFirstName");
        newResponse.setLastName("newLastName");
        newResponse.setBdate("01.01.1111");
        newResponse.setCity(new City().setTitle("newCity"));

        when(vkApiService.requestPages(anyList())).thenReturn(List.of(newResponse));

        pageChangesService.updatePages();
        assertEquals(page.getFirstName(), newResponse.getFirstName());
    }
}