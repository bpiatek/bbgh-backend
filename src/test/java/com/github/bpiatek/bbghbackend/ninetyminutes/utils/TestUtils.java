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
