package step.learning.services.kdf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.services.hash.HashService;

@Singleton
public class DigestHashKdfService implements KdfService{
    private final HashService hashService;

    @Inject
    public DigestHashKdfService(@Named("Digest-hash") HashService hashService){
        this.hashService = hashService;
    }
    @Override
    public String getDerivedKey(String password, String salt) {
        return hashService.hash(salt + password + salt);
    }
}
