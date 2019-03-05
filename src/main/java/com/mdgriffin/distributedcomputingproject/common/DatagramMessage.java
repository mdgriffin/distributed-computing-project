package com.mdgriffin.distributedcomputingproject.common;

public class DatagramMessage {

    private String message;
    private String address;
    private  int portNum;

    public DatagramMessage (String message, String address, int portNum) {
        this.message = message;
        this.address = address;
        this.portNum = portNum;
    }

    public String getMessage() {
        return message;
    }

    public String getAddress() {
        return address;
    }

    public int getPortNum() {
        return portNum;
    }
}
