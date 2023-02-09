package ru.scriptrid.vkpageobserver.service;

import ru.scriptrid.vkpageobserver.exceptions.PageAlreadyExists;
import ru.scriptrid.vkpageobserver.model.entity.ChangeEntity;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;
import ru.scriptrid.vkpageobserver.repository.ChangesRepository;
import ru.scriptrid.vkpageobserver.repository.PageRepository;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class PageService {
    private final ChangesRepository changesRepository;
    private final PageRepository pageRepository;
    private final VkApiService vkApiService;

    public PageService(PageRepository pageRepository, VkApiService vkApiService,
                       ChangesRepository changesRepository) {
        this.pageRepository = pageRepository;
        this.vkApiService = vkApiService;
        this.changesRepository = changesRepository;
    }

    @Transactional
    public PageEntity addPage(GetResponse response, UserEntity user) {
        if (pageRepository.existsById(response.getId())) {
            throw new PageAlreadyExists();
        }
        PageEntity page = toEntity(response, user);
        user.getObservingPages().add(page);
        return pageRepository.save(page);
    }

    public PageEntity getPage(Integer id) {
        return pageRepository.findById(id).orElseThrow();

    }

    public boolean pageExistsById(Integer id) {
        return pageRepository.existsById(id);
    }

    private PageEntity toEntity(GetResponse response) {
        PageEntity page = new PageEntity();
        page.setId(response.getId());
        page.setFirstName(response.getFirstName());
        page.setLastName(response.getLastName());
        if (response.getBdate() == null) {
            page.setBirthDate("unknown");
        } else {
            page.setBirthDate(response.getBdate());
        }
        if (response.getCity() == null) {
            page.setLocation("unknown");
        } else {
            page.setLocation(response.getCity().getTitle());
        }
        return page;
    }

    private PageEntity toEntity(GetResponse response, UserEntity user) {
        PageEntity page = toEntity(response);
        page.addUser(user);
        return page;
    }

    @Transactional
    public void deletePage(PageEntity page) {
        pageRepository.delete(page);
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

    public void updatePage(PageEntity currentPage, GetResponse requestedPage) {
        PageEntity actualPage = toEntity(requestedPage);
        if (!currentPage.getFirstName().equals(actualPage.getFirstName())) {
            changesRepository.save(createChangeEntity(currentPage, currentPage.getFirstName(), actualPage.getFirstName()));
            currentPage.setFirstName(actualPage.getFirstName());
        }
        if (!currentPage.getLastName().equals(actualPage.getLastName())) {
            changesRepository.save(createChangeEntity(currentPage, currentPage.getLastName(), actualPage.getLastName()));
            currentPage.setLastName(actualPage.getLastName());
        }
        if (!currentPage.getBirthDate().equals(actualPage.getBirthDate())) {
            changesRepository.save(createChangeEntity(currentPage, currentPage.getBirthDate(),
                    actualPage.getBirthDate()));
            currentPage.setBirthDate(actualPage.getBirthDate());
        }
        if (!currentPage.getLocation().equals(actualPage.getLocation())) {
            changesRepository.save(createChangeEntity(currentPage, currentPage.getLocation(), actualPage.getLocation()));
            currentPage.setLocation(actualPage.getLocation());
        }
    }

    private ChangeEntity createChangeEntity(PageEntity page, String before, String after) {
        ChangeEntity change = new ChangeEntity();
        change.setBefore(before);
        change.setAfter(after);
        change.setPage(page);
        change.setTimeOfChange(ZonedDateTime.now());
        log.info("New change before: {}, after: {}", before, after);
        return change;
    }
}
