package ru.scriptrid.vkpageobserver.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.scriptrid.vkpageobserver.service.PageChangesService;

@Component
@Slf4j
public class ChangeObservationScheduler {

    private final PageChangesService pageChangesService;

    public ChangeObservationScheduler(PageChangesService pageChangesService) {
        this.pageChangesService = pageChangesService;
    }

    @Scheduled(fixedDelayString = "${observer.delay}", initialDelayString = "${observer.initialDelay}")
    public void checkChanges() {
        log.info("Updating pages...");
        pageChangesService.updatePages();
    }
}


