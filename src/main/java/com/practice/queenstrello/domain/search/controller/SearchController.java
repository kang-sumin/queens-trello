package com.practice.queenstrello.domain.search.controller;

import com.practice.queenstrello.domain.search.dto.SearchResponse;
import com.practice.queenstrello.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    /**
     * 카드 통합검색
     * @param page 페이지 번호
     * @param size 각 페이지 당 카드 개수
     * @param title 검색할 제목(포함)
     * @param content 검색할 내용(포함)
     * @param deadline 마감날짜 (ex-2024-10-24, 정확)
     * @param nickname 담당자 닉네임(정확)
     * @return 해당 검색 조건이 맞는 카드 목록(페이징)
     */
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
