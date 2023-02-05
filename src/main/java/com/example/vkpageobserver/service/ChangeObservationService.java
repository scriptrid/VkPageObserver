package com.example.vkpageobserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChangeObservationService {

    private final PageService pageService;

    public ChangeObservationService(PageService pageService) {
        this.pageService = pageService;
    }

    @Scheduled(fixedDelay = 10000)
    public void checkChanges() {
        log.info("Updating pages...");
            pageService.updatePages();
        }
}


