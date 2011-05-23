package com.google.code.joliratools.jaxro.demo;

import com.google.code.joliratools.bind.annotation.RoTransient;

public class Account {
    public double getBalance() {
        return 0.0;
    }

    public String getNumber() {
        return "1";
    }

    @RoTransient
    public String getPassword() {
        return "007";
    }

    public AccountStatus getStatus() {
        return AccountStatus.OPEN;
    }
}
