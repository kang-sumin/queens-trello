package com.practice.queenstrello.domain.list.service;

import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.list.dto.request.BoardListSaveRequest;
import com.practice.queenstrello.domain.list.dto.request.BoardListUpdateRequest;
import com.practice.queenstrello.domain.list.dto.response.BoardListSaveResponse;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.repository.BoardListRepository;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.workspace.entity.WorkspaceMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

//저장할 때나 순서 바뀔때 정렬 생각해야된다. Validation 이용할 것
public class BoardListService {
    private final BoardListRepository boardListRepository;

    public BoardListSaveResponse savedBoardList(BoardListSaveRequest boardListSaveRequest, WorkspaceMember member) {
        validateWritePermission(member);
        BoardList boardList = new BoardList(
                boardListSaveRequest.getTitle(),
                boardListSaveRequest.getOrder()
        );
        BoardList savedBoardList = boardListRepository.save(boardList);
        return BoardListSaveResponse.of(savedBoardList);
    }

    @Transactional
    public BoardListSaveResponse updateBoardList(long boardListId, BoardListUpdateRequest boardListUpdateRequest, WorkspaceMember member) {
        validateWritePermission(member);
        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARDLIST_NOT_FOUND));
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
    //삭제를 했으면 뒤에 있던 애 처리 로직 필요
    public void deleteBoardList(long boardListId, WorkspaceMember member) {
        validateWritePermission(member);

        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARDLIST_NOT_FOUND));
        boardListRepository.delete(boardList);

    }

    //쓰기 권한 검증 -> 다 필요!
    private void validateWritePermission(WorkspaceMember member) {
        if (member.getRole() == Role.READ_ONLY) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_READ);

        }
    }
}
