package com.github.bpiatek.bbghbackend.model.comment;

import com.github.bpiatek.bbghbackend.model.comment.api.CommentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@Service
@AllArgsConstructor
public class CommentFacade {

  private final CommentRepository commentRepository;

  public Comment findById(Long id) {
    return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
  }

  public Page<Comment> findByArticleId(Long articleId, Pageable pageable) {
    return commentRepository.findByArticleId(articleId, pageable);
  }

  @Transactional
  public int setCommentOpinionStatus(Long id, CommentOpinionStatus status) {
    return commentRepository.setCommentOpinionStatusById(id, status);
  }
}
