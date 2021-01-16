package com.github.bpiatek.bbghbackend.model.player;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Created by Bartosz Piatek on 16/01/2021
 */
@Value
@Builder
class PlayerSearchResult {
  Optional<String> firstName;
  Optional<String> lastName;
}
