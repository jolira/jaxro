package com.google.code.joliratools.bind.demo;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class Executor {
    public static String execute() {
        final Customer customer = new FakeCustomer();
        final Customer[] customers = new Customer[] { customer };
        final CustomerArrayJAXROAdapter adapter = new CustomerArrayJAXROAdapter(customers);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintWriter writer = new PrintWriter(out);
        
        try {
            adapter.toXML(writer);
        }
        finally {
            writer.close();
        }
        
        return out.toString();
    }
    

}
