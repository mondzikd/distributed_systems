package zad1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class ClientWriter implements Runnable {
    private PrintWriter tcpOut;
    private DatagramSocket udpSocket;
    private String asciiArtFilePath;

    public ClientWriter(PrintWriter out, DatagramSocket udpSocket) {
        this.tcpOut = out;
        this.udpSocket = udpSocket;
        asciiArtFilePath = "src/zad1/panBulwa.txt";
    }

    @Override
    public void run() {
        String message;
        Scanner scanner = new Scanner(System.in);
        do {
            message = scanner.nextLine();
            if (message.equals("U")) {
                byte[] sendBuffer = readBytesFromFile(asciiArtFilePath);
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, udpSocket.getRemoteSocketAddress());

                try {
                    udpSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else {
                tcpOut.println(message);
            }
        } while (!message.equals("exit"));
    }

    private byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;
    }

    public void setAsciiArtFilePath(String asciiArtFilePath) {
        this.asciiArtFilePath = asciiArtFilePath;
    }

    public String getAsciiArtFilePath() {
        return asciiArtFilePath;
    }
}
