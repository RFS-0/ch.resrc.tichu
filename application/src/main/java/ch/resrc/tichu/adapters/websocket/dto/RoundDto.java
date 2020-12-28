package ch.resrc.tichu.adapters.websocket.dto;

import java.util.Set;

public class RoundDto {

  private RoundNumberDto roundNumber;

  private Set<CardPointsDto> cardPoints;

  private Set<TichuDto> tichus;

  private Set<GrandTichuDto> grandTichus;

  private RanksDto ranks;

  private String startedAt;

  private String finishedAt;

}
