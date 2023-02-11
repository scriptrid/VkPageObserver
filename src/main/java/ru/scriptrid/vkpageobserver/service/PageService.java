package ru.scriptrid.vkpageobserver.service;

import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.scriptrid.vkpageobserver.exceptions.PageAlreadyExistsException;
import ru.scriptrid.vkpageobserver.model.entity.PageEntity;
import ru.scriptrid.vkpageobserver.model.entity.UserEntity;
import ru.scriptrid.vkpageobserver.model.mapper.PageMapper;
import ru.scriptrid.vkpageobserver.repository.PageRepository;

@Service
@Slf4j
public class PageService {
    private final PageRepository pageRepository;
    private final PageMapper pageMapper;

    public PageService(PageRepository pageRepository, PageMapper pageMapper) {
        this.pageRepository = pageRepository;
        this.pageMapper = pageMapper;
    }

    @Transactional
    public PageEntity addPage(GetResponse response, UserEntity user) {
        if (pageRepository.existsById(response.getId())) {
            throw new PageAlreadyExistsException();
        }
        PageEntity page = pageMapper.toEntity(response, user);
        user.getObservingPages().add(page);
        return pageRepository.save(page);
    }

    public PageEntity getPage(Integer id) {
        return pageRepository.findById(id).orElseThrow();

    }

    public boolean pageExistsById(Integer id) {
        return pageRepository.existsById(id);
    }



    @Transactional
    public void deletePage(PageEntity page) {
        pageRepository.delete(page);
    }


}
