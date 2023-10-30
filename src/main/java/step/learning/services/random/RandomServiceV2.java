package step.learning.services.random;

import java.util.Random;

public class RandomServiceV2 implements RandomService {
    private final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private final Random random;

    public RandomServiceV2() {
        this.random = new Random();
    }

    @Override
    public void seed(String iv) {
        random.setSeed(iv.length());
    }

    @Override
    public String randomHex(int charLength) {
        char[] chars = new char[charLength];
        for(int i = 0; i < charLength; i++){
            int index = random.nextInt(HEX_CHARS.length);
            chars[i] = HEX_CHARS[index];
        }
        return new String(chars);
    }
}
