package com.example.vkpageobserver.controller;

import com.example.vkpageobserver.model.dto.ObservingPageDto;
import com.example.vkpageobserver.service.PageObserverService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/observer")
public class PageObserverController {

    private final PageObserverService pageObserverService;

    public PageObserverController(PageObserverService pageObserverService) {
        this.pageObserverService = pageObserverService;
    }

    @PostMapping("/addPage")
    public ResponseEntity<Void> addPage(@AuthenticationPrincipal UserDetails userDetails, @Valid @NumberFormat @RequestParam Integer id) {
        pageObserverService.addPageToUser(userDetails, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getPage")
    public ObservingPageDto getPage(@AuthenticationPrincipal UserDetails userDetails, @Valid @NumberFormat @RequestParam Integer id) {
        return pageObserverService.getObservingPage(userDetails, id);
    }

    @DeleteMapping("/deletePage")
    public ResponseEntity<Void> deletePage(@AuthenticationPrincipal UserDetails userDetails, @Valid @NumberFormat @RequestParam Integer id) {
        pageObserverService.deletePageFromUser(userDetails, id);
        return ResponseEntity.noContent().build();
    }
}
