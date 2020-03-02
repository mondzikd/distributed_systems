package zad1;

import java.io.Console;
import java.io.PrintWriter;
import java.util.Scanner;

public class ClientWriter implements Runnable {
    private PrintWriter out;
    private Client client;


    public ClientWriter(PrintWriter out, Client client) {
        this.out = out;
        this.client = client;
    }

    @Override
    public void run() {
        // send first message with nickname
        out.println(client.getNick());

        // send user messages
        String message;
        Scanner scanner = new Scanner(System.in);
        do {
            message = scanner.nextLine();
            out.println(message);
        } while (!message.equals("exit"));
    }
}
