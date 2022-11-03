import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPMulticastReceiver extends Thread{
    private InetAddress inetAddress = null;
    private int portNumber = 0;
    private MulticastSocket multicastSocket = null;
    private DatagramPacket datagramPacket = null;

    public UDPMulticastReceiver(InetAddress inputInetAddress, int inputPortNum) {
        inetAddress = inputInetAddress;
        portNumber = inputPortNum;
    }

    public void run() {
//        채팅 메시지를 chunk 단위(512 byte)로 나누어서 전송
        byte[] chunk = new byte[512];
        try {
            multicastSocket = new MulticastSocket(portNumber);
            multicastSocket.joinGroup(inetAddress);
            while (true) {
                datagramPacket = new DatagramPacket(chunk, chunk.length);
                multicastSocket.receive(datagramPacket);
                String receivedMessage = new String(datagramPacket.getData());
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            multicastSocket.close();
        }
    }


}
