package com.mdgriffin.distributedcomputingproject.common;

import java.util.List;

public class Message {
    private OperationCodes operationCode;
    private StatusCodes statusCode;
    List<KeyValue> headers;
    String body;

    public Message (OperationCodes operationCode, StatusCodes statusCode, List<KeyValue> headers, String body) {
        this.operationCode = operationCode;
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

}