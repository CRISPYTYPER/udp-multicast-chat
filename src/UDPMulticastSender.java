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
        try {
            datagramSocket = new DatagramSocket();
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                byte[] chunk = new byte[512];
                String messageToSend = null;
                String tempInput = bufferedReader.readLine();
                if (tempInput.charAt(0) == '#') {
                    if (tempInput.equals("#EXIT")) {
                        System.out.println("이 채팅방을 나갑니다...");
                        messageToSend = tempInput;
                        String packedMessage = userName + ": " + messageToSend;
                        // TODO 메세지 512바이트 이상일때 처리
                        System.arraycopy(packedMessage.getBytes(), 0, chunk, 0, packedMessage.getBytes().length);
                        datagramPacket = new DatagramPacket(chunk, chunk.length, inetAddress, portNumber);
                        datagramSocket.send(datagramPacket);
                        break; // while문 나가기
                    } else {
                        System.out.println("잘못된 명령어가 입력되었습니다.");
                        System.out.println("다시 입력해주세요.");
                    }
                } else {
                    messageToSend = tempInput;
                    String packedMessage = userName + ": " + messageToSend;
                    if (packedMessage.getBytes().length <= 512) { // byte가 512바이트 이하이면
                        System.arraycopy(packedMessage.getBytes(), 0, chunk, 0, packedMessage.getBytes().length);
                        datagramPacket = new DatagramPacket(chunk, chunk.length, inetAddress, portNumber);
                        datagramSocket.send(datagramPacket);
                    } else { // 512바이트 보다 큰 배열이면
                        byte[] bigByteArr = new byte[packedMessage.getBytes().length]; // 512바이트보다 큰 바이트 배열을 선언한다.
                        System.arraycopy(packedMessage.getBytes(), 0, bigByteArr, 0, packedMessage.getBytes().length);
                        int offset = 0;
                        datagramPacket = new DatagramPacket(bigByteArr, offset, 512, inetAddress, portNumber);
                        int bytesSent = 0;
                        while (bytesSent < bigByteArr.length) {
                            datagramSocket.send(datagramPacket);
                            bytesSent += datagramPacket.getLength();
                            int bytesToSend = bigByteArr.length - bytesSent;
                            int size = (bytesToSend > 512) ? 512 : bytesToSend;
                            datagramPacket.setData(bigByteArr, bytesSent, size);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            if (!datagramSocket.isClosed())
                datagramSocket.close();
        }
    }
}
