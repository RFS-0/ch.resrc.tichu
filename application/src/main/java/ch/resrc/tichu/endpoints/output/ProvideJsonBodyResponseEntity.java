package ch.resrc.tichu.endpoints.output;

import javax.ws.rs.core.Response;

public interface ProvideJsonBodyResponseEntity<T> {

  Response asResponseEntity();
}
