package ca.javau11.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GravatarUtils {

	private static final String DEFAULT_GRAVATAR_URL = "https://www.gravatar.com/avatar/00000000000000000000000000000000?s=80&d=mp";
	
    public static String getGravatarUrl(String email, int size) {
        String cleanEmail = email.trim().toLowerCase();
        String emailHash = md5Hex(cleanEmail);
        String gravatarUrl = "https://www.gravatar.com/avatar/" + emailHash + "?s=" + size;
        return gravatarUrl + "&d=" + DEFAULT_GRAVATAR_URL;
    }

    private static String md5Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
