package com.practice.queenstrello.domain.board.service;

import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.board.repository.BoardRepository;
import com.practice.queenstrello.domain.common.exception.ErrorCode;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
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

    @Transactional
    public BoardSaveResponse savedBoard(Long workspaceId, BoardSaveRequest boardSaveRequest, User user) {
        validateUser(user);// 로그인체크, 코드 컨벤션 맞추기

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));// 에러 코드 개선

        if (user.isReadOnly()) {
            throw new QueensTrelloException(ErrorCode.INVALID_AUTHORITY_CREATE);
        }
        //메서드로 묶고 public 처리 ,Global Exception 처리

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
        validateUser(user); //로그인 체크
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));
        if (user.isReadOnly()) {
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
        validateUser(user); //로그인 체크
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.BOARD_NOT_FOUND));
    //Workspace 레파지토리 체크
        if (user.isReadOnly()) {
            throw new QueensTrelloException(ErrorCode.INVALID_AUTHORITY_DELETE);
        }

        boardRepository.delete(board);
    }

    //validateWorkspaceMember로 변경
    private void validateUser(User user) {
        if (user == null) {
            throw new QueensTrelloException(ErrorCode.INVALID_USER);
        }
    }
}
