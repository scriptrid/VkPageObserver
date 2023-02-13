package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.scriptrid.vkpageobserver.model.entity.ChangeEntity;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.mapper.PageMapper;
import ru.scriptrid.vkpageobserver.repository.ChangesRepository;
import ru.scriptrid.vkpageobserver.repository.PageRepository;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class PageChangesService {

    private final PageRepository pageRepository;
    private final VkApiService vkApiService;
    private final ChangesRepository changesRepository;
    private final PageMapper pageMapper;
    private final Clock clock;

    public PageChangesService(PageRepository pageRepository, VkApiService vkApiService, ChangesRepository changesRepository, PageMapper pageMapper, Clock clock) {
        this.pageRepository = pageRepository;
        this.vkApiService = vkApiService;
        this.changesRepository = changesRepository;
        this.pageMapper = pageMapper;
        this.clock = clock;
    }

    @Transactional
    public void updatePages() {
        List<PageEntity> pages = pageRepository.findAll();
        List<String> ids = pages
                .stream()
                .map(page -> String.valueOf(page.getId()))
                .toList();
        List<GetResponse> requestedPages = vkApiService.requestPages(ids);
        for (int i = 0; i < pages.size(); i++) {
            updatePage(pages.get(i), requestedPages.get(i));
        }

    }

    private void updatePage(PageEntity currentPage, GetResponse requestedPage) {
        ZonedDateTime dateTime = ZonedDateTime.now(clock);
        PageEntity actualPage = pageMapper.toEntity(requestedPage);
        if (!currentPage.getFirstName().equals(actualPage.getFirstName())) {
            changesRepository.save(createChangeEntity(currentPage, currentPage.getFirstName(), actualPage.getFirstName(),
                    dateTime));
            currentPage.setFirstName(actualPage.getFirstName());
        }
        if (!currentPage.getLastName().equals(actualPage.getLastName())) {
            changesRepository.save(createChangeEntity(currentPage, currentPage.getLastName(), actualPage.getLastName(),
                    dateTime));
            currentPage.setLastName(actualPage.getLastName());
        }
        if (!currentPage.getBirthDate().equals(actualPage.getBirthDate())) {
            changesRepository.save(createChangeEntity(currentPage, currentPage.getBirthDate(),
                    actualPage.getBirthDate(), dateTime));
            currentPage.setBirthDate(actualPage.getBirthDate());
        }
        if (!currentPage.getLocation().equals(actualPage.getLocation())) {
            changesRepository.save(createChangeEntity(currentPage, currentPage.getLocation(), actualPage.getLocation(),
                    dateTime));
            currentPage.setLocation(actualPage.getLocation());
        }
    }

    private ChangeEntity createChangeEntity(PageEntity page, String before, String after, ZonedDateTime dateTime) {
        ChangeEntity change = new ChangeEntity();
        change.setBefore(before);
        change.setAfter(after);
        change.setPage(page);
        change.setTimeOfChange(dateTime);
        page.getChanges().add(change);
        log.info("New change: before: {}, after: {}", before, after);
        return change;
    }
}
