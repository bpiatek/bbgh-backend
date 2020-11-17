package com.github.bpiatek.bbghbackend.model.comment;

import com.github.bpiatek.bbghbackend.model.comment.api.CommentNotFoundException;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

  public Page<CommentResponse> findByArticleId(Long articleId, Pageable pageable) {
    final Page<Comment> byArticleId = commentRepository.findByArticleId(articleId, pageable);

    final List<CommentResponse> collect = byArticleId.get()
        .map(Comment::toCommentResponse)
        .collect(Collectors.toList());

    return new PageImpl<>(collect);
  }
}
