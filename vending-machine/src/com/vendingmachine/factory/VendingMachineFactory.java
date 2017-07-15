package com.vendingmachine.factory;

import com.vendingmachine.impl.VendingMachineImpl;
import com.vendingmachine.interfaces.VendingMachine;

/**
 * Created by 212587383 on 7/11/2017.
 */
public class VendingMachineFactory {
    public static VendingMachine createVendingMachine() {
        return new VendingMachineImpl();
    }
}

