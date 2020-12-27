package ch.resrc.tichu.endpoints.errorhandling;

import ch.resrc.tichu.capabilities.errorhandling.Blame;
import ch.resrc.tichu.capabilities.errorhandling.Try;
import ch.resrc.tichu.capabilities.errorhandling.faults.OurFault;
import ch.resrc.tichu.capabilities.presentation.ErrorPresenter;
import ch.resrc.tichu.capabilities.result.Result;
import ch.resrc.tichu.capabilities.validation.Validatable;
import ch.resrc.tichu.capabilities.validation.ValidationError;
import ch.resrc.tichu.endpoints.output.HavingPresentation;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents the standard flow by which our REST endpoints invoke a use case.
 * <p>
 * Ensures that input validation errors are reported properly and audits any errors and exceptions that the use case might raise.
 * </p>
 * <p>
 * All REST endpoints should use this template to invoke the respective use case.
 * </p>
 */
public class UseCaseTry {

  private final Consumer<RuntimeException> errorAudit;

  public UseCaseTry(Consumer<RuntimeException> errorAudit) {
    this.errorAudit = errorAudit;
  }


  public <T extends Validatable<T>> Execution<T, ?> withInput(T input) {
    return new Execution<>(input, null);
  }

  public <P extends HavingPresentation & ErrorPresenter> Execution<NoInput, P> withOutput(P output) {
    return new Execution<>(new NoInput(), output);
  }

  public class Execution<IN extends Validatable<IN>, OUT extends HavingPresentation & ErrorPresenter> {

    private final IN input;
    private final OUT output;

    Execution(IN theInput, OUT theOutput) {
      this.input = theInput;
      this.output = theOutput;
    }

    public <OUT2 extends HavingPresentation & ErrorPresenter> Execution<IN, OUT2> withOutput(OUT2 output) {
      return new Execution<>(this.input, output);
    }

    public OUT invoke(BiConsumer<IN, OUT> useCase) {
      input.validated()
        .mapErrors(Blame::asClientFault)
        .forEachError(errorAudit::accept)
        .forEachError(output::presentBusinessError)
        .ifSuccess(audited(() -> useCase.accept(input, output)));
      return output;
    }

    public OUT invoke(Consumer<OUT> useCase) {
      return this.invoke((in, out) -> useCase.accept(out));
    }

    private Runnable audited(Runnable useCase) {
      return () -> Try.ofVoid(useCase)
        .onSuccess(() -> {
          if (output.isPresentationMissing()) {
            throw OurFault.of(RestProblem.USE_CASE_RESULT_MISSING);
          }
        })
        .onFailure(output::presentSystemFailure)
        .onFailure(errorAudit);
    }
  }

  static class NoInput implements Validatable<NoInput> {

    @Override
    public Result<NoInput, ValidationError> validated() {
      return Result.success(this);
    }
  }
}
