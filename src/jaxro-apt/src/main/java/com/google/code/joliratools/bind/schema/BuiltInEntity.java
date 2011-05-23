package com.google.code.joliratools.bind.schema;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.reflect.ClassAdapter;

public final class BuiltInEntity extends Entity {
    static final BuiltInEntity STRING = new BuiltInEntity("xs:string", true);

    private static void add(final HashMap<String, Entity> builtins,
            final java.lang.Class<?> clazz, final BuiltInEntity entity) {
        final Class _class = new ClassAdapter(clazz);
        final String name = _class.getName();

        entity.compile(_class, null);
        builtins.put(name, entity);
    }

    static Map<String, Entity> getBuiltIns() {
        final HashMap<String, Entity> builtins = new HashMap<String, Entity>();

        add(builtins, int.class, new BuiltInEntity("xs:int", false));
        add(builtins, short.class, new BuiltInEntity("xs:short", false));
        add(builtins, long.class, new BuiltInEntity("xs:long", false));
        add(builtins, float.class, new BuiltInEntity("xs:float", false));
        add(builtins, double.class, new BuiltInEntity("xs:double", false));
        add(builtins, byte.class, new BuiltInEntity("xs:byte", false));
        add(builtins, char.class, new BuiltInEntity("xs:string", false));
        add(builtins, boolean.class, new BuiltInEntity("xs:boolean", false));
        add(builtins, String.class, STRING);
        add(builtins, Integer.class, new BuiltInEntity("xs:integer", true));
        add(builtins, Short.class, new BuiltInEntity("xs:short", true));
        add(builtins, Long.class, new BuiltInEntity("xs:long", true));
        add(builtins, Float.class, new BuiltInEntity("xs:float", true));
        add(builtins, Double.class, new BuiltInEntity("xs:double", true));
        add(builtins, Byte.class, new BuiltInEntity("xs:byte", true));
        add(builtins, Character.class, new BuiltInEntity("xs:string", true));
        add(builtins, Boolean.class, new BuiltInEntity("xs:boolean", true));
        add(builtins, URL.class, new BuiltInEntity("xs:anyURI", true));
        add(builtins, BigDecimal.class, new BuiltInEntity("xs:decimal", true));
        add(builtins, BigInteger.class, new BuiltInEntity("xs:integer", true));
        add(builtins, Date.class, new BuiltInEntity("xs:dateTime", true));

        return builtins;
    }

    private final boolean isObject;

    private BuiltInEntity(final String name, final boolean isObject) {
        super(name);

        this.isObject = isObject;
    }

    @Override
    void compile(final Class clazz, final SchemaResolver resolver) {
        super.compile(clazz, resolver);
    }

    public boolean isObject() {
        return isObject;
    }

    @Override
    public <T> T visit(final EntityVisitor<T> visitor) {
        if (!isCompiled()) {
            throw new IllegalStateException();
        }

        return visitor.visit(this);
    }
}
