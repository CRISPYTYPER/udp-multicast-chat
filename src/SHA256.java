import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;

public class SHA256 {

    public String getMulticastAddress(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        return bytesToMulticastAddress(md.digest());
    }

    private String bytesToMulticastAddress(byte[] bytes) {
        //225.x.y.z 로 변환 후 리턴
        StringBuilder stringBuilder = new StringBuilder();
        int lengthOfBytes = bytes.length;
        stringBuilder.append("225.");
        for (int i = 0; i < 3; i++) {
            stringBuilder.append(Byte.toUnsignedInt(bytes[lengthOfBytes-3+i]));
            stringBuilder.append(".");
        }
        String multicastAddress = stringBuilder.substring(0,stringBuilder.length()-1);
        return multicastAddress;
    }

    // 아래는 테스트용 (사용 안함)
    private String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

}