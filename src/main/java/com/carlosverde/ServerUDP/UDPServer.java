package com.carlosverde.ServerUDP;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer extends Thread {

    private DatagramSocket socket;
    private int port;
    private byte[] buf = new byte[256];
    private String received = "";
    private boolean stop = false;

    public UDPServer(int port, int tamano) throws SocketException {
        createServer(port);
        setBufLength(tamano);
    }

    public void createServer(int port)throws SocketException {
        this.port = port;
        socket = new DatagramSocket(this.port);

        System.out.println("[UDP] Escuchando por el puerto "+ port);
    }

    public void setBufLength(int tamano) {
        this.buf = new byte[tamano*64];
    }

    public String getReceived() {
        return received;
    }

    public void stopServer() {
        this.stop = true;
        socket.close();
    }

    public void run() {

        while (!stop) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);


                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                received
                        += new String(packet.getData(), 0, packet.getLength());
//                System.out.println("Recibiendo: " + Base64.base64Decode(received.trim()));

            }
            catch (IOException e) {}
            catch (Exception e){
                e.printStackTrace();
            }
        }
        socket.close();
    }

}
