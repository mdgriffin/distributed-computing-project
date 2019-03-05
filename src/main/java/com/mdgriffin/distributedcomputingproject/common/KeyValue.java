package com.mdgriffin.distributedcomputingproject.common;

public class KeyValue {

    private String key;
    private String value;

    public KeyValue (String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue () {}

    public String getKey () {
        return key;
    }

    public String getValue () {
        return value;
    }


}
