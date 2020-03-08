package zad1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.lang.String;

public class ClientUdpReader implements Runnable{
    private DatagramSocket socket;
    private int bufferSize;

    public ClientUdpReader(DatagramSocket udpSocket, int bufferSize) {
        this.socket = udpSocket;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[bufferSize];
        String message = "";
        do {
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            try {
                socket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // get and strip trailing '\0' signs (because of big bufferSize)
            message = new String(receivePacket.getData()).replaceAll("[\0]+$", "");
            System.out.println("[UDP message]: " + message);
        } while (!message.equals("exit"));
    }
}
