package ch.resrc.tichu.capabilities.aspects;

import static ch.resrc.tichu.capabilities.functional.PersistentCollections.addedTo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Decorates a {@code Runnable} with {@link Aspect}s using a convenient fluent interface that lets you apply the aspects in a
 * declarative way.
 *
 * @implNote Effectively immutable. Mutations produce copies of this object.
 */
public class Aspects {

  private List<Aspect> aspectChain;

  private Aspects(List<Aspect> aspects) {
    this.aspectChain = new ArrayList<>(aspects);
  }

  private Aspects(Aspects other, Consumer<Aspects> modification) {
    this.aspectChain = other.aspectChain;

    modification.accept(this);
  }

  /**
   * Creates a new instance that represents the stack of the specified aspects. The aspects get applied in the specified order when
   * wrapped around an operation with {@link #around(Runnable)}
   *
   * @param aspects the aspects
   * @return a new instance as explained
   */
  public static Aspects of(Aspect... aspects) {
    return new Aspects(List.of(aspects));
  }


  /**
   * Specifies further aspects to be applied after the aspects that have already been defined with {@link #around(Aspect...)}. The
   * aspects are applied in the order as they are given in the vararg sequence.
   *
   * @param aspects the {@code Aspect}s to be applied
   * @return this object for chained calls
   */
  public Aspects around(Aspect... aspects) {
    return around(List.of(aspects));
  }

  /**
   * Specifies further aspects to be applied after the aspects that have already been defined with {@link #around}. The aspects are
   * applied in the order as they are given in the list.
   *
   * @param aspects the {@code Aspect}s to be applied
   * @return this object for chained calls
   */
  public Aspects around(List<Aspect> aspects) {
    return new Aspects(this, $ -> $.aspectChain = addedTo($.aspectChain, aspects));
  }

  /**
   * Makes a single {@code Aspect} that executes the {@code Aspects} that you have defined so far if applied to an operation.
   *
   * @return the compound aspect
   */
  public Aspect makeCompoundAspect() {
    return aspectChain.stream()
      .reduce((aspect1, aspect2) -> (Runnable operation) -> aspect1.applyTo(() -> aspect2.applyTo(operation)))
      .orElse(Runnable::run); // in case no aspects were defined.
  }

  /**
   * Produces a {@code Runnable} the executes the given operation with the aspects of this object applied. When the runnable is
   * invoked, first the aspects execute in the defined order an then the given operation gets invoked. When the operation returns, the
   * control flow passes through the aspects in reverse order.
   *
   * @param operation the operation to which the aspects should be applied
   * @return a {@code Runnable} as explained.
   */
  public Runnable around(Runnable operation) {
    Aspect compoundAspect = makeCompoundAspect();

    return () -> compoundAspect.applyTo(operation);
  }
}
