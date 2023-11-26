package ru.urfu.pizzaSite.RestApiPizzaApplication.security;

import com.eatthepath.otp.HmacOneTimePasswordGenerator;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;


@Component
public class  TOTPGenerator {

    final HmacOneTimePasswordGenerator htop = new HmacOneTimePasswordGenerator(); // password exist 30 seconds
    Key key;
    {
        final KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(htop.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final int macLengthInBytes;
        try {
            macLengthInBytes = Mac.getInstance(htop.getAlgorithm()).getMacLength();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(macLengthInBytes * 8);
        key = keyGenerator.generateKey();
    }

    public String generatePassword(long counter) throws InvalidKeyException {
        return htop.generateOneTimePasswordString(key,  counter);
    }

}
