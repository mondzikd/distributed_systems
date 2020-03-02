package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerClientThread implements Runnable{
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private String nick;

    public  ServerClientThread(Server server, PrintWriter out, BufferedReader in) {
        this.server = server;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        // receive first message with client nickname
        try {
            this.nick = in.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // send list of active clients
        sendMessage("[server]: " + server.activeClientsNames());

        // add yourself to server's clients list
        server.addClient(this, nick);

        // tell everyone that you joined
        server.broadcast(this, "[server]: " + nick + " joined");

        // read msg, send to others
        String msg = "exit";
        do {
            try {
                msg = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.broadcast(this, nick + ": " + msg);
        } while (!msg.equals("exit"));
        sendMessage("exit");
        server.removeClient(this, nick);
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
