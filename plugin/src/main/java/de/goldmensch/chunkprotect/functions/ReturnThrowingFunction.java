package de.goldmensch.chunkprotect.functions;

@FunctionalInterface
public interface ReturnThrowingFunction<R, E extends Exception> {

  R apply() throws E;
}
