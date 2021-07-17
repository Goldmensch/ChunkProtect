package de.goldmensch.chunkprotect.utils.functions;

@FunctionalInterface
public interface ReturnThrowingFunction<R, E extends Exception> {
    R apply() throws E;
}
