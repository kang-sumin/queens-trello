package com.practice.queenstrello.domain.board.service;

import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.board.repository.BoardRepository;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.workspace.entity.MemberRole;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceMemberRepository;
import com.practice.queenstrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.practice.queenstrello.domain.board.entity.QBoard.board;

@Service
@RequiredArgsConstructor

public class BoardService {
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional
    public BoardSaveResponse savedBoard(Long workspaceId, BoardSaveRequest boardSaveRequest, User user) {
        validateWorkspaceMember(user);// 로그인체크, 코드 컨벤션 맞추기

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));// 에러 코드 개선

        boolean isReadOnly = workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(user.getId(), workspaceId, MemberRole.READ);
        //메서드로 묶고 public 처리 ,Global Exception 처리

        if (isReadOnly) {
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

    public BoardSaveResponse getBoard(long boardId) {
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));
        return BoardSaveResponse.of(newBoard);
    }

    public List<BoardSaveResponse> getBoards(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));
        List<Board> boardList = boardRepository.findByWorkspaceId(workspaceId);
        //각 board에 속한 card들을 가져와 매핑, 나중에 개선 !!!
        return boardList.stream()
                .map(BoardSaveResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardSaveResponse updateBoard(long boardId, BoardUpdateRequest boardRequest, User user) {
        validateWorkspaceMember(user); //로그인 체크
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));

        boolean isReadOnly = workspaceMemberRepository.existsByMemberIdAndWorkspaceIdAndMemberRole(
                user.getId(), newBoard.getWorkspace().getId(), MemberRole.READ);

        if (isReadOnly) {
            throw new QueensTrelloException(ErrorCode.INVALID_AUTHORITY_UPDATE);
        }
        if (boardRequest.getBackgroundColor() != null) {
            newBoard.changeBackgroundColor(boardRequest.getBackgroundColor());
        }
        if (boardRequest.getImageUrl() != null) {
            newBoard.changeImageUrl(boardRequest.getImageUrl());
        }

        Board updateBoard = boardRepository.save(newBoard); //수정 후 저장
        return BoardSaveResponse.of(updateBoard);//응답으로 변환, 성공메세지만 전달해도 된다. 보드의 간단한 정보만 반환
    }


    @Transactional
    public void deleteBoard(long boardId, User user) {
        validateWorkspaceMember(user); //로그인 체크
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));

        if (user == null) {
            throw new QueensTrelloException(ErrorCode.INVALID_AUTHORITY_DELETE);
        }

        boardRepository.delete(board);
    }

    //validateWorkspaceMember로 변경 v
    private void validateWorkspaceMember(User user) {
        if (user == null) {
            throw new QueensTrelloException(ErrorCode.INVALID_USER);
        }
    }
}
