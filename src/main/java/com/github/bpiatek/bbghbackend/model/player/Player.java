package com.github.bpiatek.bbghbackend.model.player;

import lombok.*;

import java.time.LocalDate;

import javax.persistence.*;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer urlId;
  private String firstName;
  private String lastName;
  private String currentTeam;
  private LocalDate dateOfBirth;

  public String getFullName() {
    return firstName + " " + lastName;
  }
}
