package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.base.City;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.scriptrid.vkpageobserver.model.UserDetailsImpl;
import ru.scriptrid.vkpageobserver.model.dto.ChangeDto;
import ru.scriptrid.vkpageobserver.model.dto.ObservingPageDto;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


class PageChangesServiceTest extends BaseIntegrationTest{

    private final PageChangesService pageChangesService;

    @MockBean
    private final VkApiService vkApiService;

    @MockBean
    private final Clock clock;
    private final PageInteractionService pageInteractionService;


    @Autowired
    public PageChangesServiceTest(UserService userService, PageChangesService pageChangesService,
                                  VkApiService vkApiService, Clock clock, PageInteractionService pageInteractionService) {
        super(userService);
        this.pageChangesService = pageChangesService;
        this.vkApiService = vkApiService;
        this.clock = clock;
        this.pageInteractionService = pageInteractionService;
    }

    @Test
    void updatePages() {
        GetResponse initialResponse = getResponse();
        UserEntity user = getUser();

        when(vkApiService.requestPage(any())).thenReturn(initialResponse);

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        pageInteractionService.addPageToUser(userDetails, initialResponse.getId());

        when(clock.getZone()).thenReturn(Clock.systemDefaultZone().getZone());
        when(clock.instant()).thenReturn(Instant.parse("2023-02-13T20:00:00.00Z"));
        ZonedDateTime dateTime = ZonedDateTime.now(clock);

        GetResponse newResponse = getResponse();
        newResponse.setFirstName("newFirstName");
        newResponse.setLastName("newLastName");

        when(vkApiService.requestPages(anyList())).thenReturn(List.of(newResponse));
        pageChangesService.updatePages();

        newResponse.setBdate("01.01.1111");
        newResponse.setCity(new City().setTitle("newCity"));

        when(clock.instant()).thenReturn(dateTime.plusSeconds(10).toInstant());
        when(vkApiService.requestPages(anyList())).thenReturn(List.of(newResponse));
        pageChangesService.updatePages();


        ObservingPageDto updatedPageDto = pageInteractionService.getObservingPage(userDetails ,newResponse.getId());
        List<ChangeDto> changes = updatedPageDto.changes();
        assertEquals(changes.get(0).timeOfChange(), dateTime.plusSeconds(10));
        assertEquals(changes.get(1).timeOfChange(), dateTime.plusSeconds(10));
        assertEquals(changes.get(2).timeOfChange(), dateTime);
        assertEquals(changes.get(3).timeOfChange(), dateTime);
        assertEquals(updatedPageDto.firstName(), newResponse.getFirstName());
    }
}