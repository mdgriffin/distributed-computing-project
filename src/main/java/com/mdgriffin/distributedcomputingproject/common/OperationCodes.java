package com.mdgriffin.distributedcomputingproject.common;

public enum OperationCodes {
    LOGIN(100),
    LOGOFF(101),
    UPLOAD(102),
    DOWNLOAD(103),
    LIST(104);

    private int code;

    OperationCodes(int code) {
        this.code = code;
    }

    public int getCode () {
        return this.code;
    }
}
