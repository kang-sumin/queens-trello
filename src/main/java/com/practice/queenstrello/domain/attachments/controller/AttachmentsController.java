package com.practice.queenstrello.domain.attachments.controller;

import com.practice.queenstrello.domain.attachments.service.AttachmentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/attachments")
public class AttachmentsController {

    private final AttachmentsService attachmentsService;

    public AttachmentsController(AttachmentsService attachmentsService) {
        this.attachmentsService = attachmentsService;
    }

    // 첨부파일 업로드
    @PutMapping("/image")
    public ResponseEntity<Void> uploadAttachment(
            @RequestPart("cardId") Long cardId,
            @RequestPart("file") MultipartFile file) { // 파일 첨부

        attachmentsService.uploadAttachmentLinkToCard(file, cardId); // 카드에 파일 링크 추가
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}