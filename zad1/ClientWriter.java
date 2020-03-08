package zad1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientWriter implements Runnable {
    private int multicastPortNumber;
    private InetAddress multicastGroup;

    private int serverPortNumber;
    private InetAddress serverAddress;

    private PrintWriter tcpOut;
    private DatagramSocket udpSocket;

    private String asciiArtFilePathU;
    private String asciiArtFilePathM;

    public ClientWriter(PrintWriter out, DatagramSocket udpSocket, InetAddress serverAddress, int serverPortNumber, InetAddress multicastGroup, int multicastPortNumber) {
        this.tcpOut = out;
        this.udpSocket = udpSocket;
        this.multicastGroup = multicastGroup;
        this.multicastPortNumber = multicastPortNumber;
        this.serverAddress = serverAddress;
        this.serverPortNumber = serverPortNumber;

        this.asciiArtFilePathU = "src/zad1/panBulwa.txt";
        this.asciiArtFilePathM = "src/zad1/Rex.txt";
    }

    @Override
    public void run() {
        String message;
        Scanner scanner = new Scanner(System.in);
        do {
            message = scanner.nextLine();
            if (message.equals("U")) {
                byte[] sendBuffer = readBytesFromFile(asciiArtFilePathU);
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPortNumber);

                try {
                    udpSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else if (message.equals("M")) {
                byte[] sendBuffer = readBytesFromFile(asciiArtFilePathM);
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, multicastGroup, multicastPortNumber);

                try {
                    udpSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else if (message.equals("")) {
                continue;
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

    public void setAsciiArtFilePathU(String asciiArtFilePathU) {
        this.asciiArtFilePathU = asciiArtFilePathU;
    }

    public String getAsciiArtFilePathU() {
        return asciiArtFilePathU;
    }

    public String getAsciiArtFilePathM() {
        return asciiArtFilePathM;
    }

    public void setAsciiArtFilePathM(String asciiArtFilePathM) {
        this.asciiArtFilePathM = asciiArtFilePathM;
    }
}
