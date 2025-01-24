package ca.javau11.utils;

public class Response<T> {
    private String message;
    private T data;
    private String token;

    public Response(String message, T data, String token) {
        this.message = message;
        this.data = data;
        this.token = token;
    }
    
    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }
    
    public Response(String message) {
        this.message = message;
        this.data = null;
        this.token = null;
    }

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
