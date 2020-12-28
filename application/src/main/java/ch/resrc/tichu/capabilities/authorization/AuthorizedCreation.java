package ch.resrc.tichu.capabilities.authorization;

import ch.resrc.tichu.capabilities.authorization.guarded.Guarded;
import ch.resrc.tichu.capabilities.changelog.ChangeLogging;
import ch.resrc.tichu.capabilities.errorhandling.HavingDiagnosis;
import ch.resrc.tichu.capabilities.result.Result;

public class AuthorizedCreation<T extends ChangeLogging> extends AuthorizedMutation<T> {

  private AuthorizedCreation(AccessAuthorization auth) {
    super(auth);
  }

  public static <T extends ChangeLogging> AuthorizedCreation<T> of(Class<T> type, AccessAuthorization auth) {

    return new AuthorizedCreation<>(auth);
  }

  public <E extends HavingDiagnosis> AuthorizedMutation<T> created(Result<T, E> creationResult) {

    Result<Guarded<T>, HavingDiagnosis> created =
      creationResult.map(Guarded::ofNew)
        .castErrors(HavingDiagnosis.class)
        .handleErrors(authorizeMutationErrors())
        .flatMap((Guarded<T> x) -> x.authorized(auth), HavingDiagnosis.class);

    return new AuthorizedMutation<>(this, created);
  }
}
