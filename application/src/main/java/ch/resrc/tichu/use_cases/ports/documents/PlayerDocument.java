package ch.resrc.tichu.use_cases.ports.documents;

import ch.resrc.tichu.capabilities.validation.InvalidInputDetected;
import ch.resrc.tichu.capabilities.validation.Validation;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.domain.entities.Player;
import ch.resrc.tichu.domain.value_objects.Id;
import ch.resrc.tichu.domain.value_objects.Name;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ch.resrc.tichu.capabilities.validation.Validations.allOf;
import static ch.resrc.tichu.capabilities.validation.Validations.attribute;
import static ch.resrc.tichu.capabilities.validation.Validations.notNull;
import static ch.resrc.tichu.use_cases.ports.documents.PlayerDocumentValidationErrors.MUST_NOT_BE_NULL;

public class PlayerDocument {

  private Id id;
  private Name name;
  private Instant createdAt;

  private static Validation<Seq<ValidationError>, PlayerDocument> validation() {
    return allOf(
      attribute(x -> x.id, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.name, notNull(MUST_NOT_BE_NULL)),
      attribute(x -> x.createdAt, notNull(MUST_NOT_BE_NULL))
    );
  }

  private PlayerDocument() {
  }

  public PlayerDocument(PlayerDocument other) {
    this.id = other.id;
    this.name = other.name;
    this.createdAt = other.createdAt;
  }

  private PlayerDocument copied(Consumer<PlayerDocument> modification) {
    final var theCopy = new PlayerDocument(this);
    modification.accept(theCopy);
    return theCopy;
  }

  public static Builder aPlayerDocument() {
    return new Builder();
  }

  public static PlayerDocument fromPlayer(Player player) {
    if (player == null) {
      return null;
    }
    return PlayerDocument.aPlayerDocument()
      .withId(player.id())
      .withName(player.name())
      .withCreatedAt(player.createdAt())
      .build();
  }

  public Builder but() {
    return new Builder(this.copied(__ -> {
    }));
  }

  public Id id() {
    return id;
  }

  public Name name() {
    return name;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public static class Builder {

    private final PlayerDocument workpiece;

    private Builder() {
      this.workpiece = new PlayerDocument();
    }

    private Builder(PlayerDocument workpiece) {
      this.workpiece = workpiece;
    }

    public Builder withId(Id id) {
      return new Builder(workpiece.copied(but -> but.id = id));
    }

    public Builder withName(Name name) {
      return new Builder(workpiece.copied(but -> but.name = name));
    }

    public Builder withCreatedAt(Instant createdAt) {
      return new Builder(workpiece.copied(but -> but.createdAt = createdAt));
    }

    public Either<Seq<ValidationError>, PlayerDocument> buildResult() {
      return validation().applyTo(workpiece);
    }

    public PlayerDocument build() {
      return buildResult().getOrElseThrow(InvalidInputDetected::of);
    }
  }
}

class PlayerDocumentValidationErrors {

    static final Supplier<ValidationError> MUST_NOT_BE_NULL = () -> ValidationError.of(
      PlayerDocument.class.getName(), "must not be null"
    );

}
