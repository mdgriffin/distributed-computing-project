package com.mdgriffin.distributedcomputingproject.common;

public enum StatusCodes {
    SUCCESS(101),
    ERROR(102),
    DENIED(103),
    NOT_FOUND(104);

    private int code;

    StatusCodes (int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
