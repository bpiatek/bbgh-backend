package com.github.bpiatek.bbghbackend.ninetyminutes.utils;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
public class TestUtils {
  public static final String HTML_EXAMPLE_FILE_1 = "src/test/resources/articles/htmlExample1.html";
  public static final String HTML_EXAMPLE_FILE_2 = "src/test/resources/articles/htmlExample2.html";
  public static final String HTML_EXAMPLE_FILE_3 = "src/test/resources/articles/htmlExample3.html";
  public static final String HTML_NO_COMMENTS = "src/test/resources/articles/htmlExampleNoComments.html";
  public static final String HTML_EXAMPLE_FILE_4 = "src/test/resources/articles/htmlExample4.html";
  public static final String HTML_EXAMPLE_FILE_4_EXTRA_COMMENT = "src/test/resources/articles/htmlExample4withExtraComment.html";
  public static final String HTML_PLAYER_1_FILE = "src/test/resources/player/playerExample1.html";
  public static final String HTML_PLAYER_2_FILE = "src/test/resources/player/playerExample2.html";
  public static final String HTML_PLAYER_3_FILE = "src/test/resources/player/playerExample3.html";

  @SneakyThrows
  public static String readHtmlTestFile(String path) {
    Charset ch = StandardCharsets.UTF_8;
    StringBuilder htmlBuilder = new StringBuilder();
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), ch))) {
      reader.lines().
          forEach(l -> htmlBuilder.append(l).append("\n"));
    }

    return htmlBuilder.toString();
  }
}
