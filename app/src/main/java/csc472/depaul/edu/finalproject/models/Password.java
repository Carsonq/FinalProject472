package csc472.depaul.edu.finalproject.models;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Password {
    private static Password password = null;
    private static String passwordEncrypt = null;

    static {
        password = new Password();
    }

    private Password() {
    }

    public static final Password getPassword() {
        return password;
    }

    public String getpasswordEncrypt(Context context) {
        if (passwordEncrypt != null) {
            return passwordEncrypt;
        } else {
            try {
                File dataDir = context.getFilesDir();
                File file = new File(dataDir.getAbsolutePath() + "/csc472_finalproject/passwords.txt");

                FileInputStream inputFile = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(inputFile);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String res = br.readLine();
                if (res.equals("")) {
                    return null;
                }

                passwordEncrypt = res;
                inputStreamReader.close();
                br.close();
                return passwordEncrypt;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public boolean checkPassword(Context context, final String passwd) {
        if (this.passwordEncrypt != null) {
            try {
                String decryptedPinStr = Cryption.getCryption().aes256Decrypt(passwd, Password.getPassword().getpasswordEncrypt(context));
                if (passwd.equals(decryptedPinStr)) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                String encryptedPassStr = Cryption.getCryption().aes256Encrypt(passwd, passwd);
                saveNewPassword(context, encryptedPassStr);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private void saveNewPassword(Context context, final String sText) throws Exception {
        try {
            File dataDir = context.getFilesDir();
            File dir = new File(dataDir.getAbsolutePath() + "/csc472_finalproject");
            dir.mkdirs();

            File file = new File(dir + "/passwords.txt");
            file.createNewFile();

            FileOutputStream outputFile = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputFile);
            outputStreamWriter.append(sText);
            outputStreamWriter.close();
            outputFile.close();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
