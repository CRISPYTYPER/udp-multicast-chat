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


    public UDPMulticastReceiver(InetAddress inputInetAddress, int inputPortNum, String inputUserName) {
        inetAddress = inputInetAddress;
        portNumber = inputPortNum;
        userName = inputUserName;
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
                String receivedMessage = new String(datagramPacket.getData()).trim();
                String[] colonDivided = receivedMessage.split(":");
                if (colonDivided.length > 1) {
                    String senderName;
                    String sendedContent;
                    senderName = colonDivided[0].strip();
                    sendedContent = colonDivided[1].strip();
                    if (sendedContent.equals("#EXIT")) {
                        System.out.println("-- " + senderName + " 님이 채팅을 종료하셨습니다. --");
                        if (userName.equals(senderName)) {
                            System.out.println("리시버 프로세스 종료");
                            break;
                        }
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
            if (!multicastSocket.isClosed())
                multicastSocket.close();
        }
    }

}
