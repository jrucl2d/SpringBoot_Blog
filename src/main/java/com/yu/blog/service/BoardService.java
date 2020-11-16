package com.yu.blog.service;

import com.yu.blog.dto.SaveReplyRequestDto;
import com.yu.blog.model.Board;
import com.yu.blog.model.Reply;
import com.yu.blog.model.User;
import com.yu.blog.repository.BoardRepository;
import com.yu.blog.repository.ReplyRepository;
import com.yu.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void 글쓰기(Board board, User user){
        board.setCount(0); // 조회수 초기화
        board.setUser(user); // 글쓴이가 저장됨
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> 글목록(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board 글상세보기(int id){
        return boardRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 글은 존재하지 않습니다.");
        });
    }

    @Transactional
    public void 글삭제하기(int id){
        boardRepository.deleteById(id);
    }

    @Transactional
    public void 글수정하기(int id, Board requestBoard){
        // 영속화 먼저 수행(영속성 컨텍스트에 board 데이터가 들어감). DB의 board 데이터와 영속성 컨텍스트 board 데이터가 동기화
        Board board = boardRepository.findById(id).orElseThrow(()->{ 
            return new IllegalArgumentException("해당 글은 존재하지 않습니다.");
        });
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        // 해당 함수 종료시(Service가 종료될 때) 트랜잭션이 종료되면서 더티체킹 일어남(자동 업데이트. db쪽으로 flush 일어남)
    }

    @Transactional
    public void 댓글쓰기(SaveReplyRequestDto saveReplyRequestDto){
        replyRepository.nativeQuerySave(saveReplyRequestDto.getUserId(), saveReplyRequestDto.getBoardId(), saveReplyRequestDto.getContent());
    }

    @Transactional
    public void 댓글삭제(int replyId){
        replyRepository.deleteById(replyId);
    }
}
