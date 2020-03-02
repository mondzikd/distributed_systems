package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int portNumber;
    private String hostName;
    private String nick;

    private Socket tcpSocket;

    public Client(int portNumber, String hostName, String nick) {
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void execute() throws IOException {

        try {
            // create tcp socket
            tcpSocket = new Socket(hostName, portNumber);

            // status message
            System.out.println("Connected to the chat server");

            // in & out tcp streams
            PrintWriter out = new PrintWriter(tcpSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

            // Reader and Writer threads
            Thread readerThread = new Thread(new ClientReader(in, this));
            Thread writerThread = new Thread(new ClientWriter(out, this));
            readerThread.start();
            writerThread.start();
            readerThread.join();
            writerThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tcpSocket != null){
                tcpSocket.close();
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
        System.out.println("Port number: " + portNumber);
        System.out.println("Host name: " + hostName);
        System.out.println();

        Client client = new Client(portNumber, hostName, nick);
        client.execute();
    }

}

