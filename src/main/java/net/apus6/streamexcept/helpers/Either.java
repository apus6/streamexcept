package net.apus6.streamexcept.helpers;

public interface Either<T, E extends Exception> {

    interface Success<T, E extends Exception> extends Either<T, E> {
        T result();
        static  <T, E extends Exception> Success<T, E> success(T value) {
            return () -> value;
        }
    }

    interface Failure<T, E extends Exception> extends Either<T, E> {
        E reason();
        static  <T, E extends Exception> Failure<T, E> failure(E exception) {
            return () -> exception;
        }
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failure;
    }
}