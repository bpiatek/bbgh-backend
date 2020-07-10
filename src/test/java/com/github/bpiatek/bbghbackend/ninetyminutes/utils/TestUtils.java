package com.github.bpiatek.bbghbackend.ninetyminutes.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
public class TestUtils {
  public static final String HTML_EXAMPLE_FILE_1 = "src/test/resources/articles/htmlExample1.html";
  public static final String HTML_EXAMPLE_FILE_2 = "src/test/resources/articles/htmlExample2.html";

  public static String readHtmlTestFile(String path) {
    StringBuilder htmlBuilder = new StringBuilder();
    try (Stream<String> lines = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
      lines.forEach(l -> htmlBuilder.append(l).append("\n"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return htmlBuilder.toString();
  }
}
