package zad1;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReader implements Runnable {
    private BufferedReader in;
    private Client client;

    public ClientReader(BufferedReader in, Client client) {
        this.in = in;
        this.client = client;
    }

    @Override
    public void run() {
        String response = "exit";
        do {
            try {
                response = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response);
        } while (!response.equals("exit"));
    }
}
