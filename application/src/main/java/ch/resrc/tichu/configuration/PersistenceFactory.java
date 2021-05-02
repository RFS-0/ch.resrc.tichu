package ch.resrc.tichu.configuration;

import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryGamesRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryPlayersRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryTeamsRepository;
import ch.resrc.tichu.adapters.persistence_in_memory.InMemoryUserRepository;
import ch.resrc.tichu.adapters.persistence_micro_stream.MicroStreamGamesRepository;
import ch.resrc.tichu.adapters.persistence_micro_stream.MicroStreamPlayersRepository;
import ch.resrc.tichu.adapters.persistence_micro_stream.MicroStreamTeamsRepository;
import ch.resrc.tichu.adapters.persistence_micro_stream.MicroStreamUsersRepository;
import ch.resrc.tichu.capabilities.errorhandling.ProblemDiagnosis;
import ch.resrc.tichu.capabilities.errorhandling.faults.Defect;
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
import ch.resrc.tichu.use_cases.ports.output_boundary.OutputBoundary;
import software.amazon.awssdk.services.s3.S3Client;

import javax.enterprise.context.ApplicationScoped;

import static ch.resrc.tichu.configuration.ConfigurationProblem.INVALID_ENVIRONMENT;

@ApplicationScoped
public class PersistenceFactory {

  public enum Environment {
    DEV,
    TEST,
    PROD;

    public static Environment forProfile(String activeProfile) {
      switch (activeProfile) {
        case "dev" -> {
          return DEV;
        }
        case "test" -> {
          return TEST;
        }
        case "prod" -> {
          return PROD;
        }
        default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
      }
    }
  }

  MicroStreamUsersRepository microStreamUsersRepository;
  MicroStreamGamesRepository microStreamGamesRepository;
  MicroStreamTeamsRepository microStreamTeamsRepository;
  MicroStreamPlayersRepository microStreamPlayersRepository;

  public AddUser produceAddUserOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryUserRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamUsersRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public GetAllUsers produceGetAllUsersOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryUserRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamUsersRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public AddGame produceAddGameOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryGamesRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamGamesRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public GetAllGames produceGetAllGamesOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryGamesRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamGamesRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public UpdateGame produceUpdateGameOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryGamesRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamGamesRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public AddTeam produceAddTeamOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryTeamsRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamTeamsRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public GetAllTeams produceGetAllTeamsOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryTeamsRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamTeamsRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public UpdateTeam produceUpdateTeamOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryTeamsRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamTeamsRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public AddPlayer produceAddPlayerOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryPlayersRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamPlayersRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public GetAllPlayers produceGetAllPlayersOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryPlayersRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamPlayersRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  public UpdatePlayer produceUpdatePlayerOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return new InMemoryPlayersRepository();
      }
      case DEV, PROD -> {
        return getOrCreateRepository(MicroStreamPlayersRepository.class, s3Client, bucketName);
      }
      default -> throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  @SuppressWarnings("unchecked")
  private <O extends OutputBoundary> O getOrCreateRepository(Class<? extends OutputBoundary> repositoryClass,
                                                             S3Client s3Client,
                                                             String bucketName) {
    if (repositoryClass.isAssignableFrom(MicroStreamUsersRepository.class)) {
      if (microStreamUsersRepository == null) {
        microStreamUsersRepository = new MicroStreamUsersRepository(s3Client, bucketName);
      }
      return (O) microStreamUsersRepository;
    } else if (repositoryClass.isAssignableFrom(InMemoryGamesRepository.class)) {
      if (microStreamGamesRepository == null) {
        microStreamGamesRepository = new MicroStreamGamesRepository(s3Client, bucketName);
      }
      return (O) microStreamGamesRepository;
    } else if (repositoryClass.isAssignableFrom(InMemoryTeamsRepository.class)) {
      if (microStreamTeamsRepository == null) {
        microStreamTeamsRepository = new MicroStreamTeamsRepository(s3Client, bucketName);
      }
      return (O) microStreamTeamsRepository;
    } else if (repositoryClass.isAssignableFrom(InMemoryPlayersRepository.class)) {
      if (microStreamPlayersRepository == null) {
        microStreamPlayersRepository = new MicroStreamPlayersRepository(s3Client, bucketName);
      }
      return (O) microStreamPlayersRepository;
    } else {
      throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }
}
