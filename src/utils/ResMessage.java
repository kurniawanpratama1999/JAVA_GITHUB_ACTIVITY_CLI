package utils;

public record ResMessage<T>(boolean success, String message, T data) {

    public static <U> ResMessage<U> ok(String message, U data) {
        return new ResMessage<>(true, message, data);
    }

    public static <U> ResMessage<U> fail(String message) {
        return new ResMessage<>(false, message, null);
    }

    public static <U> ResMessage<U> err(String message) {
        return new ResMessage<>(false, "Err: " + message, null);
    }
}
