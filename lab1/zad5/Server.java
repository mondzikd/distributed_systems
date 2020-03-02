package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Server {
    private int portNumber;
    private ServerSocket serverSocket;
    private List<String> nicks = new ArrayList<>();
    private Set<ServerClientThread> serverClientThreads = new HashSet<>();

    public Server(int portNumber) {
        this.portNumber = portNumber;
    }

    public void execute() throws IOException {

        System.out.println("Java TCP-UDP chat server listening on port " + portNumber);

        try {
            // create socket
            serverSocket = new ServerSocket(portNumber);

            while (true) {

                // accept client
                Socket clientSocket = serverSocket.accept();

                // in & out streams
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

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

    public void addClient(ServerClientThread serverClientThread, String nick) {
        serverClientThreads.add(serverClientThread);
        nicks.add(nick);
        System.out.println(nick + " connected");
    }

    public void removeClient(ServerClientThread serverClientThread, String nick) {
        serverClientThreads.remove(serverClientThread);
        nicks.remove(nick);
        System.out.println(nick + " removed");
    }

    public void broadcast(ServerClientThread serverClientThread, String message){
        for (ServerClientThread thread : serverClientThreads) {
            if (thread != serverClientThread) {
                thread.sendMessage(message);
            }
        }
    }

    public String activeClientsNames() {
        StringBuffer stringBuffer = new StringBuffer();

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

        return stringBuffer.toString();
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
