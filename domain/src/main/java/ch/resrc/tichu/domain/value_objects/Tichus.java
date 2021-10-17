package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import ch.resrc.tichu.domain.entities.*;
import io.vavr.collection.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class Tichus {

    private final Map<PlayerId, Tichu> playerToTichu;

    public Tichus(Map<PlayerId, Tichu> playerToTichu) {
        this.playerToTichu = playerToTichu;
    }

    private static Validation<Map<PlayerId, Tichu>, ValidationError> validation() {
        return modified(
                chained(
                        notNull(),
                        attribute(Map::keySet, noneNull()),
                        attribute(Map::values, noneNull()),
                        attribute(x -> x.keySet().length(), equalTo(4, msg("exactly four players must have a tichu")))
                ),
                context(Tichus.class)
        );
    }

    public static Result<Tichus, ValidationError> resultOf(Map<PlayerId, Tichu> values) {
        return validation().applyTo(values).map(Tichus::new);
    }

    public static Tichus of(Map<PlayerId, Tichu> values) {
        return Tichus.resultOf(values).value();
    }

    public Map<PlayerId, Tichu> value() {
        return playerToTichu;
    }

    public Tichu tichuOfPlayer(PlayerId playerId) {
        return playerToTichu.get(playerId).getOrElse(Tichu.NONE);
    }
}
