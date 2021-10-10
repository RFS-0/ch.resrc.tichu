package ch.resrc.old.domain.value_objects;

import com.fasterxml.jackson.annotation.*;


/**
 * A value object whose natural primitive representation is a {@code String}.
 *
 * <p>Directs the JSON serialization to the {@code toString()} method. Also useful for mappers that
 * need to map objects of this type to {@code String}s. They can use a generic sub-mapper that maps {@code StringValueObject} by using
 * the {@code toString} method.</p>
 */
public interface StringValueObject {

  @Override
  @JsonValue
  String toString();

}
