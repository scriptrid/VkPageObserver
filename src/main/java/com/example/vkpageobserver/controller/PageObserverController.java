package com.example.vkpageobserver.controller;

import com.example.vkpageobserver.model.dto.ObservingPageDto;
import com.example.vkpageobserver.service.PageObserverService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/page")
public class PageObserverController {

    private final PageObserverService pageObserverService;

    public PageObserverController(PageObserverService pageObserverService) {
        this.pageObserverService = pageObserverService;
    }

    @PostMapping
    public void addPage(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Integer pageId) {
        pageObserverService.addPageToUser(userDetails, pageId);
    }

    @GetMapping
    public ObservingPageDto getPage(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Integer pageId) {
        return pageObserverService.getObservingPage(userDetails, pageId);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePage(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Integer pageId) {
        pageObserverService.deletePageFromUser(userDetails, pageId);
        return ResponseEntity.noContent().build();
    }
}
