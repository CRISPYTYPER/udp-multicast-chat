import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

public class Peer {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        int portNum = 0;
        boolean isCommand = false;
        String commandPhrase = "";
        UDPMulticastReceiver udpMulticastReceiver = null;
        UDPMulticastSender udpMulticastSender = null;

        if (args.length != 1) {
            System.out.println("Port 번호를 올바르게 입력해주세요.");
            System.exit(0);
        } else {
            portNum = Integer.parseInt(args[0]);
        }

        String inputString;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("채팅방 입장 방법: #JOIN (참여할 채팅방의 이름) (사용자 이름)");
        while ((inputString = reader.readLine()) != null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
//            아무 입력 안받으면 다시 입력 받기
            if (inputString.length() == 0) {
                continue;
            }
            String[] splitedInput = inputString.split(" ");

//            '#'확인되면 isCommand flag true로 바꾸기
            isCommand = (splitedInput[0].charAt(0) == '#') ? true : false;

            if (isCommand == true) {
//                commandPhrase는 #지운 명령어 자체(e.g. #JOIN -> JOIN)
                commandPhrase = splitedInput[0].substring(1);

                switch (commandPhrase) {
                    case "JOIN":

                        // 인풋 다음과 같이 받음. #JOIN (참여할 채팅방의 이름) (사용자 이름)
                        if (splitedInput.length != 3) {
                            System.out.println("ERROR! 다음과 같은 형식으로 입력해주세요.");
                            System.out.println("#JOIN (참여할 채팅방의 이름) (사용자 이름)");
                            continue;
                        }
                        String roomName = splitedInput[1];
                        String userName = splitedInput[2];
                        SHA256 sha256 = new SHA256();
                        // String형의 multicastAddress를 InetAddress형으로 바꿈.
                        InetAddress multicastAddress = InetAddress.getByName(sha256.getMulticastAddress(roomName));

                        try {
                            udpMulticastReceiver = new UDPMulticastReceiver(multicastAddress, portNum, userName);
                            udpMulticastReceiver.start();
                            udpMulticastSender = new UDPMulticastSender(multicastAddress, portNum, userName, roomName);
                            udpMulticastSender.start();
                            udpMulticastSender.join();
                            udpMulticastReceiver.stopThread();
                            udpMulticastReceiver.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            } else {
                System.out.println("ERROR! 올바른 명령어를 입력해주세요.");
            }
        }
    }
}
