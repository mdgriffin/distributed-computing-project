package com.mdgriffin.distributedcomputingproject.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Message {
    private RequestCodes operationCode;
    private ResponseCodes statusCode;
    List<KeyValue> headers;
    String body;

    public Message (RequestCodes operationCode, ResponseCodes statusCode, List<KeyValue> headers, String body) {
        this.operationCode = operationCode;
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public RequestCodes getOperationCode() {
        return operationCode;
    }

    public ResponseCodes getStatusCode() {
        return statusCode;
    }

    public List<KeyValue> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String toJson () throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
