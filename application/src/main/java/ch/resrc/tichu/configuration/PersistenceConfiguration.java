package ch.resrc.tichu.configuration;

import ch.resrc.tichu.domain.operations.AddGame;
import ch.resrc.tichu.domain.operations.AddPlayer;
import ch.resrc.tichu.domain.operations.AddTeam;
import ch.resrc.tichu.domain.operations.AddUser;
import ch.resrc.tichu.domain.operations.GetAllGames;
import ch.resrc.tichu.domain.operations.GetAllPlayers;
import ch.resrc.tichu.domain.operations.GetAllTeams;
import ch.resrc.tichu.domain.operations.GetAllUsers;
import ch.resrc.tichu.domain.operations.UpdateGame;
import ch.resrc.tichu.domain.operations.UpdatePlayer;
import ch.resrc.tichu.domain.operations.UpdateTeam;
import io.quarkus.runtime.configuration.ProfileManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.s3.S3Client;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

public class PersistenceConfiguration {

  @ConfigProperty(name = "ch.resrc.tichu.data.games.bucket-name")
  Optional<String> gamesBucketName;
  @ConfigProperty(name = "ch.resrc.tichu.data.users.bucket-name")
  Optional<String> usersBucketName;
  @ConfigProperty(name = "ch.resrc.tichu.data.teams.bucket-name")
  Optional<String> teamsBucketName;
  @ConfigProperty(name = "ch.resrc.tichu.data.players.bucket-name")
  Optional<String> playersBucketName;

  @ApplicationScoped
  public AddGame addGame(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceAddGameOperation(
      environment,
      s3Client,
      gamesBucketName.orElse("games.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public GetAllGames getAllGames(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceGetAllGamesOperation(
      environment,
      s3Client,
      gamesBucketName.orElse("games.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public UpdateGame updateGame(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceUpdateGameOperation(
      environment,
      s3Client,
      gamesBucketName.orElse("games.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public AddUser addUser(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceAddUserOperation(
      environment,
      s3Client,
      usersBucketName.orElse("users.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public GetAllUsers getAllUsers(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceGetAllUsersOperation(
      environment,
      s3Client,
      usersBucketName.orElse("users.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public AddTeam addTeam(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceAddTeamOperation(
      environment,
      s3Client,
      teamsBucketName.orElse("teams.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public GetAllTeams getAllTeams(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceGetAllTeamsOperation(
      environment,
      s3Client,
      teamsBucketName.orElse("teams.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public UpdateTeam updateTeam(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceUpdateTeamOperation(
      environment,
      s3Client,
      teamsBucketName.orElse("teams.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public AddPlayer addPlayer(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceAddPlayerOperation(
      environment,
      s3Client,
      playersBucketName.orElse("players.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public GetAllPlayers getAllPlayers(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceGetAllPlayersOperation(
      environment,
      s3Client,
      playersBucketName.orElse("players.tichu.resrc.ch")
    );
  }

  @ApplicationScoped
  public UpdatePlayer updatePlayer(PersistenceFactory persistenceFactory, S3Client s3Client) {
    PersistenceFactory.Environment environment = PersistenceFactory.Environment.forProfile(
      ProfileManager.getActiveProfile()
    );
    return persistenceFactory.produceUpdatePlayerOperation(
      environment,
      s3Client,
      playersBucketName.orElse("players.tichu.resrc.ch")
    );
  }
}
