package com.vendingmachine.interfaces;

import com.vendingmachine.model.Item;
import com.vendingmachine.model.Bucket;
import com.vendingmachine.model.Coin;

import java.util.HashMap;

/**
 * Created by 212587383 on 7/11/2017.
 */
public interface VendingMachine{
    public long selectItemAndGetPrice(Item item);
    public void insertCoin(Coin coin);
    public HashMap<Coin,Integer> refund();
    public Bucket<Item, HashMap<Coin,Integer>> collectItemAndChange();
    public void reset();

}

