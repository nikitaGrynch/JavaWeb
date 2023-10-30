package step.learning.services.hash;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Singleton
public class Md5HashService implements HashService {
    private final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    @Override
    public String hash(String input) {
        if (input == null) {
            input = "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            char[] chars = new char[32];
            int i = 0;
            for( int b:
            digest.digest(input.getBytes(StandardCharsets.UTF_8))){
                chars[i] = HEX_CHARS[(b & 0xF0) >> 4];
                chars[i + 1] = HEX_CHARS[b & 0x0F];
                i += 2;
            }
            return new String(chars);

        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
