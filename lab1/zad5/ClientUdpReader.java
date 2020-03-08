package zad1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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

            message = new String(receivePacket.getData());
            System.out.println("[UDP] " + message);
            // TODO: condition in while should check 4 letters not the whole 8192 bytes
        } while (!message.equals("exit"));
    }
}
