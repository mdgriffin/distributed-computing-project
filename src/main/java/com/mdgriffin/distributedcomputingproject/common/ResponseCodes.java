package com.mdgriffin.distributedcomputingproject.common;

public enum ResponseCodes {
    SUCCESS(101),
    ERROR(102),
    DENIED(103),
    NOT_FOUND(104);

    private int code;

    ResponseCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
