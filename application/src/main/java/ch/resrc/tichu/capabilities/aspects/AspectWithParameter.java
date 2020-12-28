package ch.resrc.tichu.capabilities.aspects;

public interface AspectWithParameter<T> {

  Aspect using(T parameter);
}
