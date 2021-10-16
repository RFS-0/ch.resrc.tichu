package ch.resrc.tichu.domain.value_objects;

import ch.resrc.tichu.capabilities.result.*;
import ch.resrc.tichu.capabilities.validation.*;
import io.vavr.collection.*;

import static ch.resrc.tichu.capabilities.validation.ValidationErrorModifier.*;
import static ch.resrc.tichu.capabilities.validation.Validations.chained;
import static ch.resrc.tichu.capabilities.validation.Validations.*;

public class Tichus {

    private final Map<Id, Tichu> playerToTichu;

    public Tichus(Map<Id, Tichu> playerToTichu) {
        this.playerToTichu = playerToTichu;
    }

    private static Validation<Map<Id, Tichu>, ValidationError> validation() {
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

    public static Result<Tichus, ValidationError> resultOf(Map<Id, Tichu> values) {
        return validation().applyTo(values).map(Tichus::new);
    }

    public Map<Id, Tichu> value() {
        return playerToTichu;
    }

    public Tichu tichuOfPlayer(Id playerId) {
        return playerToTichu.get(playerId).getOrElse(Tichu.NONE);
    }
}
