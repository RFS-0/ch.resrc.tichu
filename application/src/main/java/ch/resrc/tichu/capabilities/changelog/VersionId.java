package ch.resrc.tichu.capabilities.changelog;

import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class VersionId {

  private String idLiteral;

  private VersionId(String idLiteral) {
    this.idLiteral = idLiteral;
  }

  public static VersionId random() {
    return VersionId.fromLiteral(UUID.randomUUID().toString());
  }

  public static VersionId fromLiteral(String idLiteral) {
    return new VersionId(idLiteral);
  }

  public String asLiteral() {
    return idLiteral;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VersionId that = (VersionId) o;

    return new EqualsBuilder()
      .append(idLiteral, that.idLiteral)
      .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(idLiteral)
      .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("idLiteral", idLiteral)
      .toString();
  }
}
