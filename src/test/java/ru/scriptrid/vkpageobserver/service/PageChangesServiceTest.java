package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.base.City;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PageChangesServiceTest extends BaseIntegrationTest{


    private final PageService pageService;
    private final PageChangesService pageChangesService;


    @Autowired
    public PageChangesServiceTest(UserService userService, PageService pageService, PageChangesService pageChangesService) {
        super(userService);
        this.pageService = pageService;
        this.pageChangesService = pageChangesService;
    }

    @Test
    void updatePages() {
        GetResponse response = getResponse();
        PageEntity page = pageService.addPage(response, getUser());

        response.setFirstName("newFirstName");
        response.setLastName("newLastName");
        response.setBdate("01.01.1111");
        response.setCity(new City().setTitle("newCity"));

        pageChangesService.updatePage(page, response);
        assertEquals(page.getFirstName(), response.getFirstName());
    }
    @Test
    void updatePage() {
    }
}