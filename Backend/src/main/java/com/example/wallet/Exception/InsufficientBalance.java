package com.example.wallet.Exception;

public class InsufficientBalance extends RuntimeException{
    public InsufficientBalance(String message) {
        super(message);
    }

    public InsufficientBalance(Throwable cause) {
        super(cause);
    }

    public InsufficientBalance(String message, Throwable cause) {
        super(message, cause);
    }
}
