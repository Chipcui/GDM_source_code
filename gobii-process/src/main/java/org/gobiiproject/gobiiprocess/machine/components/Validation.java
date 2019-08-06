package org.gobiiproject.gobiiprocess.machine.components;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Validation<S> extends Function<S,Predicate<S>> {

}
