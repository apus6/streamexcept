package net.apus6.streamexcept.helpers;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExceptionHelpers {

    public static <T, R, E extends Exception> Function<T, Either<R, E>> onExceptionMap(FunctionWithException<T, R, E> function) {
        return input -> {
            try {
                return Either.Success.success(function.apply(input));
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Exception e) {
                return Either.Failure.failure((E) e);
            }
        };
    }

    public static <T, E extends Exception> Consumer<Either<T, E>> onSuccess(Consumer<T> consumer) {
        return input -> {
            if (input.isSuccess()) {
                Either.Success<T, E> success = (Either.Success<T, E>) input;
                consumer.accept(success.result());
            }
        };
    }

    public static <T, R, E extends Exception> Function<Either<T, E>, Either<R, E>> onSuccessMapTry(FunctionWithException<T, R, E> function) {
        return input -> {
            if (input.isSuccess()) {
                Either.Success<T, E> success = (Either.Success<T, E>) input;
                try {
                    return Either.Success.success(function.apply(success.result()));
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Exception e) {
                    return Either.Failure.failure((E) e);
                }
            }
            Either.Failure<T, E> failure = (Either.Failure<T, E>) input;
            return Either.Failure.failure(failure.reason());
        };
    }

    public static <T, R, E extends Exception> Function<Either<T, E>, Either<R, E>> onSuccessMap(Function<T, R> function) {
        return input -> {
            if (input.isSuccess()) {
                Either.Success<T, E> success = (Either.Success<T, E>) input;
                return Either.Success.success(function.apply(success.result()));
            }
            Either.Failure<T, E> failure = (Either.Failure<T, E>) input;
            return Either.Failure.failure(failure.reason());
        };
    }

    public static <T, E extends Exception> Predicate<T> onExceptionFilter(PredicateWithException<T, E> predicate, Consumer<E> errorHandler) {
        return input -> {
            try {
                return predicate.test(input);
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Exception e) {
                errorHandler.accept((E) e);
                return false;
            }
        };
    }

    public static <T, E extends Exception> Predicate<Either<T, E>> onFailureFilter(Class<? extends Exception> exceptionType) {
        return input -> {
            if (input.isSuccess()) {
                return true;
            }
            Either.Failure<T, E> failure = (Either.Failure<T, E>) input;
            return !exceptionType.isAssignableFrom(failure.reason().getClass());
        };
    }

    public static <T, E extends Exception> Predicate<Either<T, E>> onFailureFilter(Class<? extends Exception> exceptionType, Consumer<E> errorHandler) {
        return input -> {
            if (input.isSuccess()) {
                return true;
            }
            Either.Failure<T, E> failure = (Either.Failure<T, E>) input;
            if (exceptionType.isAssignableFrom(failure.reason().getClass())) {
                errorHandler.accept(failure.reason());
                return false;
            }
            return true;
        };
    }

    public static <T, E extends Exception> Function<Either<T, E>, T> uneither(Consumer<E> errorHandler) {
        return input -> {
            if (input.isSuccess()) {
                Either.Success<T, E> success = (Either.Success<T, E>) input;
                return success.result();
            }
            Either.Failure<T, E> failure = (Either.Failure<T, E>) input;
            errorHandler.accept(failure.reason());
            throw new IllegalStateException(failure.reason());
        };
    }
}
