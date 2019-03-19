package com.mdgriffin.distributedcomputingproject.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void whenGettingHeaderValue_withValidKey_valueReturned () {
        Message message = new Message(
            Request.DOWNLOAD,
            null,
            Arrays.asList(new KeyValue("test1", "Example 1"), new KeyValue("test2", "Example 1")),
            ""
        );

        String value = message.getHeaderValue("test1");
        assertEquals("Example 1", value);
    }

    @Test()
    public void whenGettingHeaderValue_withMissingKey_exceptionThrown () {
        Message message = new Message(
                Request.DOWNLOAD,
                null,
                Arrays.asList(new KeyValue("test1", "Example 1"), new KeyValue("test2", "Example 1")),
                ""
        );

        assertThrows(InvalidParameterException.class, () -> message.getHeaderValue("test3"));
    }
}
