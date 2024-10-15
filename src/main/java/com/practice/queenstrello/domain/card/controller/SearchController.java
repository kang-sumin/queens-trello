package com.practice.queenstrello.domain.card.controller;

import com.practice.queenstrello.domain.card.dto.SearchResponse;
import com.practice.queenstrello.domain.card.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search")
    public Page<SearchResponse> searchCard (
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue ="10")  int size,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) String content,
                                    @RequestParam(required = false) String deadline,
                                    @RequestParam(required = false) String nickname
                                      ) {
        return searchService.searchCard(page, size, title, content ,deadline, nickname);
    }
}
