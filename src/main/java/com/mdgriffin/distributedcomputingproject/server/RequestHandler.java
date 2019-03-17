package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.Message;

public interface RequestHandler {

    Message login(Message message);

    Message list (Message message);

    Message upload (Message message);

}
