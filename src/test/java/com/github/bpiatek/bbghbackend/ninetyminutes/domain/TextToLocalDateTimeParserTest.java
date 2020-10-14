package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
class TextToLocalDateTimeParserTest {

  @Test
  void shouldCorrectlyParseStringToLocalDateTime() {
    // given
    String textDate = "3 lipca 2020, 19:54:19 - dagazka";

    // when
    LocalDateTime actualDate = new TextToLocalDateTimeParser().parseToLocalDateTime(textDate);

    // then
    assertThat(actualDate).isEqualTo(LocalDateTime.of(2020, 7,3,19,54,19));
  }

  @Test
  void shouldCorrectlyParseStringToLocalDate() {
    // given
    String textDate = "5 maja 1982 (38 lat)";

    // when
    final LocalDate actualDate = new TextToLocalDateTimeParser().parseToLocalDate(textDate);

    // then
    assertThat(actualDate).isEqualTo(LocalDate.of(1982, 5,5));
  }
}
