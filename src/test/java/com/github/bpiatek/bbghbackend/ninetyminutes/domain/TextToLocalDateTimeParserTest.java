package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
class TextToLocalDateTimeParserTest {

  @Test
  void shouldCorrectlyParseStringToDate() {
    // given
    String textDate = "3 lipca 2020, 19:54:19 - dagazka";

    // when
    LocalDateTime actualDate = new TextToLocalDateTimeParser().parse(textDate);

    // then
    assertThat(actualDate).isEqualTo(LocalDateTime.of(2020, 7,3,19,54,19));
  }
}
