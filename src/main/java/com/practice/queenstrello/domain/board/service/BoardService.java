package com.practice.queenstrello.domain.board.service;

import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.board.repository.BoardRepository;
import com.practice.queenstrello.domain.card.entity.Card;
import com.practice.queenstrello.domain.common.exception.QueensTrelloException;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.workspace.entity.Workspace;
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
    public BoardSaveResponse savedBoard(BoardSaveRequest boardSaveRequest, User user) {
        validateUser(user);// 로그인체크

        Workspace workspace = workspaceRepository.findById(Request.getWorkspaceId())
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));

        if (user.isReadOnly()) {
            throw new QueensTrelloException("보드를 생성할 권한이 없습니다.");
        }

        if (boardSaveRequest.getTitle() == null || boardSaveRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("보드 제목은 필수입니다.");
        }

        Board newBoard = new Board(
                boardSaveRequest.getId(),
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
                .orElseThrow(()-> new IllegalArgumentException("해당 보드를 찾을 수 없습니다.") );
        return BoardSaveResponse.of(newBoard);
    }

    public List<BoardSaveResponse> getBoards(Long workspaceId) {
        Long workspaceId = null; //todo workspace or repository 에서 추가
        List<Board> boardList = boardRepository.findByWorkspaceId(workspaceId);
        //각 board에 속한 card들을 가져와 매핑
        return boardList.stream()
                .map(BoardSaveResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardSaveResponse updateBoard(long boardId, BoardUpdateRequest boardRequest, User user) {
        validateUser(user); //로그인 체크
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));
        if (user.isReadOnly()) {
            throw new QueensTrelloException("보드를 수정할 권한이 없습니다.");
        }
        if (boardRequest.getTitle() != null) {
            newBoard.changeTitle(boardRequest.getTitle());
        }
        if (boardRequest.getBackgroundColor() != null) {
            newBoard.changeBackgroundColor(boardRequest.getBackgroundColor());
        }
        if (boardRequest.getImageUrl() != null) {
            newBoard.changeImageUrl(boardRequest.getImageUrl());
        }

        Board updateBoard = boardRepository.save(newBoard); //수정 후 저장
        return BoardSaveResponse.of(updateBoard);//응답으로 변환
    }
    @Transactional
    public void updateBoardWorkspace(long boardId, Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));
        //JPQL 쿼리를 사용해 보드의 워크스페이스 업데이트
        boardRepository.updateWorkspace(boardId, workspaceId);
    };

    @Transactional
    public void deleteBoard(long boardId, User user) {
        validateUser(user); //로그인 체크
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));

        if (user.isReadOnly()) {
            throw new QueensTrelloException("보드를 삭제할 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
    }
}
