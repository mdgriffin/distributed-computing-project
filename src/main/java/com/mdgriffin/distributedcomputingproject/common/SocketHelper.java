package com.mdgriffin.distributedcomputingproject.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

// TODO: Make Observable
public class SocketHelper {

    private DatagramSocket socket;
    private static final int MAX_LEN = 200;


    public SocketHelper (DatagramSocket socket) {
        this.socket = socket;
    }

    public void send (DatagramMessage message) throws IOException {
        byte[] sendBuffer = message.getMessage().getBytes();
        InetAddress hostAddress = InetAddress.getByName(message.getAddress());
        PacketSegmenter packetSegmenter = new PacketSegmenter(sendBuffer, MAX_LEN);

        while (packetSegmenter.hasNext()) {
            byte[] packet = packetSegmenter.next();
            socket.send(new DatagramPacket(packet, packet.length, hostAddress, message.getPortNum()));
        }
    }

    public DatagramMessage receive () throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        boolean isLast = false;
        DatagramPacket datagram;
        byte[] receiveBuffer = new byte[MAX_LEN];
        datagram = new DatagramPacket(receiveBuffer, MAX_LEN);

        while (!isLast) {
            receiveBuffer = new byte[MAX_LEN];
            datagram = new DatagramPacket(receiveBuffer, MAX_LEN);
            socket.receive(datagram);
            outputStream.write(Arrays.copyOfRange(receiveBuffer, 1, receiveBuffer.length));
            isLast = receiveBuffer[0] != 0;
        }

        return new DatagramMessage(new String(outputStream.toByteArray()), datagram.getAddress().getHostAddress(), datagram.getPort());
    }
}
