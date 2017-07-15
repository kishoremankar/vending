package com.vendingmachine.Exceptions;

/**
 * Created by 212587383 on 7/11/2017.
 */
public class NotSufficientChangeException extends RuntimeException {
    private String message;
    public NotSufficientChangeException(String string) {
        this.message = string;
    }
    @Override
    public String getMessage(){
        return message;
    }
}

