package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
class TextToLocalDateTimeParser {

  private static final int POSITION_OF_DAY = 0;
  private static final int POSITION_OF_MONTH = 1;
  private static final int POSITION_OF_YEAR = 2;
  private static final int POSITION_OF_TIME = 3;

  LocalDateTime parse(String textDate) {
    return constructLocalDateTime(splitBySpacesAndRemoveCommas(textDate));
  }

  private LocalDateTime constructLocalDateTime(List<String> list) {
    return LocalDateTime.of(constructLocalDate(list),
                            constructLocalTime(list.get(POSITION_OF_TIME)));
  }

  private LocalDate constructLocalDate(List<String> strings) {
    return LocalDate.of(parseInt(strings.get(POSITION_OF_YEAR)),
                        monthToNumber(strings.get(POSITION_OF_MONTH)),
                        parseInt(strings.get(POSITION_OF_DAY)));
  }

  private LocalTime constructLocalTime(String localTimeText) {
    List<Integer> timeValues = Arrays.stream(localTimeText.split(":"))
        .map(Integer::parseInt)
        .collect(Collectors.toList());

    return LocalTime.of(timeValues.get(0), timeValues.get(1), timeValues.get(2));
  }

  private int monthToNumber(String month) {
    switch (month) {
      case "stycznia":
        return 1;
      case "lutego":
        return 2;
      case "marca":
        return 3;
      case "kwietnia":
        return 4;
      case "maja":
        return 5;
      case "czerwca":
        return 6;
      case "lipca":
        return 7;
      case "sierpnia":
        return 8;
      case "września":
        return 9;
      case "października":
        return 10;
      case "listopada":
        return 11;
      case "grudnia":
        return 12;
      default:
        return 0;
    }
  }

  private List<String> splitBySpacesAndRemoveCommas(String text) {
    return asList(text.replace(",", "").split("\\s+"));
  }
}
