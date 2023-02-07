package com.example.vkpageobserver.scheduler;

import com.example.vkpageobserver.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChangeObservationScheduler {

    private final PageService pageService;

    public ChangeObservationScheduler(PageService pageService) {
        this.pageService = pageService;
    }

    @Scheduled(fixedDelayString = "${observer.delay}", initialDelayString = "${observer.initialDelay}")
    public void checkChanges() {
        log.info("Updating pages...");
        pageService.updatePages();
    }
}


