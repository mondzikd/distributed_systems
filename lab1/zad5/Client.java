package zad1;

// TODO: Client should send udp Socket address instead of portNumber

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int portNumber;
    private String hostName;
    private String nick;

    private Socket tcpSocket;
    private DatagramSocket udpSocket;

    public Client(int portNumber, String hostName, String nick) {
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.nick = nick;
    }

    public void execute() throws IOException {

        try {
            // create tcp socket
            tcpSocket = new Socket(hostName, portNumber);

            // create udp socket and connect it to the server
            udpSocket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(hostName);
            udpSocket.connect(address, portNumber);

            // status message
            System.out.println("Connected to the chat server");

            // in & out tcp streams
            PrintWriter tcpOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            BufferedReader tcpIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

            // send first message with nickname and UDP socket local port number
            // TODO: change portNumber to SocketAddress
            tcpOut.println(nick + " " + udpSocket.getLocalPort());

            // create TcpReader, UdpReader and Writer threads
            Thread tcpReaderThread = new Thread(new ClientTcpReader(tcpIn));
            Thread udpReaderThread = new Thread(new ClientUdpReader(udpSocket, 8192));
            Thread writerThread = new Thread(new ClientWriter(tcpOut, udpSocket));
            tcpReaderThread.start();
            udpReaderThread.start();
            writerThread.start();
            tcpReaderThread.join();
            udpReaderThread.join();
            writerThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Is it really needed?
            if (tcpSocket != null){
                tcpSocket.close();
            }
            if (udpSocket.isConnected()) {
                udpSocket.disconnect();
            }
            if (udpSocket != null) {
                udpSocket.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int portNumber = 12345;
        String hostName = "localhost";
        String nick = "User";

        if (args.length == 3) {
            portNumber = Integer.parseInt(args[0]);
            hostName = args[1];
            nick = args[2];
        }
        else {
            System.out.print("Your nickname: ");
            Scanner scanner = new Scanner(System.in);
            nick = scanner.next();
        }

        System.out.println("Java TCP-UDP chat client");
        System.out.println("Server port number: " + portNumber);
        System.out.println("Host name: " + hostName);
        System.out.println();

        Client client = new Client(portNumber, hostName, nick);
        client.execute();
    }

}

