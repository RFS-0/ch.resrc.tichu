package ch.resrc.tichu.domain;

import java.util.Objects;

public abstract class DomainPrimitive<T extends DomainPrimitive<T, V>, V> {


  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    T other = (T) o;
    return Objects.equals(getPrimitiveValue(), other.getPrimitiveValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPrimitiveValue());
  }

  @Override
  public String toString() {
    return getPrimitiveValue() != null ? getPrimitiveValue().toString() : null;
  }

  /**
   * @return the primitive value representation of this object
   */
  protected abstract V getPrimitiveValue();
}
