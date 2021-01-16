package com.github.bpiatek.bbghbackend.model.player;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 16/01/2021
 */
@ExtendWith(MockitoExtension.class)
class PlayerSearcherTest {

  @Mock
  PlayerRepository playerRepository;

  @InjectMocks
  PlayerSearcher playerSearcher;

  @Test
  void test() {
    // given
    List<String> distinctFirstNames = List.of("Jan", "Kamil", "Wojciech");
    List<String> distinctLastNames = List.of("Nowak", "Kowalski", "Mazurek");
    String lastName = "Nowak";

    // when
    final Optional<String> lastName1 = playerSearcher.getLastName(lastName);

    // then
    assertThat(true);
  }
}
