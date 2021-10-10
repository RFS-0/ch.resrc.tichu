package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;

import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class Picture extends DomainPrimitive<Picture, String> implements StringValueObject, Comparable<Picture> {

    private final String value;

    private Picture(String value) {
        this.value = value;
    }

    public static Validation<String, ValidationError> validation() {
        return chained(
                notBlank(),
                isUrl()
        );
    }

    public static Result<Picture, ValidationError> resultOf(String literal) {
        return validation().applyTo(literal).map(Picture::new);
    }

    @Override
    protected String getPrimitiveValue() {
        return value;
    }

    @Override
    public int compareTo(Picture other) {
        return this.value.compareTo(other.value);
    }
}
