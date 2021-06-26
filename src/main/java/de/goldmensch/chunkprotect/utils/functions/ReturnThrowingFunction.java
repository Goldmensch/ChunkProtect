package de.goldmensch.chunkprotect.utils.functions;

public interface ReturnThrowingFunction<R, E extends Exception> {
    R apply() throws E;
}
