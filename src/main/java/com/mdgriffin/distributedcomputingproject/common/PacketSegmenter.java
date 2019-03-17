package com.mdgriffin.distributedcomputingproject.common;

import com.google.common.primitives.Bytes;

import java.util.Arrays;
import java.util.Iterator;

public class PacketSegmenter  implements Iterator<byte[]> {

    private byte[] packet;
    private int contentSize;
    private int currentOffset;
    private static final int CONTROL_LEN = 1;

    public PacketSegmenter (byte[] packet, int packetSize) {
        this.packet = packet;
        this.currentOffset = 0;

        contentSize = packetSize - CONTROL_LEN;
    }

    @Override
    public boolean hasNext() {
        return currentOffset < getNumPackets();
    }

    @Override
    public byte[] next() {
        int startIndex = contentSize * currentOffset;
        int bytesRemaining = packet.length - (currentOffset * contentSize);
        int bytesInPacket = bytesRemaining < contentSize? bytesRemaining : contentSize;
        byte[] segment = Arrays.copyOfRange(packet, startIndex, startIndex + bytesInPacket);
        byte isLast =  (byte)(currentOffset == getNumPackets() - 1? 1 : 0);

        currentOffset++;

        return addControlChar(new byte[]{isLast}, segment);
    }

    public int getNumPackets () {
        return ((int) Math.ceil((double)packet.length / contentSize));
    }

    private byte[] addControlChar (byte[] controlHeader, byte[] packet) {
        return Bytes.concat(controlHeader, packet);
    }

}
