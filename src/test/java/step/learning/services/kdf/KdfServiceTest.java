package step.learning.services.kdf;

import org.junit.jupiter.api.Test;
import step.learning.services.hash.HashService;
import step.learning.services.hash.Md5HashService;

import static org.junit.jupiter.api.Assertions.*;

class KdfServiceTest {
    HashService hashService = new Md5HashService();

    @Test
    void getDerivedKey() {
        KdfService kdfService = new DigestHashKdfService(hashService);
        String derivedKey = kdfService.getDerivedKey("password", "salt");

        assertNotNull(derivedKey, "Derived key cannot be null");
        assertFalse(derivedKey.isEmpty(), "Derived key cannot be empty");

        String derivedKey1 = kdfService.getDerivedKey("qwerty123", "salt");
        String derivedKey2 = kdfService.getDerivedKey("qwerty123", "salt1");

        assertNotEquals(derivedKey1, derivedKey2, "Derived keys with the same password and different salt cannot be the same");

        derivedKey1 = kdfService.getDerivedKey("qwerty1234", "salt");
        derivedKey2 = kdfService.getDerivedKey("qwerty123", "salt");

        assertNotEquals(derivedKey1, derivedKey2, "Derived keys with different password and the same salt cannot be the same");

        derivedKey1 = kdfService.getDerivedKey("qwerty123", "salt");
        derivedKey2 = kdfService.getDerivedKey("qwerty123", "salt");

        assertEquals(derivedKey1, derivedKey2, "Derived keys with the same password and the same salt should be the same");

    }
}