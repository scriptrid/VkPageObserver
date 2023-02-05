package com.example.vkpageobserver.controller;

import com.example.vkpageobserver.model.dto.ObservingPageDto;
import com.example.vkpageobserver.service.PageInteractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/page")
public class PageObserverController {

    private final PageInteractionService pageInteractionService;

    public PageObserverController(PageInteractionService pageInteractionService) {
        this.pageInteractionService = pageInteractionService;
    }

    @PostMapping
    public void addPage(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Integer pageId) {
        pageInteractionService.addPageToUser(userDetails, pageId);
    }

    @GetMapping
    public ObservingPageDto getPage(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Integer pageId) {
        return pageInteractionService.getObservingPage(userDetails, pageId);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePage(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Integer pageId) {
        pageInteractionService.deletePageFromUser(userDetails, pageId);
        return ResponseEntity.noContent().build();
    }
}
