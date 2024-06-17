package Networks;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Helper {
    public static byte[] encodeBase64(String data) {
        return Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(byte[] encodedData) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedData);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
