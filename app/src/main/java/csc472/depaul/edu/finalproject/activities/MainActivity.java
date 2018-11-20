package csc472.depaul.edu.finalproject.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import csc472.depaul.edu.finalproject.models.DateRange;
import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.models.ScanThread;

public class MainActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check pending image scan
        String resUrl = getMainActivity().getString(R.string.sanner_result_url, getMainActivity().getString(R.string.sanner_apikey));
        AsyncTask.execute(new ScanThread(resUrl, getMainActivity()));

        setContentView(R.layout.activity_main);

        requestInternetPermission();

        final CardView account_management = findViewById(R.id.manage_account);
        if (account_management != null) {
            account_management.setOnClickListener(onClickManageAccount);
        }

        final CardView expense_report = findViewById(R.id.expense_report);
        if (expense_report != null) {
            expense_report.setOnClickListener(onClickExpenseReport);
        }

        final CardView camera = findViewById(R.id.take_photo);
        if (camera != null) {
            camera.setOnClickListener(onClickTakePhoto);
        }

        final CardView receipt_report = findViewById(R.id.receipt_report);
        if (receipt_report != null) {
            receipt_report.setOnClickListener(onClickRecieptReport);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myCalendar = Calendar.getInstance();
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
            myCalendar.add(Calendar.DAY_OF_MONTH, -15);
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

    private View.OnClickListener onClickRecieptReport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date e = myCalendar.getTime();
            myCalendar.add(Calendar.DAY_OF_MONTH, -15);
            Date s = myCalendar.getTime();
            DateRange dr = new DateRange(s, e);

            if (dr.isValid()) {
                Intent intent = new Intent(getMainActivity(), ReceiptReportActivity.class);
                intent.putExtra("date_range", dr);
                getMainActivity().startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "The start date must be prior to the end date!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    private View.OnClickListener onClickTakePhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getMainActivity(), ReceiptActivity.class);
            getMainActivity().startActivity(intent);
        }
    };

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

    private void requestInternetPermission() {
        int internetPermission = ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.INTERNET);

        if (internetPermission != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
