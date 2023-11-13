package step.learning.services.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomServiceV1Test {

    @Test
    void seed() {
        RandomService randomService1 = new RandomServiceV1();
        RandomService randomService2 = new RandomServiceV1();
        String seed = "123";
        randomService1.seed(seed);
        randomService2.seed(seed);
        assertEquals(randomService1.randomHex(10),
                randomService2.randomHex(10),
                "Same data from the same seed");

        randomService1.seed(seed + "1");
        randomService2.seed(seed);
        assertNotEquals(randomService1.randomHex(10),
                randomService2.randomHex(10),
                "Different data from the different seed");
    }

    @Test
    void randomHex() {
        RandomService randomService1 = new RandomServiceV1();

        for(int i = 0; i < 20; i++) {
            String hex = randomService1.randomHex(i);
            assertEquals(i, hex.length(), "randomHex Length testing");
            assertTrue(hex.matches("^[0-9A-Fa-f]*$"),
                    String.format("Pattern matching for '%s'", hex));
        }
    }
}