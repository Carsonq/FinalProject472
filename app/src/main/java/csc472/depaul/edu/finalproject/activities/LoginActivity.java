package csc472.depaul.edu.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.models.Password;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String passwdEncrypt = Password.getPassword().getpasswordEncrypt(getLoginActivity());
        final TextView loginLable = findViewById(R.id.login_label);

        if (passwdEncrypt == null) {
            loginLable.setText("Create");
        }

        final CardView login = findViewById(R.id.login_button);

        if (login != null) {
            login.setOnClickListener(onClickLogin);
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

    private View.OnClickListener onClickLogin = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final EditText passwd = findViewById(R.id.password);
            String passwdText = passwd.getText().toString();

            if (!Password.getPassword().checkPassword(getLoginActivity(), passwdText)) {
                Toast toast = Toast.makeText(getLoginActivity(), getResources().getString(R.string.wrong_password), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Intent intent = new Intent(getLoginActivity(), MainActivity.class);
                getLoginActivity().startActivity(intent);
            }
        }
    };
}
