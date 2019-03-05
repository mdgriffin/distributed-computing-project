package com.mdgriffin.distributedcomputingproject.common;

public class KeyValue {

    private String key;
    private String value;

    public KeyValue (String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String key () {
        return key;
    }

    public String value () {
        return value;
    }
}
