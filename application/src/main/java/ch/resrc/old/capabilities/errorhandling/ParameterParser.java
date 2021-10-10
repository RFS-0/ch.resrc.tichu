package ch.resrc.old.capabilities.errorhandling;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class ParameterParser {

  private static final Pattern PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

  public static Set<String> parse(String messageTemplate) {
    if (messageTemplate == null) {
      return Set.of();
    }

    var matcher = PATTERN.matcher(messageTemplate);
    return matcher.results()
      .map(match -> match.group(1))
      .map(String::trim)
      .collect(Collectors.toSet());
  }
}
