package com.mdgriffin.distributedcomputingproject.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class SessionTest {

    @Test
    public void whenCreatingSession_uniqueSessionIdGenerated () {
        Session session1 = new SessionImpl("");
        Session session2 = new SessionImpl("");

        assertFalse(session1.getId().equals(session2.getId()));
    }

}
