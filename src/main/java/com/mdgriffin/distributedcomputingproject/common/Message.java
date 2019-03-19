package com.mdgriffin.distributedcomputingproject.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

public class Message {
    private Request request;
    private Response response;
    List<KeyValue> headers;
    String body;

    public Message (Request request, Response response, List<KeyValue> headers, String body) {
        this.request = request;
        this.response = response;
        this.headers = headers;
        this.body = body;
    }

    public Message () {}

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public List<KeyValue> getHeaders() {
        return headers;
    }

    public String getHeaderValue (String key) throws InvalidParameterException {
        return headers.stream().filter(name -> name.getKey().equals(key)).findFirst().orElseThrow(() -> new InvalidParameterException()).getValue();
    }

    public String getBody() {
        return body;
    }

    public String toJson () throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static Message fromJson (String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, Message.class);
    }
}
