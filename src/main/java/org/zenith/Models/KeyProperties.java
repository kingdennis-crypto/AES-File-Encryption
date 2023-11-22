package org.zenith.Models;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class KeyProperties {
    private final SecretKey secretKey;
    private final byte[] nonce;

    public KeyProperties(byte[] secret, byte[] nonce) {
        this.secretKey = new SecretKeySpec(secret, "AES");
        this.nonce = nonce;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] getNonce() {
        return nonce;
    }
}
