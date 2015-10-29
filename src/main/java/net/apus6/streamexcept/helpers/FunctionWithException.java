package net.apus6.streamexcept.helpers;

public interface FunctionWithException<T, R, E extends Exception> {
    R apply(T input) throws E;
}
