package com.example.vkpageobserver.controller;

import com.example.vkpageobserver.service.PageObserverService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/observer")
public class PageObserverController {

    private final PageObserverService pageObserverService;

    public PageObserverController(PageObserverService pageObserverService) {
        this.pageObserverService = pageObserverService;
    }

    @PostMapping("/addPage")
    public ResponseEntity<Void> addPage(@AuthenticationPrincipal UserDetails userDetails, @Valid @NumberFormat @RequestParam String id) {
        pageObserverService.addPageToUser(userDetails,Integer.valueOf(id));
        return ResponseEntity.ok().build();
    }
}
