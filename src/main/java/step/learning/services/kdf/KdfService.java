package step.learning.services.kdf;

/**
 * Key Derivation Function service (by RFC 2898)
 */
public interface KdfService {
    String getDerivedKey(String password, String salt);
}
