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
    Document document = Jsoup.parse(html);
    final Elements tableRows = provideTableRows(document);

    String firstName = null;
    String lastName = null;
    String dob = null;

    for (Element e: tableRows) {
      String text = e.toString().replace("\n", "")
          .replaceAll(" +", " ");

      if(text.contains("Imię")) {
        firstName = getRegexValue(text);
      }
      if(text.contains("Nazwisko")) {
        lastName = getRegexValue(text);
      }
      if(text.contains("Data urodzenia")) {
        dob = getRegexValue(text);
      }
    }

    return Player.builder()
        .urlId(urlId)
        .firstName(firstName)
        .lastName(lastName)
        .dateOfBirth(getDateOfBirth(dob))
        .build();
  }

  private LocalDate getDateOfBirth(String dob) {
    return toLocalDateTimeParser.parseToLocalDate(dob);
  }

  private Elements provideTableRows(Document document) {
    Element table = document.select("table[class=main]").first();

    return table.select("tr");
  }

  private String getRegexValue(String text) {
    final Matcher matcher = PATTERN.matcher(text);

    if(matcher.find()) {
      return matcher.group("value");
    }

    return null;
  }
}
