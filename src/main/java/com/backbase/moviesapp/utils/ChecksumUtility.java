package com.backbase.moviesapp.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@UtilityClass
public class ChecksumUtility {

    public String computeChecksum(InputStream inputStream) {
        try (Reader r = new InputStreamReader(inputStream)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            int c;
            while ((c = r.read()) != -1) digest.update((byte)c);
            return Base64.getEncoder().encodeToString(digest.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
