package com.google.code.joliratools.jaxro.demo;

import com.google.code.joliratools.bind.annotation.RoRootElement;

@RoRootElement
public interface Customer {
    public Account getAccount(String number);

    public Account[] getAccounts();

    String getName();

    boolean isHappy();
}
