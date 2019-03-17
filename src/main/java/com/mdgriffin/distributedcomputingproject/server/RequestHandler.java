package com.mdgriffin.distributedcomputingproject.server;

import com.mdgriffin.distributedcomputingproject.common.DatagramMessage;
import com.mdgriffin.distributedcomputingproject.common.Message;

import java.io.IOException;

public interface RequestHandler {

    Message login(Message message);

    Message list (Message message);

}
