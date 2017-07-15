package com.vendingmachine.impl;

import com.vendingmachine.Exceptions.NotSufficientChangeException;
import com.vendingmachine.Exceptions.SoldOutException;
import com.vendingmachine.model.Inventory;
import com.vendingmachine.model.Item;
import com.vendingmachine.Exceptions.NotFullPaidException;
import com.vendingmachine.interfaces.VendingMachine;
import com.vendingmachine.model.Bucket;
import com.vendingmachine.model.Coin;

import java.util.HashMap;

/**
 * Created by 212587383 on 7/11/2017.
 */
public class VendingMachineImpl implements VendingMachine {
    private Inventory<Object> cashInventory = new Inventory<Object>();
    private Inventory<Item> itemInventory = new Inventory<Item>();
    private long totalSales;
    private Item currentItem;
    private long currentBalance;

    public VendingMachineImpl(){
        initialize();
    }

    private void initialize(){
        //initialize machine with 5 coins of each denomination
        //and 5 cans of each Item
        for(Coin c : Coin.values()){
            cashInventory.put(c, 5);
        }

        for(Item i : Item.values()){
            itemInventory.put(i, 5);
        }

    }

    @Override
    public long selectItemAndGetPrice(Item item) {
        if(itemInventory.hasItem(item)){
            currentItem = item;
            return currentItem.getPrice();
        }
        throw new SoldOutException("Sold Out, Please buy another item");
    }

    @Override
    public void insertCoin(Coin coin) {
        currentBalance = currentBalance + coin.getDenomination();
        cashInventory.add(coin);
    }

    @Override
    public Bucket<Item, HashMap<Coin,Integer>> collectItemAndChange() {
        Item item = collectItem();
        totalSales = totalSales + currentItem.getPrice();

        HashMap<Coin,Integer> change = collectChange();

        return new Bucket<Item, HashMap<Coin,Integer>>(item, change);
    }

    private Item collectItem() throws NotSufficientChangeException,
            NotFullPaidException {
        if(isFullPaid()){
            if(hasSufficientChange()){
                itemInventory.deduct(currentItem);
                return currentItem;
            }
            throw new NotSufficientChangeException("Not Sufficient change in Inventory");

        }
        long remainingBalance = currentItem.getPrice() - currentBalance;
        throw new NotFullPaidException("Price not full paid, remaining : ",
                remainingBalance);
    }

    private HashMap<Coin,Integer> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        HashMap<Coin,Integer> change = getChange(changeAmount);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }

    @Override
    public HashMap<Coin,Integer> refund(){
        HashMap<Coin,Integer> refund = getChange(currentBalance);
        updateCashInventory(refund);
        currentBalance = 0;
        currentItem = null;
        return refund;
    }


    private boolean isFullPaid() {
        if(currentBalance >= currentItem.getPrice()){
            return true;
        }
        return false;
    }


    public HashMap<Coin,Integer> getChange(long remaining) throws NotSufficientChangeException{
        HashMap<Coin,Integer> coinMap = new HashMap<>();
        long balance = remaining;
        while (balance > 0) {
            if (balance >= Coin.HUNDRED.getDenomination() && cashInventory.hasItem(Coin.HUNDRED) && cashInventory.getQuantity(Coin.HUNDRED) >= balance/Coin.HUNDRED.getDenomination()) {
                coinMap.put(Coin.HUNDRED,coinMap.get(Coin.HUNDRED) == null ? 1 : coinMap.get(Coin.HUNDRED)+1);
                balance-=Coin.HUNDRED.getDenomination();
                continue;
            } else if (balance >= Coin.TWENTY.getDenomination() && cashInventory.hasItem(Coin.TWENTY) && cashInventory.getQuantity(Coin.TWENTY) >= balance/Coin.TWENTY.getDenomination()) {
                coinMap.put(Coin.TWENTY,coinMap.get(Coin.TWENTY) == null ? 1 : coinMap.get(Coin.TWENTY)+1);
                balance-=Coin.TWENTY.getDenomination();
                continue;
            } else if (balance >= Coin.TEN.getDenomination() && cashInventory.hasItem(Coin.TEN) && cashInventory.getQuantity(Coin.TEN) >= balance/Coin.TEN.getDenomination()) {
                coinMap.put(Coin.TEN,coinMap.get(Coin.TEN) == null ? 1 : coinMap.get(Coin.TEN)+1);
                balance-=Coin.TEN.getDenomination();
                continue;
            }else if (balance >= Coin.FIVE.getDenomination() && cashInventory.hasItem(Coin.FIVE) && cashInventory.getQuantity(Coin.FIVE) >= balance/Coin.FIVE.getDenomination()) {
                coinMap.put(Coin.FIVE,coinMap.get(Coin.FIVE) == null ? 1 : coinMap.get(Coin.FIVE)+1);
                balance-=Coin.FIVE.getDenomination();
                continue;
            }
            else if (balance >= Coin.TWO.getDenomination() && cashInventory.hasItem(Coin.TWO) && cashInventory.getQuantity(Coin.TWO) >= balance/Coin.TWO.getDenomination()) {
                coinMap.put(Coin.TWO,coinMap.get(Coin.TWO) == null ? 1 : coinMap.get(Coin.TWO)+1);
                balance-=Coin.TWO.getDenomination();
                continue;
            }  else if (balance >= Coin.ONE.getDenomination() && cashInventory.hasItem(Coin.ONE) && cashInventory.getQuantity(Coin.ONE) >= balance/Coin.ONE.getDenomination()) {
                coinMap.put(Coin.ONE,coinMap.get(Coin.ONE) == null ? 1 : coinMap.get(Coin.ONE)+1);
                balance-=Coin.ONE.getDenomination();
                continue;
            } else {
                throw new NotSufficientChangeException("Not sufficient Change Exception, please try another product");
            }
        }
        return coinMap;
    }

    @Override
    public void reset(){
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentItem = null;
        currentBalance = 0;
    }

    public void printStats(){
        System.out.println("Total Sales : " + totalSales);
        System.out.println("Current Item Inventory : " + itemInventory);
        System.out.println("Current Cash Inventory : " + cashInventory);
    }


    private boolean hasSufficientChange(){
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
    }

    private boolean hasSufficientChangeForAmount(long amount){
        boolean hasChange = true;
        try{
            getChange(amount);
        }catch(NotSufficientChangeException nsce){
            return hasChange = false;
        }

        return hasChange;
    }

    private void updateCashInventory(HashMap<Coin,Integer> change) {
        for(Coin c : change.keySet()){
            cashInventory.deduct(c);
        }
    }

    public long getTotalSales(){
        return totalSales;
    }



}


