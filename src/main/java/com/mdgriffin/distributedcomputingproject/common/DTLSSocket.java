package com.mdgriffin.distributedcomputingproject.common;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import java.security.*;
import javax.net.ssl.*;


public class DTLSSocket {

    private static int MAX_HANDSHAKE_LOOPS = 200;
    private static int MAX_RECEIVE_LOOPS = 60;
    private static int BUFFER_SIZE = 1024;
    private static int MAXIMUM_PACKET_SIZE = 1024;

    private String keyFilename = "./src/main/resources/ssl/server.jks";
    private String trustFilename = "./src/main/resources/ssl/trustedCerts.jks";
    private static String passwd = "password123";
    private InetSocketAddress peerSocketAddr;
    private SSLEngine engine;
    private DatagramSocket socket;

    public DTLSSocket (DatagramSocket socket, InetSocketAddress peerSocketAddr, String side) throws Exception {
        if (side.equals("Client")) {
            keyFilename = "./src/main/resources/ssl/client.jks";
            engine = createSSLEngine(true);
        } else {
            engine = createSSLEngine(false);
        }

        this.socket = socket;
        this.peerSocketAddr = peerSocketAddr;

        // handshaking
        handshake(engine, socket, peerSocketAddr, side);
    }

    SSLEngine createSSLEngine(boolean isClient) throws Exception {
        SSLContext context = getDTLSContext();
        SSLEngine engine = context.createSSLEngine();

        SSLParameters paras = engine.getSSLParameters();
        paras.setMaximumPacketSize(MAXIMUM_PACKET_SIZE);

        engine.setUseClientMode(isClient);
        engine.setSSLParameters(paras);

        return engine;
    }

    // handshake
    void handshake(SSLEngine engine, DatagramSocket socket, SocketAddress peerAddr, String side) throws Exception {

        boolean endLoops = false;
        int loops = MAX_HANDSHAKE_LOOPS;
        engine.beginHandshake();
        while (!endLoops) {

            if (--loops < 0) {
                throw new RuntimeException("Too much loops to produce handshake packets");
            }

            SSLEngineResult.HandshakeStatus hs = engine.getHandshakeStatus();
            if (hs == SSLEngineResult.HandshakeStatus.NEED_UNWRAP ||
                    hs == SSLEngineResult.HandshakeStatus.NEED_UNWRAP_AGAIN) {

                log(side, "Receive DTLS records, handshake status is " + hs);

                ByteBuffer iNet;
                ByteBuffer iApp;
                if (hs == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                    byte[] buf = new byte[BUFFER_SIZE];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        socket.receive(packet);
                    } catch (SocketTimeoutException ste) {
                        log(side, "Warning: " + ste);

                        List<DatagramPacket> packets = new ArrayList<>();
                        boolean finished = onReceiveTimeout(engine, peerAddr, side, packets);

                        for (DatagramPacket p : packets) {
                            socket.send(p);
                        }

                        if (finished) {
                            log(side, "Handshake status is FINISHED "
                                    + "after calling onReceiveTimeout(), "
                                    + "finish the loop");
                            endLoops = true;
                        }

                        log(side, "New handshake status is "
                                + engine.getHandshakeStatus());

                        continue;
                    }

                    iNet = ByteBuffer.wrap(buf, 0, packet.getLength());
                    iApp = ByteBuffer.allocate(BUFFER_SIZE);
                } else {
                    iNet = ByteBuffer.allocate(0);
                    iApp = ByteBuffer.allocate(BUFFER_SIZE);
                }

                SSLEngineResult r = engine.unwrap(iNet, iApp);
                SSLEngineResult.Status rs = r.getStatus();
                hs = r.getHandshakeStatus();
                if (rs == SSLEngineResult.Status.OK) {
                    // OK
                } else if (rs == SSLEngineResult.Status.BUFFER_OVERFLOW) {
                    log(side, "BUFFER_OVERFLOW, handshake status is " + hs);

                    // the client maximum fragment size config does not work?
                    throw new Exception("Buffer overflow: " +
                            "incorrect client maximum fragment size");
                } else if (rs == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                    log(side, "BUFFER_UNDERFLOW, handshake status is " + hs);

                    // bad packet, or the client maximum fragment size
                    // config does not work?
                    if (hs != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                        throw new Exception("Buffer underflow: " +
                                "incorrect client maximum fragment size");
                    } // otherwise, ignore this packet
                } else if (rs == SSLEngineResult.Status.CLOSED) {
                    throw new Exception(
                            "SSL engine closed, handshake status is " + hs);
                } else {
                    throw new Exception("Can't reach here, result is " + rs);
                }

                if (hs == SSLEngineResult.HandshakeStatus.FINISHED) {
                    log(side, "Handshake status is FINISHED, finish the loop");
                    endLoops = true;
                }
            } else if (hs == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                List<DatagramPacket> packets = new ArrayList<>();
                boolean finished = produceHandshakePackets(engine, peerAddr, side, packets);

                for (DatagramPacket p : packets) {
                    socket.send(p);
                }

                if (finished) {
                    log(side, "Handshake status is FINISHED "
                            + "after producing handshake packets, "
                            + "finish the loop");
                    endLoops = true;
                }
            } else if (hs == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                runDelegatedTasks(engine);
            } else if (hs == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                log(side, "Handshake status is NOT_HANDSHAKING, finish the loop");
                endLoops = true;
            } else if (hs == SSLEngineResult.HandshakeStatus.FINISHED) {
                throw new Exception(
                        "Unexpected status, SSLEngine.getHandshakeStatus() "
                                + "shouldn't return FINISHED");
            } else {
                throw new Exception("Can't reach here, handshake status is " + hs);
            }
        }

