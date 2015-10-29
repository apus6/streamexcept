package net.apus6.streamexcept.helpers;

public interface PredicateWithException<T, E extends Exception>  {
    boolean test(T input) throws E;
}
