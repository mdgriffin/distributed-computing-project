package com.mdgriffin.distributedcomputingproject.server;

public interface Authentication {
    boolean isValidUser (String username, String password);
}
