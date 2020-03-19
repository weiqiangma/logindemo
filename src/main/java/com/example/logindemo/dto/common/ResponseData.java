package com.example.logindemo.dto.common;

public class ResponseData<T> {

    private boolean success;

    private String message;

    private int code;

    private T result;

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

    private long timestamp = System.currentTimeMillis();

    public boolean isSuccess() { return success; };

    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage(){ return message; }

    public void setMessage(String message) { this.message = message; };

    public T getResult(){ return result; }

    public void setResult(T result) { this.result = result; };

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) {this.timestamp = timestamp;}
}
