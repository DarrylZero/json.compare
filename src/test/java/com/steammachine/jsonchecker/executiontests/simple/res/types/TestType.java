package com.steammachine.jsonchecker.executiontests.simple.res.types;

import com.steammachine.jsonchecker.types.UnmappedProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 30.12.2017 10:21:46
 *
 * @author Vladimir Bogodukhov
 *         {@link com.steammachine.jsonchecker.executiontests.simple.res.types.TestType}
 *         com.steammachine.jsonchecker.executiontests.simple.res.types.TestType
 **/
public class TestType implements UnmappedProperties {

    private String name;
    private String field1 = "Vale la pena !!!";
    private int field2;
    private final Map<String, String> unmapped = new HashMap<>();

    public Map<String, String> unmapped() {
        return unmapped;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public int getField2() {
        return field2;
    }

    public void setField2(int field2) {
        this.field2 = field2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestType)) return false;

        TestType type = (TestType) o;

        if (field2 != type.field2) return false;
        if (name != null ? !name.equals(type.name) : type.name != null) return false;
        if (field1 != null ? !field1.equals(type.field1) : type.field1 != null) return false;
        return unmapped.equals(type.unmapped);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (field1 != null ? field1.hashCode() : 0);
        result = 31 * result + field2;
        result = 31 * result + unmapped.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TestType{" +
                "field1='" + field1 + '\'' +
                ", field2=" + field2 +
                ", unmapped=" + unmapped +
                '}';
    }

    @Override
    public void setValue(String propertyName, String value) {
        unmapped.put(propertyName, value);
    }
}
