package com.practice.queenstrello.domain.list.service;

import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.list.dto.request.BoardListSaveRequest;
import com.practice.queenstrello.domain.list.dto.request.BoardListUpdateRequest;
import com.practice.queenstrello.domain.list.dto.response.BoardListSaveResponse;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.repository.BoardListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardListService {
    private final BoardListRepository boardListRepository;

    public BoardListSaveResponse savedBoardList(BoardListSaveRequest boardListSaveRequest) {
        BoardList boardList = new BoardList(
                boardListSaveRequest.getTitle(),
                boardListSaveRequest.getOrder()
        );
        BoardList savedBoardList = boardListRepository.save(boardList);
        return BoardListSaveResponse.of(savedBoardList);
    }
    @Transactional
    public BoardListSaveResponse updateBoardList(long boardListId, BoardListUpdateRequest boardListUpdateRequest) {
        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.LIST_NOT_FOUND));
        if (boardListUpdateRequest.getTitle() != null) {
            boardList.changeTitle(boardListUpdateRequest.getTitle());
        }
        if (boardListUpdateRequest.getOrder() != null) {
            boardList.changeOrder(boardListUpdateRequest.getOrder());
        }
        BoardList updateBoardList = boardListRepository.save(boardList);
        return BoardListSaveResponse.of(updateBoardList);
    }
    @Transactional
    public void deleteBoardList(long boardListId) {
        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.LIST_NOT_FOUND));
        boardListRepository.delete(boardList);

    }
}
