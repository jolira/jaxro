package com.google.code.joliratools.bind.demo;

import com.google.code.joliratools.bind.annotation.*;
import java.util.*;

@RoRootElement
public interface Customer {
    public Account getAccount(String number);

    public Account[] getAccounts();

    String getName();

    boolean isHappy();

    @RoQualifier
    Date getDateOfBirth();

    public Account getAccount();
    
    public Boolean isSuperHappy();
    
    public int getIntPrimitive();
    
    public Integer getInteger();
    
    public double getDoublePrimitive();
    
    public Double getDouble();

    public long getLongPrimitive();
    
    public Long getLong();
    
    Calendar getLastSeen();
    
    GregorianCalendar getLastPurchase();
}
