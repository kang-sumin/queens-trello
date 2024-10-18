package com.practice.queenstrello.domain.board.service;

import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.board.repository.BoardRepository;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.common.service.S3Service;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import com.practice.queenstrello.domain.workspace.entity.WorkspaceMember;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceMemberRepository;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class BoardService {
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final S3Service s3Service;

    @Transactional
    public BoardSaveResponse savedBoard(Long workspaceId, BoardSaveRequest boardSaveRequest, AuthUser authUser) {
        //현재 로그인 한 사용자 정보 가져오고 그 사용자의 권한(ADMIN, MASTER, USER)확인 User객체로서 받아온다.
        User user = User.fromAuthUser(authUser);
        //리스트를 추가할 보드가 존재하는지 확인
        //존재하면 존재한 보드 객체를 검색해서 받아야 하고 없으면 없다는 예외를 발생해야함

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND));

        //워크스페이스의 아이디와 현재 접속해있는 사용자 아이디로 현재 사용자의 워크스페이스 멤버 권한을 확인할수있다
        //보드가 존재하면 해당 보드가 속해있는 워크스페이스의 아이디 값을 받아와서 그 아이디로 멤버가 멤버의 역할을 조회할수있음
        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(user.getId(), workspaceId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND));

        //워크스페이스 멤버 권한이 READ가 아니면 다 할수있으므로 권한이 READ인것만 예외처리
        if (workspaceMember.getMemberRole().equals(MemberRole.READ)) {
            throw new QueensTrelloException(ErrorCode.INVALID_AUTHORITY_CREATE);
        }

        //생성자 생성
        Board newBoard = new Board(
                boardSaveRequest.getTitle(),
                boardSaveRequest.getBackgroundColor(),
                boardSaveRequest.getImageUrl(),
                workspace
        );

        Board savedBoard = boardRepository.save(newBoard);
        return BoardSaveResponse.of(savedBoard);
    }

    @Transactional
    public BoardSaveResponse getBoard(long boardId, User user) {
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));
        //워크 스페이스 멤버인지 여부 확인
        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(user.getId(), newBoard.getWorkspace().getId())
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND));

        return BoardSaveResponse.of(newBoard);
    }

    @Transactional
    public List<BoardSaveResponse> getBoards(Long workspaceId, User user) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND));

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(user.getId(), workspaceId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND));

        List<Board> boardList = boardRepository.findByWorkspaceId(workspaceId);
        //각 board에 속한 card들을 가져와 매핑, 나중에 개선 !!!
        return boardList.stream()
                .map(BoardSaveResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardSaveResponse updateBoard(Long boardId, BoardUpdateRequest boardRequest, AuthUser authUser, Long workspaceId) {
        //존재하면 존재한 보드 객체를 검색해서 받아야 하고 없으면 없다는 예외를 발생해야함
        User user = User.fromAuthUser(authUser);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));

        //워크스페이스의 아이디와 현재 접속해있는 사용자 아이디로 현재 사용자의 워크스페이스 멤버 권한을 확인할수있다
        //보드가 존재하면 해당 보드가 속해있는 워크스페이스의 아이디 값을 받아와서 그 아이디로 멤버가 멤버의 역할을 조회할수있음
        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(user.getId(), workspaceId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND));

        //워크스페이스 멤버 권한이 READ가 아니면 다 할수있으므로 권한이 READ인것만 예외처리
        if (workspaceMember.getMemberRole().equals(MemberRole.READ)) {
            throw new QueensTrelloException(ErrorCode.INVALID_AUTHORITY_CREATE);
        }
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));

        if (workspaceMember.getMemberRole().equals(MemberRole.READ)) {
            throw new QueensTrelloException(ErrorCode.INVALID_AUTHORITY_UPDATE);
        }
        if (boardRequest.getBackgroundColor() != null) {
            newBoard.changeBackgroundColor(boardRequest.getBackgroundColor());
        }
        if (boardRequest.getImageUrl() != null) {
            newBoard.changeImageUrl(boardRequest.getImageUrl());
            s3Service.deleteFile(board.getImageUrl()); //기존의 Url 더이상 참조안하면 버린다.
        }

        Board updateBoard = boardRepository.save(newBoard); //수정 후 저장
        return BoardSaveResponse.of(updateBoard);//응답으로 변환, 성공메세지만 전달해도 된다. 보드의 간단한 정보만 반환
    }


    @Transactional
    public void deleteBoard(long boardId, User user, AuthUser authUser) {
        User.fromAuthUser(authUser);
        //리스트를 추가할 보드가 존재하는지 확인
        //존재하면 존재한 보드 객체를 검색해서 받아야 하고 없으면 없다는 예외를 발생해야함
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));

        //워크스페이스의 아이디와 현재 접속해있는 사용자 아이디로 현재 사용자의 워크스페이스 멤버 권한을 확인할수있다
        //보드가 존재하면 해당 보드가 속해있는 워크스페이스의 아이디 값을 받아와서 그 아이디로 멤버가 멤버의 역할을 조회할수있음
        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(user.getId(), boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND));

        //워크스페이스 멤버 권한이 READ가 아니면 다 할수있으므로 권한이 READ인것만 예외처리
        if (workspaceMember.getMemberRole().equals(MemberRole.READ)) {
            throw new QueensTrelloException(ErrorCode.INVALID_AUTHORITY_CREATE);
        }

        boardRepository.delete(board);
    }

}
