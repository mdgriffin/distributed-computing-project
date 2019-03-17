package com.mdgriffin.distributedcomputingproject.common;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PacketSegmenterTest {

    @Test
    public void whenSegmentingPacket_correctNumPackets () {
        int packetSize = 10;
        byte[] bytes = new byte[100];
        Arrays.fill(bytes,(byte) 1 );

        PacketSegmenter segmenter = new PacketSegmenter(bytes, packetSize);

        assertEquals(100, bytes.length);
        assertEquals(12, segmenter.getNumPackets());
    }

    @Test
    public void whenSegmentingPacket_byteArrayCorrectSize () {
        int packetSize = 10;
        byte[] bytes = new byte[100];
        Arrays.fill( bytes, (byte) 1 );

        PacketSegmenter segmenter = new PacketSegmenter(bytes, packetSize);

        assertTrue(segmenter.hasNext());

        byte[] segment = segmenter.next();

        assertEquals(10, segment.length);
    }

    @Test
    public void whenGettingFirstSegments_lastIndicatorFalse () {
        int packetSize = 10;
        byte[] bytes = new byte[100];
        Arrays.fill( bytes, (byte) 1 );

        PacketSegmenter segmenter = new PacketSegmenter(bytes, packetSize);

        assertTrue(segmenter.hasNext());

        byte[] segment = segmenter.next();
        byte controlChar = segment[0];

        System.out.println(controlChar);

        boolean isLast = controlChar != 0;

        assertFalse(isLast);
    }

    @Test
    public void whenGettingLastSegment_lastIndicatorTrue () {
        int packetSize = 120;
        byte[] bytes = new byte[100];
        Arrays.fill( bytes, (byte) 1 );

        PacketSegmenter segmenter = new PacketSegmenter(bytes, packetSize);

        assertEquals(1, segmenter.getNumPackets());
        assertTrue(segmenter.hasNext());

        byte[] segment = segmenter.next();

        assertFalse(segmenter.hasNext());
        assertEquals(101 ,segment.length);

        byte controlChar = segment[0];

        System.out.println(controlChar);

        boolean isLast = controlChar != 0;

        assertTrue(isLast);
    }

    @Test
    public void whenGettingSegments_allSegmentsReturned () {
        int packetSize = 20;
        byte[] bytes = new byte[100];
        Arrays.fill( bytes, (byte) 1 );

        PacketSegmenter segmenter = new PacketSegmenter(bytes, packetSize);

        assertEquals(6, segmenter.getNumPackets());

        int numCalls = 0;

        while (segmenter.hasNext()) {
            byte[] segment = segmenter.next();

            if (numCalls == segmenter.getNumPackets() - 1) {
                assertEquals(6, segment.length);
            } else {
                assertEquals(20, segment.length);
            }

            numCalls++;
        }

        assertFalse(segmenter.hasNext());
        assertEquals(6, numCalls);
    }
}
