package com.example.vkpageobserver.service;

import com.example.vkpageobserver.exсeptions.PageAlreadyExists;
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
    public void addPage(GetResponse response, UserEntity user) {
        if (pageRepository.existsById(response.getId())) {
            throw new PageAlreadyExists();
        }
        PageEntity page = toEntity(response, user);
        pageRepository.save(page);
    }

    private PageEntity toEntity(GetResponse response, UserEntity user) {
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
        page.addUser(user);
        return page;
    }

    public PageEntity getPage(Integer id) {
        return pageRepository.findById(id).orElseThrow();

    }

    public boolean pageExistsById(Integer id) {
        return pageRepository.existsById(id);
    }
}
