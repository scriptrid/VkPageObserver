package com.example.vkpageobserver.service;

import com.example.vkpageobserver.model.entity.PageEntity;
import com.example.vkpageobserver.model.entity.UserEntity;
import com.example.vkpageobserver.repository.PageRepository;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PageService {
    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Transactional
    public void addPage(PageEntity page) {
        if (pageRepository.existsById(page.getId())) {

        }
        pageRepository.save(page);
    }

    public boolean pageExistsById(Integer id) {
        return pageRepository.existsById(id);
    }

    public PageEntity getPage(Integer id) {
        return pageRepository.getReferenceById(id);

    }

    public PageEntity toEntity(UserEntity user, GetResponse response) {
        PageEntity page = new PageEntity();
        page.setId(response.getId());
        page.setFirstName(response.getFirstName());
        page.setLastName(response.getLastName());
        page.setBirthDate(LocalDate.parse(response.getBdate(), DateTimeFormatter.ofPattern("dd.[]M.yyyy")));
        page.setLocation(response.getCity().getTitle());
        page.getUsers().add(user);
        return page;
    }
}
