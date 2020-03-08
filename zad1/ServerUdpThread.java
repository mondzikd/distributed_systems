package zad1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class ServerUdpThread implements Runnable {
    private int bufferSize;
    private Server server;
    private DatagramSocket udpSocket;

    public ServerUdpThread(Server server, DatagramSocket udpSocket, int bufferSize) {
        this.server = server;
        this.udpSocket = udpSocket;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[bufferSize];
        while(true) {
            Arrays.fill(receiveBuffer, (byte)0);
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            try {
                udpSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // get and strip trailing '\0' signs (because of big bufferSize)
            String message = new String(receivePacket.getData()).replaceAll("[\0]+$", "");

            int senderPortNumber = receivePacket.getPort();
            InetAddress senderAddress = receivePacket.getAddress();

            String nick = server.getNickFromUdpClient(senderPortNumber);

            try {
                server.udpBroadcast(senderPortNumber, senderAddress, nick + ": \n" +  message);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendMessage(Integer portNumber, InetAddress address, String message) throws IOException {
        byte[] answerBuffer = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(answerBuffer, answerBuffer.length, address, portNumber);
        udpSocket.send(sendPacket);
    }
}
