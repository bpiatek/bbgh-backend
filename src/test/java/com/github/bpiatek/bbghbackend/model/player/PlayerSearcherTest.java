package com.github.bpiatek.bbghbackend.model.player;

import static java.util.List.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

/**
 * Created by Bartosz Piatek on 16/01/2021
 */
@ExtendWith(MockitoExtension.class)
class PlayerSearcherTest {

  @Mock
  private static PlayerRepository playerRepository;

  @InjectMocks
  private static PlayerSearcher playerSearcher;

  @BeforeAll
  static void setUp() {
    NamesAndSurnamesProvider provider = new NamesAndSurnamesProvider(playerRepository);
    provider.setDistinctFirstNames(of("Jan", "Kamil", "Wojciech"));
    provider.setDistinctLastNames(of("Nowak", "Kowalski", "Mazurek"));
    playerSearcher = new PlayerSearcher(provider);
  }

  @Test
  void shouldFindLastName() {
    // given
    String expectedLastName = "Nowak";

    // when
    PlayerSearchResult actualPlayer = playerSearcher.search(expectedLastName);

    // then
    assertThat(actualPlayer.getLastName()).contains(expectedLastName);
    assertThat(actualPlayer.getFirstName()).isEmpty();
  }

  @Test
  void shouldNotFindLastName() {
    // given
    String lastName = "NieMa";

    // when
    PlayerSearchResult actualPlayer = playerSearcher.search(lastName);

    // then
    assertThat(actualPlayer.getLastName()).isEmpty();
    assertThat(actualPlayer.getFirstName()).isEmpty();
  }

}
