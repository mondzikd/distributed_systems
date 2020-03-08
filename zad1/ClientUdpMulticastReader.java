package zad1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ClientUdpMulticastReader implements Runnable {
    private MulticastSocket socket;
    private int bufferSize;

    public ClientUdpMulticastReader(MulticastSocket socket, int bufferSize) {
        this.socket = socket;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[bufferSize];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            try {
                socket.receive(receivePacket);
            } catch (IOException e) {
                System.out.println("Multicast thread terminated");
                break;
            }

            // get and strip trailing '\0' signs (because of big bufferSize)
            String message = new String(receivePacket.getData()).replaceAll("[\0]+$", "");
            System.out.println("[UDP multicast]:\n" + message);
        }
    }
}
