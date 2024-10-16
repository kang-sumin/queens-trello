package com.practice.queenstrello.domain.list.service;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.board.repository.BoardRepository;
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
import com.practice.queenstrello.domain.workspace.repository.WorkspaceMemberRepository;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

//저장할 때나 순서 바뀔때 정렬 생각해야된다. Validation 이용할 것
public class BoardListService{
    private final BoardListRepository boardListRepository;
    private final BoardRepository boardRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    public BoardListSaveResponse savedBoardList(BoardListSaveRequest boardListSaveRequest, AuthUser authUser, Long boardId) {

        //현재 로그인 한 사용자 정보 가져오고 그 사용자의 권한(ADMIN, MASTER, USER)확인 User객체로서 받아온다.
        User user = User.fromAuthUser(authUser);

        //리스트를 추가할 보드가 존재하는지 확인
        //존재하면 존재한 보드 객체를 검색해서 받아야 하고 없으면 없다는 예외를 발생해야함
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));

        //워크스페이스의 아이디와 현재 접속해있는 사용자 아이디로 현재 사용자의 워크스페이스 멤버 권한을 확인할수있다
        //보드가 존재하면 해당 보드가 속해있는 워크스페이스의 아이디 값을 받아와서 그 아이디로 멤버가 멤버의 역할을 조회할수있음
        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(user.getId(), board.getWorkspace().getId())
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND));

        //워크스페이스 멤버 권한이 READ가 아니면 다 할수있으므로 권한이 READ인것만 예외처리
        if (workspaceMember.getMemberRole().equals(MemberRole.READ)) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_READ);
        }

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
        if (member.getMemberRole() == MemberRole.READ) {
            throw new QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_READ);

        }
    }
}

