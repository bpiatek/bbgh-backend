package com.github.bpiatek.bbghbackend.model.player;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
interface PlayerRepository extends Repository<Player, Long> {
  Player save(Player player);
  Optional<Player> findById(Long id);

  @Query(value = "SELECT max(Player.url_id) FROM Player", nativeQuery = true)
  Integer findLastPlayerIdRead();
}
