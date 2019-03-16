package com.mdgriffin.distributedcomputingproject.server;

import java.util.Date;

public interface Session {

    String getId ();

    Date getExpiry ();

    String getUsername();

}
