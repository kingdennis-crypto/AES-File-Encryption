package org.zenith.Utilities;

import org.zenith.Models.KeyProperties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;

public class EncryptionHandler {
    public byte[] encrypt(byte[] plain, SecretKey secret, byte[] iv) throws InvalidParameterException {
        try {
            if (plain == null || secret == null || iv == null)
                throw new InvalidParameterException();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            return cipher.doFinal(plain);
        } catch(InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String decrypt(byte[] encrypt, SecretKey secret, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            return new String(cipher.doFinal(encrypt), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
