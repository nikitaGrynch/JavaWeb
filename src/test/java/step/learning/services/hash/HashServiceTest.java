package step.learning.services.hash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashServiceTest {

    @Test
    void hash() {
        HashService hashService = new Md5HashService();
        String data1 = "HashService hashService = new Md5HashService();";
        String data2 = "HashService hashService";
        String data3 = "";
        String hash1 = hashService.hash(data1);
        String hash2 = hashService.hash(data2);
        String hash3 = hashService.hash(data3);
        assertTrue(hash1.length() == hash2.length() && hash1.length() == hash3.length(), "Hash length testing");

        assertTrue(!hash1.equals(hash2) && !hash1.equals(hash3), "Hash must be different for different data");

        assertEquals(hash1, hashService.hash(data1), "Repeat hash for the same data");
    }
}