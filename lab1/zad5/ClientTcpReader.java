package zad1;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientTcpReader implements Runnable {
    private BufferedReader in;

    public ClientTcpReader(BufferedReader in) {
        this.in = in;
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
