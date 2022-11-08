import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPMulticastReceiver extends Thread {
    private InetAddress inetAddress = null;
    private int portNumber = 0;
    private MulticastSocket multicastSocket = null;
    private DatagramPacket datagramPacket = null;
    private String userName = null;

    private boolean exitThread;


    public UDPMulticastReceiver(InetAddress inputInetAddress, int inputPortNum, String inputUserName) {
        inetAddress = inputInetAddress;
        portNumber = inputPortNum;
        userName = inputUserName;
        exitThread = false;
    }

    public void stopThread() {
        exitThread = true;
    }

    public void run() {
        try {
            multicastSocket = new MulticastSocket(portNumber);
            multicastSocket.joinGroup(inetAddress);
            while (true) {
                if (exitThread) break;
                //        채팅 메시지를 chunk 단위(512 byte)로 나누어서 수신
                byte[] chunk = new byte[512];
                datagramPacket = new DatagramPacket(chunk, chunk.length);
                multicastSocket.receive(datagramPacket);
                String receivedMessage = new String(datagramPacket.getData()).trim();
                String[] colonDivided = receivedMessage.split(":");
                if (colonDivided.length > 1) {
                    String senderName;
                    String sendedContent;
                    senderName = colonDivided[0].strip();
                    sendedContent = colonDivided[1].strip();
                    if (sendedContent.equals("#EXIT")) {
                        System.out.println("-- " + senderName + " 님이 채팅을 종료하셨습니다. --");
                    } else {
                        System.out.println(receivedMessage);
                    }
                } else {
                    System.out.println(receivedMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            if (!multicastSocket.isClosed()) {
                multicastSocket.close();
            }
        }
    }

}
