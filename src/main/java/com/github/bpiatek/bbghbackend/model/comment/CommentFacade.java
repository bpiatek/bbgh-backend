package com.github.bpiatek.bbghbackend.model.comment;

import static java.util.stream.Collectors.toList;

import com.github.bpiatek.bbghbackend.model.comment.api.CommentIsHateSpeechRequest;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentNotFoundException;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CommentFacade {

  private final CommentRepository commentRepository;

  public Comment findById(Long id) {
    log.debug("Looking for COMMENT with ID: {}", id);
    return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
  }

  public Page<CommentResponse> findByArticleId(Long articleId, Pageable pageable) {
    log.debug("Looking for COMMENTS by article ID: {}", articleId);
    return toCommentResponsePage(commentRepository.findByArticleId(articleId, pageable));
  }

  public Page<CommentResponse> findAll(Pageable pageable) {
    return toCommentResponsePage(commentRepository.findAll(pageable));
  }

  public Page<CommentResponse> findWithNegativeMentionsMarkedByHuman(Pageable pageable) {
    return toCommentResponsePage(commentRepository.findWithNegativeMentionsMarkedByHuman(pageable));
  }

  private PageImpl<CommentResponse> toCommentResponsePage(Page<Comment> commentsPage) {
    List<CommentResponse> commentResponseList = toCommentResponseList(commentsPage);

    return new PageImpl<>(commentResponseList, commentsPage.getPageable(), commentsPage.getTotalElements());
  }

  private List<CommentResponse> toCommentResponseList(Page<Comment> commentsPage) {
    return commentsPage.get()
        .map(Comment::toCommentResponse)
        .collect(toList());
  }

  @Transactional
  public int setIsHateSpeech(Long id, CommentIsHateSpeechRequest request) {
    return commentRepository.setMentionSentimentById(id, request.getIsHateSpeech());
  }
}
