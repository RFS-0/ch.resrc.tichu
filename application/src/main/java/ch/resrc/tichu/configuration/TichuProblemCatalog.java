package ch.resrc.tichu.configuration;

import ch.resrc.tichu.capabilities.errorhandling.DomainProblem;
import ch.resrc.tichu.capabilities.errorhandling.GenericProblem;
import ch.resrc.tichu.capabilities.errorhandling.Problem;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCatalogue;
import ch.resrc.tichu.capabilities.errorhandling.ProblemCode;
import ch.resrc.tichu.capabilities.errorhandling.RestProblem;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static ch.resrc.tichu.capabilities.errorhandling.ProblemCode.code;
import static java.util.Map.entry;

public class TichuProblemCatalog implements ProblemCatalogue {

  static final List<Entry<Problem, ProblemCode>> codeCatalogue = List.of(
    entry(RestProblem.HTTP_METHOD_NOT_SUPPORTED, code(200)),
    entry(RestProblem.RESOURCE_NOT_FOUND, code(201)),
    entry(RestProblem.USE_CASE_RESULT_MISSING, code(202)),

    entry(DomainProblem.INVALID_PROPERTY_MUTATION, code(300)),
    entry(DomainProblem.MANDATORY_VALUE_MISSING, code(301)),
    entry(DomainProblem.INVARIANT_VIOLATED, code(302)),

    entry(GenericProblem.SYSTEM_FAILURE, code(400)),
    entry(GenericProblem.BAD_REQUEST, code(401)),
    entry(GenericProblem.COMMUNICATION_FAILURE, code(402))
  );

  @Override
  public ProblemCode codeFor(Problem problem) {
    return codeCatalogue.stream()
      .dropWhile(entry -> !entry.getKey().equals(problem))
      .map(Map.Entry::getValue)
      .findFirst()
      .orElse(ProblemCode.UNDEFINED);
  }

  List<Map.Entry<Problem, ProblemCode>> entries() {
    return codeCatalogue;
  }
}
