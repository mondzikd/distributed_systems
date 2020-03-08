package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerClientThread implements Runnable{
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private String nick;
    private int clientPortNumber;

    public  ServerClientThread(Server server, PrintWriter out, BufferedReader in) {
        this.server = server;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        // receive first message with client nickname and portNumber
        try {
            String[] verifyMessage = in.readLine().split(" ");
            this.nick = verifyMessage[0];
            this.clientPortNumber = Integer.parseInt(verifyMessage[1]);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            // send list of active clients
            sendMessage("[server]: " + server.activeClientsNames());

            // add yourself to server's clients list
            server.addClient(this, nick, clientPortNumber);

            // tell everyone that you joined
            server.broadcast(this, "[server]: " + nick + " joined");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // read msg, send to others
        String msg = "exit";
        do {
            try {
                msg = in.readLine();
                server.broadcast(this, nick + ": " + msg);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        } while (msg != null && !msg.equals("exit"));

        try {
            server.removeClient(this, nick, clientPortNumber);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}