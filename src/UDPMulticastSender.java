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
        try {
            datagramSocket = new DatagramSocket();
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String messageToSend = null;
                String tempInput = bufferedReader.readLine();
                if(tempInput.charAt(0) == '#') {
                    if (tempInput.equals("#EXIT")) {
                        System.out.println("프로그램을 종료합니다...");
                        messageToSend = tempInput;
                        String packedMessage = userName + ": " + messageToSend;
                        // TODO 메세지 512바이트 이상일때 처리
                        System.arraycopy(packedMessage.getBytes(), 0, chunk, 0, packedMessage.getBytes().length);
                        ;
                        datagramPacket = new DatagramPacket(chunk, chunk.length, inetAddress, portNumber);
                        datagramSocket.send(datagramPacket);
                        break;
                    } else {
                        System.out.println("잘못된 명령어가 입력되었습니다.");
                        System.out.println("다시 입력해주세요.");
                    }
                }else {
                    messageToSend = tempInput;
                    String packedMessage = userName + ": " + messageToSend;
                    // TODO 메세지 512바이트 이상일때 처리
                    System.arraycopy(packedMessage.getBytes(), 0, chunk, 0, packedMessage.getBytes().length);
                    ;
                    datagramPacket = new DatagramPacket(chunk, chunk.length, inetAddress, portNumber);
                    datagramSocket.send(datagramPacket);
                }

            }

        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            if(!datagramSocket.isClosed())
                datagramSocket.close();
        }
    }
}
