package de.goldmensch.chunkprotect.utils.functions;

@FunctionalInterface
public interface ThrowingFunction<E extends Exception> {
    void apply() throws E;
}
