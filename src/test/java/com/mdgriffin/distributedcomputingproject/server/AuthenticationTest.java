package com.mdgriffin.distributedcomputingproject.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationTest {

    private Authentication auth = new ListAuthentication();

    @Test
    public void user_withValidDetail_canLogin () {
        assertEquals(true, auth.isValidUser("jdoe", "password123"));
    }

    @Test
    public void user_withinvalidDetails_cannotLogin () {
        assertEquals(false, auth.isValidUser("jdoe22", "password123"));
        assertEquals(false, auth.isValidUser("jdoe", "password1234"));
        assertEquals(false, auth.isValidUser(null, null));
    }

}
