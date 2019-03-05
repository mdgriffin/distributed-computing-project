package com.mdgriffin.distributedcomputingproject.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageTest {

    @Test
    public void withValidMessage_jsonGeneratedCorrectly () {
        Message message = new Message(
            Request.LIST,
            Response.SUCCESS,
            Arrays.asList(new KeyValue("testKey", "testValue")),
        "Body Test"
        );

        try {
            String jsonMessage = message.toJson();
            assertEquals("{\"request\":\"LIST\",\"response\":\"SUCCESS\",\"headers\":[{\"key\":\"testKey\",\"value\":\"testValue\"}],\"body\":\"Body Test\"}", jsonMessage);
        } catch (JsonProcessingException exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void withValidJson_messageWillDeserialize () {

        try {
            Message message = Message.fromJson("{\"request\":\"LIST\",\"response\":\"SUCCESS\",\"headers\":[{\"key\":\"testKey\",\"value\":\"testValue\"}],\"body\":\"Body Test\"}");

            assertEquals(Request.LIST, message.getRequest());
        } catch (IOException exc) {
            fail(exc.getMessage());
        }
    }
}
