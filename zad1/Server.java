package zad1;

// TODO: Client should send udp Socket address instead of portNumber

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Server {
    private int portNumber;

    private ServerSocket serverSocket;
    private DatagramSocket udpSocket;

    private ServerUdpThread serverUdp;

    private List<String> nicks = new ArrayList<>();
    private Set<ServerClientThread> serverClientThreads = new HashSet<>();
    private Map<Integer, String> udpClientsNicknames = new HashMap<>();

    private Semaphore clientInfoAccess;

    public Server(int portNumber) {
        this.portNumber = portNumber;
        clientInfoAccess = new Semaphore(1);
    }

    public void execute() throws IOException {

        System.out.println("Java TCP-UDP chat server listening on port " + portNumber);

        try {
            // create tcp server socket
            serverSocket = new ServerSocket(portNumber);

            // TODO: change names of classes
            // create udp socket and handling thread
            udpSocket = new DatagramSocket(portNumber);
            serverUdp = new ServerUdpThread(this, udpSocket, 8192);
            Thread serverUdpThread = new Thread(serverUdp);
            serverUdpThread.start();

            while (true) {

                // accept client
                Socket tcpSocket = serverSocket.accept();

                // in & out streams
                PrintWriter out = new PrintWriter(tcpSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

                // create thread for client
                Thread serverClientThread = new Thread(new ServerClientThread(this, out, in));
                serverClientThread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    public void addClient(ServerClientThread serverClientThread, String nick, Integer udpClientPortNumber) throws InterruptedException {
        clientInfoAccess.acquire();

        serverClientThreads.add(serverClientThread);
        nicks.add(nick);
        udpClientsNicknames.put(udpClientPortNumber, nick);
        System.out.println(nick + " connected");

        clientInfoAccess.release();
    }

    public void removeClient(ServerClientThread serverClientThread, String nick, Integer udpClientPortNumber) throws IOException, InterruptedException {
        // Tell client's tcp and udp about exit
        // InetAddress in stupid way. Should be replaced by SocketAddress
        serverUdp.sendMessage(udpClientPortNumber, InetAddress.getByName("localhost"), "exit");
        serverClientThread.sendMessage("exit");

        clientInfoAccess.acquire();

        // Remove information about client
        serverClientThreads.remove(serverClientThread);
        nicks.remove(nick);
        udpClientsNicknames.remove(udpClientPortNumber);
        System.out.println(nick + " removed");

        clientInfoAccess.release();
    }

    public void broadcast(ServerClientThread serverClientThread, String message) throws InterruptedException {
        clientInfoAccess.acquire();

        for (ServerClientThread thread : serverClientThreads) {
            if (thread != serverClientThread) {
                thread.sendMessage(message);
            }
        }

        clientInfoAccess.release();
    }

    public void udpBroadcast(int senderPortNumber, InetAddress senderAddress, String message) throws IOException, InterruptedException {
        clientInfoAccess.acquire();

        for (int port : udpClientsNicknames.keySet()) {
            if (port != senderPortNumber) {
                // Stupid always sending to sender address
                // TODO: search client address (client should send SocketAddress not only portNumber)
                serverUdp.sendMessage(port, senderAddress, message);
            }
        }

        clientInfoAccess.release();
    }

    public String activeClientsNames() throws InterruptedException {
        StringBuffer stringBuffer = new StringBuffer();

        clientInfoAccess.acquire();

        if (serverClientThreads.isEmpty()) {
            stringBuffer.append("No active users");
        }
        else {
            stringBuffer.append("Active users: ");
            for (String s : nicks) {
                stringBuffer.append(s);
                stringBuffer.append(", ");
            }
        }

        clientInfoAccess.release();

        return stringBuffer.toString();
    }

    public String getNickFromUdpClient(int senderPortNumber) {
        return udpClientsNicknames.get(senderPortNumber);
    }

    public static void main(String[] args) throws IOException {
        int portNumber = 12345;

        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
        }

        Server server = new Server(portNumber);
        server.execute();
    }
}