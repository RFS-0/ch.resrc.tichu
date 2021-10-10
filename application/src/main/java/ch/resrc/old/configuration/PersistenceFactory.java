package ch.resrc.old.configuration;

import ch.resrc.old.adapters.persistence_in_memory.*;
import ch.resrc.old.adapters.persistence_micro_stream.*;
import ch.resrc.old.capabilities.errorhandling.*;
import ch.resrc.old.capabilities.errorhandling.faults.*;
import ch.resrc.old.domain.*;
import ch.resrc.old.domain.operations.*;
import software.amazon.awssdk.services.s3.*;

import javax.enterprise.context.*;

import static ch.resrc.old.configuration.ConfigurationProblem.*;

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

  InMemoryUsersRepository inMemoryUsersRepository;
  InMemoryGamesRepository inMemoryGamesRepository;
  InMemoryTeamsRepository inMemoryTeamsRepository;
  InMemoryPlayersRepository inMemoryPlayersRepository;

  public AddUser produceAddUserOperation(Environment environment, S3Client s3Client, String bucketName) {
    switch (environment) {
      case TEST -> {
        return getOrCreateTestRepository(InMemoryUsersRepository.class);
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
        return getOrCreateTestRepository(InMemoryUsersRepository.class);
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
        return getOrCreateTestRepository(InMemoryGamesRepository.class);
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
        return getOrCreateTestRepository(InMemoryGamesRepository.class);
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
        return getOrCreateTestRepository(InMemoryGamesRepository.class);
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
        return getOrCreateTestRepository(InMemoryTeamsRepository.class);
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
        return getOrCreateTestRepository(InMemoryTeamsRepository.class);
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
        return getOrCreateTestRepository(InMemoryTeamsRepository.class);
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
        return getOrCreateTestRepository(InMemoryPlayersRepository.class);
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
        return getOrCreateTestRepository(InMemoryPlayersRepository.class);
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
        return getOrCreateTestRepository(InMemoryPlayersRepository.class);
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
    } else if (repositoryClass.isAssignableFrom(MicroStreamGamesRepository.class)) {
      if (microStreamGamesRepository == null) {
        microStreamGamesRepository = new MicroStreamGamesRepository(s3Client, bucketName);
      }
      return (O) microStreamGamesRepository;
    } else if (repositoryClass.isAssignableFrom(MicroStreamTeamsRepository.class)) {
      if (microStreamTeamsRepository == null) {
        microStreamTeamsRepository = new MicroStreamTeamsRepository(s3Client, bucketName);
      }
      return (O) microStreamTeamsRepository;
    } else if (repositoryClass.isAssignableFrom(MicroStreamPlayersRepository.class)) {
      if (microStreamPlayersRepository == null) {
        microStreamPlayersRepository = new MicroStreamPlayersRepository(s3Client, bucketName);
      }
      return (O) microStreamPlayersRepository;
    } else {
      throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }

  @SuppressWarnings("unchecked")
  private <O extends OutputBoundary> O getOrCreateTestRepository(Class<? extends OutputBoundary> repositoryClass) {
    if (repositoryClass.isAssignableFrom(InMemoryUsersRepository.class)) {
      if (inMemoryUsersRepository == null) {
        inMemoryUsersRepository = new InMemoryUsersRepository();
      }
      return (O) inMemoryUsersRepository;
    } else if (repositoryClass.isAssignableFrom(InMemoryGamesRepository.class)) {
      if (inMemoryGamesRepository == null) {
        inMemoryGamesRepository = new InMemoryGamesRepository();
      }
      return (O) inMemoryGamesRepository;
    } else if (repositoryClass.isAssignableFrom(InMemoryTeamsRepository.class)) {
      if (inMemoryTeamsRepository == null) {
        inMemoryTeamsRepository = new InMemoryTeamsRepository();
      }
      return (O) inMemoryTeamsRepository;
    } else if (repositoryClass.isAssignableFrom(InMemoryPlayersRepository.class)) {
      if (inMemoryPlayersRepository == null) {
        inMemoryPlayersRepository = new InMemoryPlayersRepository();
      }
      return (O) inMemoryPlayersRepository;
    } else {
      throw Defect.of(ProblemDiagnosis.of(INVALID_ENVIRONMENT));
    }
  }
}
