package ch.resrc.tichu.adapters.javax_validation;

import java.util.Locale;
import javax.validation.MessageInterpolator;

public class NoMessageInterpolation implements MessageInterpolator {

  @Override
  public String interpolate(String messageTemplate, Context context) {

    return messageTemplate;
  }

  @Override
  public String interpolate(String messageTemplate, Context context, Locale locale) {

    return messageTemplate;
  }
}
