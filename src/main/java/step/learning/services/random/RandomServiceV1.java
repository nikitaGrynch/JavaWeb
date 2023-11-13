package step.learning.services.random;

import java.util.Random;
public class RandomServiceV1 implements RandomService {
    private final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private final Random random ;

    public RandomServiceV1() {
        random = new Random() ;
    }

    @Override
    public void seed(String iv) {
        random.setSeed( Long.parseLong( iv ) ) ;
    }

    @Override
    public String randomHex( int charLength ) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charLength; i++) {
            int index = random.nextInt( HEX_CHARS.length ) ;
            sb.append( HEX_CHARS[index] ) ;
        }
        return sb.toString();
    }
}
