package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static java.util.regex.Pattern.DOTALL;
import static java.util.regex.Pattern.quote;

import com.github.bpiatek.bbghbackend.model.player.Player;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@Service
@AllArgsConstructor
class PlayerCreator {

  private static final Pattern PATTERN = Pattern.compile(quote("<td><b>") + "(?<skip>.*?)" + quote("</b></td>") +
                                                         quote(" <td>") + "(?<value>.*?)" + quote("</td>"), DOTALL);

  private final TextToLocalDateTimeParser toLocalDateTimeParser;

  Player createFromHtml(String html, Integer urlId) {
    final Elements tableRows = provideTableRows(html);
    if (tableRows == null) {
      return null;
    }

    return buildPlayerFromTableRows(urlId, tableRows);
  }

  private Elements provideTableRows(String html) {
    Document document = Jsoup.parse(html);
    Element table = document.select("table[class=main]").first();
    if (table != null) {
      return table.select("tr");
    }
    return null;
  }

  private Player buildPlayerFromTableRows(Integer urlId, Elements tableRows) {
    String firstName = null;
    String lastName = null;
    String dob = null;
    String currentTeam = null;

    for (Element e : tableRows) {
      String text = elementToStringAndTrim(e);

      if (text.contains("Imię")) {
        firstName = getRegexValue(text);
      }
      if (text.contains("Nazwisko")) {
        lastName = getRegexValue(text);
      }
      if (text.contains("Data urodzenia")) {
        dob = getRegexValue(text);
      }
      if (text.contains("Obecny klub")) {
        final String value = getRegexValue(text);
        currentTeam = "".equals(value) ? null : value;
      }
    }

    return Player.builder()
        .urlId(urlId)
        .firstName(firstName)
        .lastName(lastName)
        .dateOfBirth(getDateOfBirth(dob))
        .currentTeam(currentTeam)
        .build();
  }

  private String elementToStringAndTrim(Element e) {
    return e.toString()
        .replace("\n", "")
        .replaceAll(" +", " ");
  }

  private LocalDate getDateOfBirth(String dob) {
    return toLocalDateTimeParser.parseToLocalDate(dob);
  }

  private String getRegexValue(String text) {
    final Matcher matcher = PATTERN.matcher(text);

    if (matcher.find()) {
      return matcher.group("value");
    }

    return null;
  }
}
