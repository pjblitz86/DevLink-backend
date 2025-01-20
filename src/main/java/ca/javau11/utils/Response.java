package ca.javau11.utils;

public class Response<T> {
    private String message;
    private T data;
    private String token;

    // Constructor
    public Response(String message, T data, String token) {
        this.message = message;
        this.data = data;
        this.token = token;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
