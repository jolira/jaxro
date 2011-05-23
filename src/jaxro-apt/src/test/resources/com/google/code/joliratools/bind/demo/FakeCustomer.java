package com.google.code.joliratools.bind.demo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FakeCustomer implements Customer {
    private final Account[] accounts = new Account[] { new Account() };

    public Account getAccount() {
        return new Account();
    }

    public Account getAccount(String number) {
        return new Account();
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public Date getDateOfBirth() {
        Calendar cal = new GregorianCalendar(1872, 7, 9, 0, 0, 0);

        return cal.getTime();
    }

    @Override
    public Double getDouble() {
        return new Double(222.22);
    }

    @Override
    public double getDoublePrimitive() {
        return 222.3;
    }

    @Override
    public Integer getInteger() {
        return new Integer(321);
    }

    @Override
    public int getIntPrimitive() {
        return 321;
    }

    @Override
    public Long getLong() {
        return new Long(333);
    }

    @Override
    public long getLongPrimitive() {
        return 333;
    }

    public String getName() {
        return "Joe Doe";
    }

    public boolean isHappy() {
        return true;
    }

    @Override
    public Boolean isSuperHappy() {
        return new Boolean(true);
    }
}
