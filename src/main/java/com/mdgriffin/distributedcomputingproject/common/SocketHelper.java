package com.mdgriffin.distributedcomputingproject.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SocketHelper {

    private DatagramSocket socket;
    private static final int MAX_LEN = 100;

    public SocketHelper (DatagramSocket socket) {
        this.socket = socket;
    }

    public void send (DatagramMessage message) throws IOException {
        byte[] sendBuffer = message.getMessage().getBytes();
        InetAddress hostAddress = InetAddress.getByName(message.getAddress());

        DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, hostAddress, message.getPortNum());
        socket.send(packet);
    }

    public DatagramMessage receive () throws IOException {
        byte[] receiveBuffer = new byte[MAX_LEN];
        DatagramPacket datagram = new DatagramPacket(receiveBuffer, MAX_LEN);
        socket.receive(datagram);
        return new DatagramMessage(new String(receiveBuffer), datagram.getAddress().getHostAddress(), datagram.getPort());
    }
}
