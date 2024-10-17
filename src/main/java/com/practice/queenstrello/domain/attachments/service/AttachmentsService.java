package com.practice.queenstrello.domain.attachments.service;

import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.card.repository.CardRepository;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.common.service.S3Service;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class AttachmentsService {

    private final CardRepository cardRepository;

    private final S3Service s3Service;

    public String uploadAttachmentLinkToCard(MultipartFile file) {

        // S3에 파일 업로드 후 URL 반환
        return s3Service.uploadFile(file);


    }
}

