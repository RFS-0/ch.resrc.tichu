package ch.resrc.old.capabilities.validations.old;

import io.vavr.collection.*;
import org.apache.commons.lang3.builder.*;

import java.util.function.*;

import static java.lang.String.*;

public class ValidationError {

  private Origin origin;
  private String details;
  private Set<Claim> claims;
  private final Object invalidValue;

  public enum Origin {
    UNKNOWN("UNKNOWN"),
    DOMAIN("DOMAIN"),
    CLIENT("CLIENT"),
    PERSISTENCE("PERSISTENCE");

    private final String value;

    Origin(String value) {
      this.value = value;
    }

    public static Origin of(String value) {
      return switch (value.toUpperCase()) {
        case "UNKNOWN" -> UNKNOWN;
        case "DOMAIN" -> DOMAIN;
        case "CLIENT" -> CLIENT;
        case "PERSISTENCE" -> PERSISTENCE;
        default -> UNKNOWN;
      };
    }

    public String value() {
      return value;
    }
  }

  public enum Claim {
    CAN_BE_DISPLAYED,
    MUST_NOT_BE_DISPLAYED;
  }

  private ValidationError(Origin origin, String details, Object invalidValue, Set<Claim> claims) {
    this.origin = origin;
    this.details = details;
    this.invalidValue = invalidValue;
    this.claims = claims;
  }

  private ValidationError(ValidationError other) {
    this.origin = other.origin;
    this.details = other.details;
    this.invalidValue = other.invalidValue;
    this.claims = HashSet.ofAll(other.claims);
  }

  public static ValidationError of(String details) {
    return new ValidationError(Origin.UNKNOWN, details, null, HashSet.of());
  }

  public static ValidationError of(Origin origin, String details, Object invalidValue, Set<Claim> claims) {
    return new ValidationError(origin, details, invalidValue, claims);
  }

  public static <T> ValidationError of(Origin origin, String details, Supplier<T> invalidValueSupplier, Set<Claim> claims) {
    return new ValidationError(origin, details, invalidValueSupplier.get(), claims);
  }

  private ValidationError copied(Consumer<ValidationError> modification) {
    var theCopy = new ValidationError(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public ValidationError butOrigin(String value) {
    return this.copied(x -> x.origin = Origin.of(value));
  }

  public ValidationError butDetails(String details) {
    return this.copied(x -> x.details = details);
  }

  public ValidationError butClaimed(Claim toBeAdded) {
    return this.copied(x -> x.claims = claims.add(toBeAdded));
  }

  public ValidationError butClaimRevoked(Claim revoked) {
    return this.copied(x -> x.claims = claims.remove(revoked));
  }

  public String errorMessage() {

    return format("%s: %s%s", origin(), details(), invalidValuePart());
  }

  private String invalidValuePart() {
    if (isClaimed(Claim.MUST_NOT_BE_DISPLAYED)) {
      return "";
    } else {
      return format(" - Invalid value: <%s>", displayableInvalidValue());
    }
  }

  private Object displayableInvalidValue() {

    return this.claims.contains(Claim.CAN_BE_DISPLAYED) ? invalidValue() : "HIDDEN FOR SECURITY REASONS";
  }

  private boolean isClaimed(Claim claim) {
    return claims.contains(claim);
  }

  public String details() {
    return details;
  }

  public Origin origin() {
    return origin;
  }

  private Object invalidValue() {
    if (isClaimed(Claim.CAN_BE_DISPLAYED)) {
      return invalidValue;
    }

    throw new IllegalStateException(
      format(
        "The invalid value is not safe to be used. It is possibly malicious. "
          + "To access it, you must first claim it to be safe with <%s.%s>",
        Claim.class.getSimpleName(),
        Claim.CAN_BE_DISPLAYED.name()
      )
    );
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("origin", origin)
      .append("details", details)
      .append("claims", claims)
      .append("invalidValue", invalidValue)
      .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ValidationError that = (ValidationError) o;

    if (origin != that.origin) return false;
    if (details != null ? !details.equals(that.details) : that.details != null) return false;
    if (claims != null ? !claims.equals(that.claims) : that.claims != null) return false;
    return invalidValue != null ? invalidValue.equals(that.invalidValue) : that.invalidValue == null;
  }

  @Override
  public int hashCode() {
    int result = origin != null ? origin.hashCode() : 0;
    result = 31 * result + (details != null ? details.hashCode() : 0);
    result = 31 * result + (claims != null ? claims.hashCode() : 0);
    result = 31 * result + (invalidValue != null ? invalidValue.hashCode() : 0);
    return result;
  }
}
