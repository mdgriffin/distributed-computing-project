package com.mdgriffin.distributedcomputingproject.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketHelper {

    private DatagramSocket socket;
    private static final int MAX_LEN = 100;

    public SocketHelper (DatagramSocket socket) {
        this.socket = socket;
    }

    public void send (String host, int portNum, String message) throws IOException {
        byte[] sendBuffer = message.getBytes();
        InetAddress hostAddress = InetAddress.getByName(host);

        DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, hostAddress, portNum);
        socket.send(packet);
    }

    public String receive () throws IOException {
        byte[] receiveBuffer = new byte[MAX_LEN];
        DatagramPacket datagram = new DatagramPacket(receiveBuffer, MAX_LEN);
        socket.receive(datagram);
        return new String(receiveBuffer);
    }
}
