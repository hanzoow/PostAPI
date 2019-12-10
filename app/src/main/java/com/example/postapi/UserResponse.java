package com.example.postapi;
// T extends ....
public class UserResponse<T> {
    private T data;
    private Exception e;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }
}
