package de.goldmensch.chunkprotect.utils.functions;

public interface ThrowingFunction<E extends Exception> {
    void apply() throws E;
}
