package zad4;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class UdpServer {

    public static void main(String args[])
    {
        System.out.println("JAVA UDP SERVER");
        DatagramSocket socket = null;
        int portNumber = 9008;
        String javaAnswer = "Pong Java";
        String pythonAnswer = "Pong Python";

        try{
            socket = new DatagramSocket(portNumber);
            byte[] receiveBuffer = new byte[1024];

            while(true) {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                System.out.println("received msg: " + msg);

                // resend
                String answer = javaAnswer;
                if(msg.charAt(0) == 'p'){
                    answer = pythonAnswer;
                }

                byte[] anwerBuffer = answer.getBytes();
                InetAddress address = receivePacket.getAddress();
                int clientPortNumber = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(anwerBuffer, anwerBuffer.length, address, clientPortNumber);
                socket.send(sendPacket);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
