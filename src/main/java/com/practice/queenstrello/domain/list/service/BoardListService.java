package com.practice.queenstrello.domain.list.service;

import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.list.dto.request.BoardListSaveRequest;
import com.practice.queenstrello.domain.list.dto.request.BoardListUpdateRequest;
import com.practice.queenstrello.domain.list.dto.response.BoardListSaveResponse;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.repository.BoardListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;

@Service
@RequiredArgsConstructor
public class BoardListService {
    private final BoardListRepository boardListRepository;

    public BoardListSaveResponse savedBoardList(BoardListSaveRequest boardListSaveRequest, Board board, Member member) {
        validateWritePermission(member);

        BoardList boardList = new BoardList(
                boardListSaveRequest.getTitle(),
                boardListSaveRequest.getOrder(),
                board
        );
        BoardList savedBoardList = boardListRepository.save(boardList);
        return BoardListSaveResponse.of(savedBoardList);
    }

    @Transactional
    public BoardListSaveResponse updateBoardList(long boardListId, BoardListUpdateRequest boardListUpdateRequest) {
        validateWritePermission(member);

        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(() -> new IllegalArgumentException("리스트가 없습니다."));

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
        validateWritePermission(member);

        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(() -> new IllegalArgumentException("리스트가 없습니다."));
        boardListRepository.delete(boardList);

    }

    //쓰기 권한 검증
    private void validateWritePermission(Member member) {
        if (member.getRole() == Role.READ_ONLY) {
            throw new PermissionDeniedException("읽기 전용 멤버는 이 작업을 수행할 수 없습니다.");

        }
    }
}
