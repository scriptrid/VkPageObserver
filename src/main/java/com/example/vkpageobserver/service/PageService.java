package com.example.vkpageobserver.service;

import com.example.vkpageobserver.exсeptions.PageAlreadyExists;
import com.example.vkpageobserver.model.entity.ChangeEntity;
import com.example.vkpageobserver.model.entity.PageEntity;
import com.example.vkpageobserver.model.entity.UserEntity;
import com.example.vkpageobserver.repository.ChangesRepository;
import com.example.vkpageobserver.repository.PageRepository;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    public void addPage(GetResponse response, UserEntity user) {
        if (pageRepository.existsById(response.getId())) {
            throw new PageAlreadyExists();
        }
        PageEntity page = toEntity(response, user);
        pageRepository.save(page);
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
            page.setBirthDate(null);
        } else {
            page.setBirthDate(LocalDate.parse(response.getBdate(), DateTimeFormatter.ofPattern("dd.[]M.yyyy")));
        }
        if (response.getCity() == null) {
            page.setLocation("UNKNOWN");
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
    public void updatePages() {
        pageRepository.findAll().forEach(this::updatePage);
    }

    public void updatePage(PageEntity currentPage) {
        PageEntity actualPage = toEntity(vkApiService.requestPage(String.valueOf(currentPage.getId())));
        if (!currentPage.getFirstName().equals(actualPage.getFirstName())) {
            changesRepository.save(createEntity(currentPage, currentPage.getFirstName(), actualPage.getFirstName()));
            currentPage.setFirstName(actualPage.getFirstName());
        }
        if (!currentPage.getLastName().equals(actualPage.getLastName())) {
            changesRepository.save(createEntity(currentPage, currentPage.getLastName(), actualPage.getLastName()));
            currentPage.setLastName(actualPage.getLastName());
        }
        if (!currentPage.getBirthDate().isEqual(actualPage.getBirthDate())) {
            changesRepository.save(createEntity(currentPage, currentPage.getBirthDate().toString(),
                    actualPage.getBirthDate().toString()));
            currentPage.setBirthDate(actualPage.getBirthDate());
        }
        if (!currentPage.getLocation().equals(actualPage.getLocation())) {
            changesRepository.save(createEntity(currentPage, currentPage.getLocation(), actualPage.getLocation()));
            currentPage.setLocation(actualPage.getLocation());
        }
    }

    private ChangeEntity createEntity(PageEntity page, String before, String after) {
        ChangeEntity change = new ChangeEntity();
        change.setBefore(before);
        change.setAfter(after);
        change.setPage(page);
        change.setTimeOfChange(ZonedDateTime.now());
        log.info("New change before: {}, after: {}", before, after);
        return change;
    }

}
