package org.gobiiproject.gobiiprocess.machine.components;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface Reaction<S> extends Function<S,Consumer<S>> {

}
