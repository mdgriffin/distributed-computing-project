package com.mdgriffin.distributedcomputingproject.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageTest {

    @Test
    public void withValidMessage_jsonGeneratedCorrectly () {
        Message message = new Message(
            RequestCodes.LIST,
            ResponseCodes.SUCCESS,
            Arrays.asList(new KeyValue("testKey", "testValue")),
        "Body Test"
        );

        try {
            String jsonMessage = message.toJson();
            assertEquals("{\"operationCode\":\"LIST\",\"statusCode\":\"SUCCESS\",\"headers\":[{\"key\":\"testKey\",\"value\":\"testValue\"}],\"body\":\"Body Test\"}", jsonMessage);
        } catch (JsonProcessingException exc) {
            fail(exc.getMessage());
        }
    }
}
