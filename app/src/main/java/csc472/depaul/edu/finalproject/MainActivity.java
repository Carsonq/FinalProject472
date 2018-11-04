package csc472.depaul.edu.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private final static float MIN_RATE = 0.001f;
    private static String NAME_HOLDER = "NAME_HOLDER";
    private static String INITIAL_HOLDER = "INITIAL_HOLDER";
    private static String RATE_HOLDER = "RATE_HOLDER";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Button invest = findViewById(R.id.invest);
        if (invest != null)
        {
            invest.setOnClickListener(onClickInvest);
        }

        
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            String sName = getEditViewText(R.id.name);
            if (!sName.equals("")) {
                savedInstanceState.putString(NAME_HOLDER, sName);
            }

            String sInitial = getEditViewText(R.id.investment);
            if (!sInitial.equals("")) {
                savedInstanceState.putString(INITIAL_HOLDER, sInitial);
            }

            String sRate = getEditViewText(R.id.rate);
            if (!sRate.equals("")) {
                savedInstanceState.putString(RATE_HOLDER, sRate);
            }
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            EditText name = findViewById(R.id.name);
            if (name != null && !name.getText().toString().equals(""))
            {
                name.setText(savedInstanceState.getString(NAME_HOLDER));
            }

            EditText investment = findViewById(R.id.investment);
            if (investment != null & !investment.getText().toString().equals(""))
            {
                investment.setText(savedInstanceState.getString(INITIAL_HOLDER));
            }

            EditText rate = findViewById(R.id.rate);
            if (rate != null && !rate.getText().toString().equals(""))
            {
                rate.setText(savedInstanceState.getString(RATE_HOLDER));
            }
        }
    }

    private final MainActivity getMainActivity()
    {
        return this;
    }

    private View.OnClickListener onClickInvest = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
                        Intent intent = new Intent(getMainActivity(), NewAuthActivity.class);
//                        intent.putExtra("investment_data", investment);
                        getMainActivity().startActivity(intent);
        }
    };

    //helper functions below
    private boolean validateEditTextField(int id)
    {
        boolean isValid = false;

        final EditText editText = findViewById(id);
        if (editText != null)
        {
            String sText = editText.getText().toString();

            isValid = ((sText != null) && (!sText.isEmpty()));
        }

        if (!isValid)
        {
            String sToastMessage = getResources().getString(R.string.fields_cannot_be_empty);
            Toast toast = Toast.makeText(getApplicationContext(), sToastMessage, Toast.LENGTH_LONG);
            toast.show();
        }

        return isValid;
    }

    private final String getEditViewText(int id)
    {
        String sText = "";

        final EditText editText = findViewById(id);
        if (editText != null)
        {
            sText = editText.getText().toString();
        }

        return sText;
    }
}
