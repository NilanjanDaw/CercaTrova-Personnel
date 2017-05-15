package debapriya.thunderstruck.com.cercatrovapersonnel.support;

import android.util.Base64;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by nilanjan on 15-May-17.
 * Project client_personnel
 */

public class Encryption {

    private final static char[] hexArray = "0123456789abcdef".toCharArray();
    private static final String TAG = Encryption.class.getName();
    private static Cipher cipher = null;
    private static SecretKey secretKey;
    public Encryption() throws NoSuchPaddingException, NoSuchAlgorithmException {
        byte[] decodedKey = Base64.decode(Constants.PASS_KEY, Base64.DEFAULT);
        // rebuild key using SecretKeySpec
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        cipher = Cipher.getInstance("AES");
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public String encryptPassword(String arg) throws Exception {

        // uncomment the following line to add the Provider of choice
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());


        String encodedKey = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
        byte[] encryptMessage =  encryptMsg(arg, secretKey);
        Log.d(TAG, "getCipherText: " + bytesToHex(encryptMessage));
        String decryptedMessage = decryptMsg(encryptMessage, secretKey);
        Log.d(TAG, "getCipherText: " + decryptedMessage);
        return bytesToHex(encryptMessage);
    }

    private byte[] encryptMsg(String message, SecretKey secret)
            throws Exception
    {
	   /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    private String decryptMsg(byte[] cipherText, SecretKey secret)
            throws Exception
    {
	    /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }
}
