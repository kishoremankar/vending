package com.vendingmachine.Exceptions;

/**
 * Created by 212587383 on 7/11/2017.
 */
public class NotFullPaidException extends RuntimeException {
    private String message;
    private long remaining;
    public NotFullPaidException(String message, long remaining) {
        this.message = message;
        this.remaining = remaining;
    }
    public long getRemaining(){
        return remaining;
    }
    @Override
    public String getMessage(){
        return message + remaining;
    }
}

