import java.util.Scanner;

public class Peer {
    public static void main(String[] args) {
        int portNum = 0;
        String inputStr;
        Scanner scan = new Scanner(System.in);

        if(args.length != 1) {
            System.out.println("Port 번호를 입력해주세요.");
            System.exit(0);
        } else {
            portNum = Integer.parseInt(args[0]);
        }

        while(true) {
            inputStr = scan.nextLine();
            char firstCharacter = inputStr.charAt(0);
            System.out.println(firstCharacter);
        }
    }
}