        SSLEngineResult.HandshakeStatus hs = engine.getHandshakeStatus();
        log(side, "Handshake finished, status is " + hs);

        if (engine.getHandshakeSession() != null) {
            throw new Exception(
                    "Handshake finished, but handshake session is not null");
        }

        SSLSession session = engine.getSession();
        if (session == null) {
            throw new Exception("Handshake finished, but session is null");
        }
        log(side, "Negotiated protocol is " + session.getProtocol());
        log(side, "Negotiated cipher suite is " + session.getCipherSuite());

        // handshake status should be NOT_HANDSHAKING
        // according to the spec, SSLEngine.getHandshakeStatus() can't return FINISHED
        if (hs != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            throw new Exception("Unexpected handshake status " + hs);
        }
    }

    public void send(Message message) throws Exception {
        byte[] messageBytes = message.toJson().getBytes();
        PacketSegmenter packetSegmenter = new PacketSegmenter(messageBytes, MAXIMUM_PACKET_SIZE);

        while (packetSegmenter.hasNext()) {
            byte[] sendBuffer = packetSegmenter.next();
            ByteBuffer source = ByteBuffer.allocate(sendBuffer.length);
            source.put(sendBuffer);
            source.flip();

            ByteBuffer appNet = ByteBuffer.allocate(32768);
            SSLEngineResult r = engine.wrap(source, appNet);
            appNet.flip();

            byte[] ba = new byte[appNet.remaining()];
            appNet.get(ba);

            socket.send(new DatagramPacket(ba, ba.length, peerSocketAddr));
        }
    }

    Message receive() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean isLast = false;

        while (!isLast) {
            int loops = MAX_RECEIVE_LOOPS;
            while (true) {
                if (--loops < 0) {
                    throw new RuntimeException("Too many loops to receive application data");
                }

                byte[] buf = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                ByteBuffer netBuffer = ByteBuffer.wrap(buf, 0, packet.getLength());
                ByteBuffer recBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                SSLEngineResult rs = engine.unwrap(netBuffer, recBuffer);
                recBuffer.flip();
                if (recBuffer.remaining() != 0) {
                    byte[] receiveBuffer = recBuffer.array();
                    outputStream.write(Arrays.copyOfRange(receiveBuffer, 1, receiveBuffer.length));
                    isLast = receiveBuffer[0] != 0;
                    break;
                }
            }
        }

        return Message.fromJson(new String(outputStream.toByteArray()));
    }

    // produce handshake packets
    private boolean produceHandshakePackets(SSLEngine engine, SocketAddress socketAddr,
                                    String side, List<DatagramPacket> packets) throws Exception {

        boolean endLoops = false;
        int loops = MAX_HANDSHAKE_LOOPS;
        while (!endLoops) {

            if (--loops < 0) {
                throw new RuntimeException(
                        "Too much loops to produce handshake packets");
            }

            ByteBuffer oNet = ByteBuffer.allocate(32768);
            ByteBuffer oApp = ByteBuffer.allocate(0);
            SSLEngineResult r = engine.wrap(oApp, oNet);
            oNet.flip();

            SSLEngineResult.Status rs = r.getStatus();
            SSLEngineResult.HandshakeStatus hs = r.getHandshakeStatus();
            if (rs == SSLEngineResult.Status.BUFFER_OVERFLOW) {
                // the client maximum fragment size config does not work?
                throw new Exception("Buffer overflow: " +
                        "incorrect server maximum fragment size");
            } else if (rs == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                log(side, "Produce handshake packets: BUFFER_UNDERFLOW occured");
                log(side, "Produce handshake packets: Handshake status: " + hs);
                // bad packet, or the client maximum fragment size
                // config does not work?
                if (hs != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                    throw new Exception("Buffer underflow: " +
                            "incorrect server maximum fragment size");
                } // otherwise, ignore this packet
            } else if (rs == SSLEngineResult.Status.CLOSED) {
                throw new Exception("SSLEngine has closed");
            } else if (rs == SSLEngineResult.Status.OK) {
                // OK
            } else {
                throw new Exception("Can't reach here, result is " + rs);
            }

            // SSLEngineResult.Status.OK:
            if (oNet.hasRemaining()) {
                byte[] ba = new byte[oNet.remaining()];
                oNet.get(ba);
                DatagramPacket packet = createHandshakePacket(ba, socketAddr);
                packets.add(packet);
            }

            if (hs == SSLEngineResult.HandshakeStatus.FINISHED) {
                log(side, "Produce handshake packets: "
                        + "Handshake status is FINISHED, finish the loop");
                return true;
            }

            boolean endInnerLoop = false;
            SSLEngineResult.HandshakeStatus nhs = hs;
            while (!endInnerLoop) {
                if (nhs == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                    runDelegatedTasks(engine);
                } else if (nhs == SSLEngineResult.HandshakeStatus.NEED_UNWRAP ||
                        nhs == SSLEngineResult.HandshakeStatus.NEED_UNWRAP_AGAIN ||
                        nhs == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {

                    endInnerLoop = true;
                    endLoops = true;
                } else if (nhs == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                    endInnerLoop = true;
                } else if (nhs == SSLEngineResult.HandshakeStatus.FINISHED) {
                    throw new Exception(
                            "Unexpected status, SSLEngine.getHandshakeStatus() "
                                    + "shouldn't return FINISHED");
                } else {
                    throw new Exception("Can't reach here, handshake status is "
                            + nhs);
                }
                nhs = engine.getHandshakeStatus();
            }
        }

        return false;
    }

    DatagramPacket createHandshakePacket(byte[] ba, SocketAddress socketAddr) {
        return new DatagramPacket(ba, ba.length, socketAddr);
    }

    // run delegated tasks
    void runDelegatedTasks(SSLEngine engine) throws Exception {
        Runnable runnable;
        while ((runnable = engine.getDelegatedTask()) != null) {
            runnable.run();
        }

        SSLEngineResult.HandshakeStatus hs = engine.getHandshakeStatus();
        if (hs == SSLEngineResult.HandshakeStatus.NEED_TASK) {
            throw new Exception("handshake shouldn't need additional tasks");
        }
    }

    // retransmission if timeout
    boolean onReceiveTimeout(SSLEngine engine, SocketAddress socketAddr,
                             String side, List<DatagramPacket> packets) throws Exception {

        SSLEngineResult.HandshakeStatus hs = engine.getHandshakeStatus();
        if (hs == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            return false;
        } else {
            // retransmission of handshake messages
            return produceHandshakePackets(engine, socketAddr, side, packets);
        }
    }

    // get DTSL context
    SSLContext getDTLSContext() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore ts = KeyStore.getInstance("JKS");

        char[] passphrase = passwd.toCharArray();

        try (FileInputStream fis = new FileInputStream(keyFilename)) {
            ks.load(fis, passphrase);
        }

        try (FileInputStream fis = new FileInputStream(trustFilename)) {
            ts.load(fis, passphrase);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);

        SSLContext sslCtx = SSLContext.getInstance("DTLS");

        sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslCtx;
    }

    static void log(String side, String message) {
        System.out.println(side + ": " + message);
    }
}