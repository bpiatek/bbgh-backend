package com.github.bpiatek.bbghbackend.model;

import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
public interface CommentHtmlExtractor {
  List<Comment> getComments(String html);
}
