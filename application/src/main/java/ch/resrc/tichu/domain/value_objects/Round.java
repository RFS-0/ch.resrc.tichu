package ch.resrc.tichu.domain.value_objects;

import java.time.Instant;
import java.util.Set;

public class Round implements StringValueObject {

  private RoundNumber roundNumber;

  private Set<CardPoints> cardPoints;

  private Set<Tichu> tichus;

  private Set<GrandTichu> grandTichus;

  private Ranks ranks;

  private Instant startedAt;

  private Instant finishedAt;
}
