package ch.resrc.tichu.adapters.endpoints_websocket.teams.dto;

import ch.resrc.tichu.adapters.endpoints_websocket.games.create_game.dto.TeamDto;
import ch.resrc.tichu.use_cases.teams.add_player.ports.documents.PlayerAddedToTeamDocument;

import java.util.function.Consumer;

public class PlayerAddedToTeamDto {

  public PlayerDto player;
  public TeamDto team;

  public PlayerAddedToTeamDto(Consumer<PlayerAddedToTeamDto> initializer) {
    initializer.accept(this);
  }

  public static PlayerAddedToTeamDto fromDocument(PlayerAddedToTeamDocument the) {
    return new PlayerAddedToTeamDto(x -> {
      x.player = PlayerDto.fromDocument(the.player());
      x.team = TeamDto.fromDocument(the.team());
    });
  }

}
