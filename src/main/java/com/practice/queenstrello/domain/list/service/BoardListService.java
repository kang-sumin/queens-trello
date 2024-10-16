package com.practice.queenstrello.domain.list.service;

import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.list.dto.request.BoardListSaveRequest;
import com.practice.queenstrello.domain.list.dto.request.BoardListUpdateRequest;
import com.practice.queenstrello.domain.list.dto.response.BoardListSaveResponse;
import com.practice.queenstrello.domain.list.entity.BoardList;
import com.practice.queenstrello.domain.list.repository.BoardListRepository;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.entity.WorkspaceMember;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

//저장할 때나 순서 바뀔때 정렬 생각해야된다. Validation 이용할 것
public class BoardListService{
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
    public void deleteBoardList(long boardListId, WorkspaceMember member) {
        validateWritePermission(member);

        BoardList boardList = boardListRepository.findById(boardListId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARDLIST_NOT_FOUND));
        //보드 리스트 삭제
        boardListRepository.delete(boardList);
        //권한 검증
        Long boardId = boardList.getBoard().getId();
        int deletedOrder = boardList.getOrder();

        //순서가 큰 boardList 가져오기
        List<BoardList> listsToUpdate = boardListRepository.findByBoardIdAndOrderGreaterThan(boardId, deletedOrder);

        //order값 하나씩 감소시키기
        for (BoardList list : listsToUpdate) {
            list.setOrder(list.getOrder() - 1);
        }

        //변경된 BoardList 저장
        boardListRepository.saveAll(listsToUpdate);
    }

    //쓰기 권한 검증 -> 다 필요!
    private void validateWritePermission(WorkspaceMember member) {
        if (member == null || member.getMemberRole() == MemberRole.READ) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_READ);

        }
    }
}

