package com.google.code.joliratools.bind.demo;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class JSONExecutor {
    public static String execute() {
        final Customer customer = new FakeCustomer();
        final Customer[] customers = new Customer[] { customer };
        final CustomerArrayJSONAdapter adapter = new CustomerArrayJSONAdapter(
                customers);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintWriter writer = new PrintWriter(out);

        try {
            adapter.toJSON(writer);
        } finally {
            writer.close();
        }

        return out.toString();
    }

    public static void main(String args[]) {
        System.out.println(execute());
    }
}
