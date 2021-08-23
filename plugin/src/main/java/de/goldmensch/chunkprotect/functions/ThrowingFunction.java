package de.goldmensch.chunkprotect.functions;

@FunctionalInterface
public interface ThrowingFunction<E extends Exception> {
    void apply() throws E;
}