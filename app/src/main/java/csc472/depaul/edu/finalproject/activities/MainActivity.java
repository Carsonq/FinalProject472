package csc472.depaul.edu.finalproject.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import csc472.depaul.edu.finalproject.models.DateRange;
import csc472.depaul.edu.finalproject.R;

public class MainActivity extends AppCompatActivity {
    private final static float MIN_RATE = 0.001f;
    private static String NAME_HOLDER = "NAME_HOLDER";
    private static String INITIAL_HOLDER = "INITIAL_HOLDER";
    private static String RATE_HOLDER = "RATE_HOLDER";

    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        requestInternetPermission();

        final CardView account_management = findViewById(R.id.manage_account);
        if (account_management != null) {
            account_management.setOnClickListener(onClickManageAccount);
        }

//        String today_date = dateFormat.format(myCalendar.getTime());

//        final EditText start_date = findViewById(R.id.start_date);
//        start_date.setHint(today_date);
//        if (start_date != null) {
//            start_date.setOnClickListener(onClickCalendar);
//        }
//
//        final EditText end_date = findViewById(R.id.end_date);
//        end_date.setHint(today_date);
//        if (end_date != null) {
//            end_date.setOnClickListener(onClickCalendar);
//        }

        final CardView expense_report = findViewById(R.id.expense_report);
        if (expense_report != null) {
            expense_report.setOnClickListener(onClickExpenseReport);
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
//        if (savedInstanceState != null)
//        {
//            String sName = getEditViewText(R.id.name);
//            if (!sName.equals("")) {
//                savedInstanceState.putString(NAME_HOLDER, sName);
//            }
//
//            String sInitial = getEditViewText(R.id.investment);
//            if (!sInitial.equals("")) {
//                savedInstanceState.putString(INITIAL_HOLDER, sInitial);
//            }
//
//            String sRate = getEditViewText(R.id.rate);
//            if (!sRate.equals("")) {
//                savedInstanceState.putString(RATE_HOLDER, sRate);
//            }
//        }

        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        if (savedInstanceState != null) {
//            EditText name = findViewById(R.id.name);
//            if (name != null && !name.getText().toString().equals(""))
//            {
//                name.setText(savedInstanceState.getString(NAME_HOLDER));
//            }
//
//            EditText investment = findViewById(R.id.investment);
//            if (investment != null & !investment.getText().toString().equals(""))
//            {
//                investment.setText(savedInstanceState.getString(INITIAL_HOLDER));
//            }
//
//            EditText rate = findViewById(R.id.rate);
//            if (rate != null && !rate.getText().toString().equals(""))
//            {
//                rate.setText(savedInstanceState.getString(RATE_HOLDER));
//            }
//        }
    }

    private final MainActivity getMainActivity() {
        return this;
    }

    private View.OnClickListener onClickManageAccount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getMainActivity(), AccountManagementActivity.class);
            getMainActivity().startActivity(intent);
        }
    };

    private View.OnClickListener onClickExpenseReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date e = myCalendar.getTime();
            myCalendar.add(Calendar.MONTH, -1);
            Date s = myCalendar.getTime();
            DateRange dr = new DateRange(s, e);

            if (dr.isValid()) {
                Intent intent = new Intent(getMainActivity(), ExpenseReportActivity.class);
                intent.putExtra("date_range", dr);
                getMainActivity().startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "The start date must be prior to the end date!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    private View.OnClickListener onClickCalendar = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDate(v);
                }
            };

            new DatePickerDialog(getMainActivity(), date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private void updateDate(View d) {
        if (d != null) {
            ((EditText) d).setText(dateFormat.format(myCalendar.getTime()));
        }
    }

    //helper functions below
    private boolean validateEditTextField(int id) {
        boolean isValid = false;

        final EditText editText = findViewById(id);
        if (editText != null) {
            String sText = editText.getText().toString();

            isValid = ((sText != null) && (!sText.isEmpty()));
        }

        if (!isValid) {
            String sToastMessage = getResources().getString(R.string.fields_cannot_be_empty);
            Toast toast = Toast.makeText(getApplicationContext(), sToastMessage, Toast.LENGTH_LONG);
            toast.show();
        }

        return isValid;
    }

    private final String getEditViewText(int id) {
        String sText = "";

        final EditText editText = findViewById(id);
        if (editText != null) {
            sText = editText.getText().toString();
        }

        return sText;
    }

    private void requestInternetPermission()
    {
        int internetPermission = ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.INTERNET);

        if (internetPermission != PackageManager.PERMISSION_GRANTED)
        {
            int REQUEST_INTERNET = 1;

            String[] PERMISSIONS_INTERNET = {
                    Manifest.permission.INTERNET
            };

            ActivityCompat.requestPermissions(
                    getMainActivity(),
                    PERMISSIONS_INTERNET,
                    REQUEST_INTERNET
            );
        }
    }
}
