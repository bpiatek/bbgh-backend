package com.github.bpiatek.bbghbackend.model.player;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.github.bpiatek.bbghbackend.model.mention.api.SentimentCounter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

/**
 * Created by Bartosz Piatek on 17/01/2021
 */
class MentionRatioCalculatorTest {

  private static final MentionRatioCalculator ratioCalculator = new MentionRatioCalculator();

  @ParameterizedTest
  @MethodSource("provideSentimentCounterData")
  void shouldCalculatePlayerRatio(int positive, int negative, int neutral, int notChecked, String expectedRatio) {
    // given
    final SentimentCounter given = new SentimentCounter(
        valueOf(positive),
        valueOf(negative),
        valueOf(neutral),
        valueOf(notChecked),
        ZERO
    );

    // when
    SentimentCounter calculated = ratioCalculator.calculate(given);

    // then
    assertThat(calculated.getRatio()).isEqualTo(new BigDecimal(expectedRatio));
  }

  static Stream<Arguments> provideSentimentCounterData() {
    return Stream.of(
        arguments(1, 0, 0, 0, "100.00"),
        arguments(1, 0, 12, 4, "100.00"),
        arguments(0, 0, 12, 4, "0"),
        arguments(0, 1, 12, 4, "0.00"),
        arguments(3, 1, 12, 7, "75.00")
    );
  }
}
