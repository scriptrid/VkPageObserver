package ru.scriptrid.vkpageobserver.controller;

import ru.scriptrid.vkpageobserver.model.dto.ObservingPageDto;
import ru.scriptrid.vkpageobserver.service.PageInteractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/page/{pageId}")
public class PageInteractionController {

    private final PageInteractionService pageInteractionService;

    public PageInteractionController(PageInteractionService pageInteractionService) {
        this.pageInteractionService = pageInteractionService;
    }

    @PostMapping
    public void addPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer pageId) {
        pageInteractionService.addPageToUser(userDetails, pageId);
    }

    @GetMapping
    public ObservingPageDto getPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer pageId) {
        return pageInteractionService.getObservingPage(userDetails, pageId);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer pageId) {
        pageInteractionService.deletePageFromUser(userDetails, pageId);
        return ResponseEntity.noContent().build();
    }
}
