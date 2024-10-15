package com.practice.queenstrello.domain.board.service;

import com.practice.queenstrello.domain.board.dto.request.BoardSaveRequest;
import com.practice.queenstrello.domain.board.dto.request.BoardUpdateRequest;
import com.practice.queenstrello.domain.board.dto.response.BoardSaveResponse;
import com.practice.queenstrello.domain.board.entity.Board;
import com.practice.queenstrello.domain.board.repository.BoardRepository;
import com.practice.queenstrello.domain.card.entity.Card;
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
    //워크스페이스 레파지토리 추가

    @Transactional
    public BoardSaveResponse savedBoard(BoardSaveRequest boardSaveRequest) {
        Board newBoard = new Board(
                boardSaveRequest.getId(),
                boardSaveRequest.getTitle(),
                boardSaveRequest.getBackgroundColor(),
                boardSaveRequest.getImageUrl()
        );

        Board savedBoard = boardRepository.save(newBoard);
        return BoardSaveResponse.of(savedBoard);
    }

    public BoardSaveResponse getBoard(long boardId) {
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("Board not found") );
        return BoardSaveResponse.of(newBoard);
    }

    public List<BoardSaveResponse> getBoards() {
        Long workspaceId = null; //todo workspace or repository 에서 추가
        List<Board> boardList = boardRepository.findByWorkspaceId(workspaceId);
        //각 board에 속한 card들을 가져와 매핑
        return boardList.stream()
                .map(BoardSaveResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardSaveResponse updateBoard(long boardId, BoardUpdateRequest boardRequest) {
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("Board not found"));
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
        //JPQL 쿼리를 사용해 보드의 워크스페이스 업데이트
        boardRepository.updateWorkspace(boardId, workspaceId);
    };

    @Transactional
    public void deleteBoard(long boardId) {
        Board newBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("Board not found"));
        boardRepository.delete(newBoard);
    }
}
