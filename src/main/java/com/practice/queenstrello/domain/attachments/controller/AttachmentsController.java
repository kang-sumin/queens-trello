package com.practice.queenstrello.domain.attachments.controller;

import com.practice.queenstrello.domain.attachments.service.AttachmentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping
public class AttachmentsController {

    private final AttachmentsService attachmentsService;

    public AttachmentsController(AttachmentsService attachmentsService) {
        this.attachmentsService = attachmentsService;
    }

    // 첨부파일 업로드
    @PutMapping("/attachments/image")
    public ResponseEntity<String> uploadAttachment(
            @RequestPart("file") MultipartFile file) { // 파일 첨부

        String fileUrl = attachmentsService.uploadAttachmentLinkToCard(file); //
        return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);
    }
}