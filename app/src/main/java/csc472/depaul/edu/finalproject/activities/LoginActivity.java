package csc472.depaul.edu.finalproject.activities;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.models.FingerprintHandler;
import csc472.depaul.edu.finalproject.models.Password;
import csc472.depaul.edu.finalproject.utils.SharedPreferencesHelper;

public class LoginActivity extends AppCompatActivity {

    private String passwdEncrypt;
    private static final String KEY_NAME = "AppKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwdEncrypt = Password.getPassword().getpasswordEncrypt(getLoginActivity());
        final TextView loginLable = findViewById(R.id.login_label);

        final CardView login = findViewById(R.id.login_button);

        if (login != null) {
            login.setOnClickListener(onClickLogin);
        }

        if (passwdEncrypt == null) {
            loginLable.setText("Create");
        } else if (SharedPreferencesHelper.ReadBoolean(getLoginActivity(), getResources().getString(R.string.finger_sp_filename), "enable")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                if (!fingerprintManager.isHardwareDetected()) {
                    Toast toast = Toast.makeText(getLoginActivity(), "Your device doesn't support fingerprint authentication", Toast.LENGTH_LONG);
                    toast.show();
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    Toast toast = Toast.makeText(getLoginActivity(), "No fingerprint configured. Please register at least one fingerprint in your device's Settings", Toast.LENGTH_LONG);
                    toast.show();
                } else if (!keyguardManager.isKeyguardSecure()) {
                    Toast toast = Toast.makeText(getLoginActivity(), "Please enable lockscreen security in your device's Settings", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    requestFingerPrintPermission();
                    try {
                        generateKey();
                    } catch (FingerprintException e) {
                        e.printStackTrace();
                    }

                    if (initCipher()) {
                        cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler helper = new FingerprintHandler(this);
                        helper.startAuth(fingerprintManager, cryptoObject);

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(getResources().getString(R.string.fingerprint_title));
                        builder.setMessage(getResources().getString(R.string.fingerprint_hint));
                        builder.setNegativeButton("Use Password", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                login.setOnClickListener(onClickLogin);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alertDialog.dismiss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private LoginActivity getLoginActivity() {
        return this;
    }

    public void setupFingerPrint() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.dialog_use_fing);
        alertDialogBuilder.setTitle(R.string.dialog_title);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                        if (!fingerprintManager.isHardwareDetected()) {
                            Toast toast = Toast.makeText(getLoginActivity(), "Your device doesn't support fingerprint authentication", Toast.LENGTH_LONG);
                            toast.show();
                        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                            Toast toast = Toast.makeText(getLoginActivity(), "No fingerprint configured. Please register at least one fingerprint in your device's Settings", Toast.LENGTH_LONG);
                            toast.show();
                        } else if (!keyguardManager.isKeyguardSecure()) {
                            Toast toast = Toast.makeText(getLoginActivity(), "Please enable lockscreen security in your device's Settings", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            requestFingerPrintPermission();
                            SharedPreferencesHelper.WriteBoolean(getLoginActivity(), getResources().getString(R.string.finger_sp_filename), "enable", true);
                        }
                        Intent intent = new Intent(getLoginActivity(), MainActivity.class);
                        getLoginActivity().startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferencesHelper.WriteBoolean(getLoginActivity(), getResources().getString(R.string.finger_sp_filename), "enable", false);
                        Intent intent = new Intent(getLoginActivity(), MainActivity.class);
                        getLoginActivity().startActivity(intent);
                    }
                });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private View.OnClickListener onClickLogin = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final EditText passwd = findViewById(R.id.password);
            String passwdText = passwd.getText().toString();

            if (!Password.getPassword().checkPassword(getLoginActivity(), passwdText)) {
                Toast toast = Toast.makeText(getLoginActivity(), getResources().getString(R.string.wrong_password), Toast.LENGTH_LONG);
                toast.show();
            } else {
                if (passwdEncrypt == null) {
                    setupFingerPrint();
                } else {
                    Intent intent = new Intent(getLoginActivity(), MainActivity.class);
                    getLoginActivity().startActivity(intent);
                }
            }
        }
    };

    private void generateKey() throws FingerprintException {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }

    private void requestFingerPrintPermission() {
        int fingerprintPermission = ActivityCompat.checkSelfPermission(getLoginActivity(), Manifest.permission.USE_FINGERPRINT);
        if (fingerprintPermission != PackageManager.PERMISSION_GRANTED) {
            int REQUEST_FINGER = 1;

            String[] PERMISSIONS_FINGER = {
                    Manifest.permission.USE_FINGERPRINT
            };

            ActivityCompat.requestPermissions(
                    getLoginActivity(),
                    PERMISSIONS_FINGER,
                    REQUEST_FINGER
            );
        }
    }
}
