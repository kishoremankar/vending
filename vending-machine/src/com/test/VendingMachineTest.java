package com.test;

import com.vendingmachine.Exceptions.NotSufficientChangeException;
import com.vendingmachine.Exceptions.SoldOutException;
import com.vendingmachine.factory.VendingMachineFactory;
import com.vendingmachine.impl.VendingMachineImpl;
import com.vendingmachine.interfaces.VendingMachine;
import com.vendingmachine.model.Bucket;
import com.vendingmachine.model.Coin;
import com.vendingmachine.model.Item;
import org.junit.Ignore;

import java.util.HashMap;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
public class VendingMachineTest {
    private static VendingMachine vm;
    @BeforeClass
    public static void setUp(){
        vm = VendingMachineFactory.createVendingMachine();
    }
@AfterClass
public static void tearDown(){
    vm = null;
}
@Test
public void testBuyItemWithExactPrice() {
    //select item, price in cents
    long price = vm.selectItemAndGetPrice(Item.COKE);
    // price should be Coke's price
    assertEquals(Item.COKE.getPrice(), price); //25 cents paid
    vm.insertCoin(Coin.HUNDRED);
    Bucket<Item, HashMap<Coin,Integer>> bucket = vm.collectItemAndChange();
    Item item = bucket.getFirst();
    HashMap<Coin,Integer> change = bucket.getSecond();
    //should be Coke
    assertEquals(Item.COKE, item);
    //there should not be any change
    assertTrue(change.isEmpty());
}
@Test
public void testBuyItemWithMorePrice(){
    long price = vm.selectItemAndGetPrice(Item.SODA);
    assertEquals(Item.SODA.getPrice(), price);
    vm.insertCoin(Coin.HUNDRED);
    Bucket<Item, HashMap<Coin,Integer>> bucket = vm.collectItemAndChange();
    Item item = bucket.getFirst();
    HashMap<Coin,Integer> change = bucket.getSecond();
    //should be Coke
    assertEquals(Item.SODA, item);
    //there should not be any change
    assertTrue(!change.isEmpty());
    //comparing change
    assertEquals(55 - Item.SODA.getPrice(), getTotal(change));
}
@Test public void testRefund(){
    long price = vm.selectItemAndGetPrice(Item.PEPSI);
    assertEquals(Item.PEPSI.getPrice(), price);
    vm.insertCoin(Coin.HUNDRED);
    vm.insertCoin(Coin.TWENTY);
    vm.insertCoin(Coin.TEN);
    vm.insertCoin(Coin.FIVE);
    vm.insertCoin(Coin.TWO);
    vm.insertCoin(Coin.ONE);
    assertEquals(143, getTotal(vm.refund()));
}
@Test(expected=SoldOutException.class)
public void testSoldOut(){
    for (int i = 0; i < 5; i++) {
        vm.selectItemAndGetPrice(Item.COKE);
        vm.insertCoin(Coin.HUNDRED);
        vm.collectItemAndChange();
    }
}
@Test(expected=NotSufficientChangeException.class)
public void testNotSufficientChangeException(){
    for (int i = 0; i < 5; i++) {
        vm.selectItemAndGetPrice(Item.SODA);
        vm.insertCoin(Coin.TWENTY);
        vm.insertCoin(Coin.TEN);
        vm.insertCoin(Coin.TWENTY);
        vm.collectItemAndChange();
        vm.selectItemAndGetPrice(Item.PEPSI);
        vm.insertCoin(Coin.TWENTY);
        vm.insertCoin(Coin.TWENTY);
        vm.collectItemAndChange();
    }
}
@Test(expected=SoldOutException.class)
public void testReset(){
    VendingMachine vmachine = VendingMachineFactory.createVendingMachine();
    vmachine.reset();
    vmachine.selectItemAndGetPrice(Item.COKE);
}
@Ignore public void testVendingMachineImpl(){
    VendingMachineImpl vm = new VendingMachineImpl();
}
private long getTotal(HashMap<Coin,Integer> change){
    long total = 0;
    for(Coin c : change.keySet()){
        total = total + c.getDenomination();
    } return total;
}
}

