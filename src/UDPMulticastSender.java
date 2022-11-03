import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDPMulticastSender extends Thread {
    private BufferedReader bufferedReader = null;
    private InetAddress inetAddress = null;
    private int portNumber = 0;
    private String userName = "";
    private DatagramSocket datagramSocket = null;
    private DatagramPacket datagramPacket = null;

    public UDPMulticastSender(InetAddress inputInetAddress, int inputPortNum, String inputUserName) {
        inetAddress = inputInetAddress;
        portNumber = inputPortNum;
        userName = inputUserName;
    }

    public void run() {
        byte[] chunk = new byte[512];
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            datagramSocket = new DatagramSocket();
            while (true) {
                String messageToSend = bufferedReader.readLine();
                String packedMessage = userName + ": " + messageToSend;
                // TODO 메세지 512바이트 이상일때 처리
                chunk = packedMessage.getBytes();
                datagramPacket = new DatagramPacket(chunk, chunk.length, inetAddress, portNumber);
                datagramSocket.send(datagramPacket);
            }

        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            datagramSocket.close();
        }
    }
}
