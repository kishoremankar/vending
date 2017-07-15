package com.vendingmachine.model;

/**
 * Created by 212587383 on 7/11/2017.
 */
public enum Coin {
    HUNDRED(100), TWENTY(20), TEN(10), FIVE(5),TWO(2),ONE(1);
    private int denomination;
    private Coin(int denomination){
        this.denomination = denomination;
    }
    public int getDenomination(){
        return denomination;
    }
}

