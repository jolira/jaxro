package com.google.code.joliratools.bind.demo;

import java.io.Serializable;
import com.google.code.joliratools.bind.annotation.RoTransient;
import com.google.code.joliratools.bind.annotation.RoType;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

@RoType
public class Account {
    private static final Customer CUSTOMER = new FakeCustomer();

    public NoObject getNothing() {
        return null;
    }

    public Collection<String> getNotices() {
        Collection<String> notices = new ArrayList<String>();

        notices.add("notice1");
        notices.add("notice2");
        notices.add("notice3");
        notices.add("notice4");

        return notices;
    }

    public List<String> getNicknames() {
        List<String> names = new ArrayList<String>();

        names.add("Peter");
        names.add("Paul");
        names.add("Mary");

        return names;
    }

    public List<Customer> getAccountHolders() {
        List<Customer> customers = new ArrayList<Customer>();

        customers.add(CUSTOMER);

        return customers;
    }

    public List getHolders() {
        return null;
    }

    public double getBalance() {
        return 0.0;
    }

    public Serializable getNumber() {
        return "1";
    }

    @RoTransient
    public String getPassword() {
        return "007";
    }

    public AccountStatus getStatus() {
        return AccountStatus.OPEN;
    }

    public String getNew() {
        return "this is new";
    }
    public String getNullString() {
        return null;
    }

    public String[] getNullArray() {
        return null;
    }
    List<Customer> getNullAccountHolders() {
        return null;
    }
}
