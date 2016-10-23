package ru.iammaxim.vkconsole;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by maxim on 23.10.2016.
 */
public class UserUploader {
    private static final String IP = "188.120.243.80";
//    private static final String IP = "localhost";
    private static final int port = 45673;

    public static byte[] encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(value.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void upload(String s) {
        try {
            Socket socket = new Socket(IP, port);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            byte[] data = encrypt("supersecretkeyVK", "supersecretpassC", s);
            dos.writeInt(data.length);
            dos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
