package com.end2end.ansimnuri.board.service;

import com.end2end.ansimnuri.board.domain.entity.Qna;
import com.end2end.ansimnuri.board.domain.entity.QnaReply;
import com.end2end.ansimnuri.board.domain.repository.QnaReplyRepository;
import com.end2end.ansimnuri.board.domain.repository.QnaRepository;
import com.end2end.ansimnuri.board.dto.QnaReplyDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QnaReplyServiceImpl implements QnaReplyService {
    private final QnaReplyRepository qnaReplyRepository;
    private final QnaRepository qnaRepository;

    @Override
    public QnaReplyDTO selectByQnaId(long qnaId) {
        QnaReply qnaReply = qnaReplyRepository.findByQnaId(qnaId);
        return (qnaReply == null) ? null : QnaReplyDTO.of(qnaReply);
    }

    @Transactional
    @Override
    public void insert(QnaReplyDTO qnaReplyDTO) {
        Qna qna = qnaRepository.findById(qnaReplyDTO.getQnaId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d에 해당하는 ID가 존재하지 않습니다.", qnaReplyDTO.getQnaId())));
        QnaReply qnaReply = qnaReplyRepository.findByQnaId(qnaReplyDTO.getQnaId());
        if(qnaReply != null) {
            throw new IllegalArgumentException("해당 질문글은 이미 답변이 존재합니다.");
        }

        qnaReplyRepository.save(QnaReply.of(qnaReplyDTO.getContent(), qna));
    }

    @Transactional
    @Override
    public void update(QnaReplyDTO qnaReplyDTO) {
        QnaReply qnaReply = qnaReplyRepository.findById(qnaReplyDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d에 해당하는 ID가 존재하지 않습니다.", qnaReplyDTO.getId())));
        qnaReply.update(qnaReplyDTO.getContent());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        QnaReply qnaReply = qnaReplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d에 해당하는 ID가 존재하지 않습니다.", id)));
        qnaReplyRepository.delete(qnaReply);
    }
}
