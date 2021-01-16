package com.github.bpiatek.bbghbackend.model.player;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
interface PlayerRepository extends Repository<Player, Long> {
  Player save(Player player);

  Optional<Player> findById(Long id);

  @Query(value = "SELECT max(player.url_id) FROM player", nativeQuery = true)
  Integer findLastPlayerIdRead();

  Page<Player> findAll(Pageable pageable);

  @Query("SELECT DISTINCT p.firstName, p.lastName FROM Player p")
  List<String> distinctFullNames();

  @Query("SELECT p FROM Player p WHERE UPPER(CONCAT(p.firstName, ' ', p.lastName)) LIKE UPPER(CONCAT('%', :name, '%'))")
  Page<Player> search(@Param("name") String name, Pageable pageable);

  Page<Player> findAllByFirstNameIgnoreCase(String firstName, Pageable pageable);

  Page<Player> findAllByLastNameIgnoreCase(String lastName, Pageable pageable);

  Page<Player> findAllByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName, Pageable pageable);
}
