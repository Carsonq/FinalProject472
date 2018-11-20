package csc472.depaul.edu.finalproject.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import csc472.depaul.edu.finalproject.db.ReceiptDate;
import csc472.depaul.edu.finalproject.db.TransactionDatabase;
import csc472.depaul.edu.finalproject.db.TransactionViewModel;
import csc472.depaul.edu.finalproject.models.DateRange;
import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.models.ILoadDataObserver;
import csc472.depaul.edu.finalproject.models.TransactionLoadViewAdapter;
import csc472.depaul.edu.finalproject.models.Transactions;
import csc472.depaul.edu.finalproject.db.DataProcessor;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ExpenseReportActivity extends AppCompatActivity implements ILoadDataObserver {
    private RecyclerView recyclerView;
    private TransactionLoadViewAdapter transactionLoadViewAdapter;
    private TransactionViewModel transactionViewModel;

    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Transactions transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_expense_report);

        DateRange dateRange = (DateRange) getIntent().getParcelableExtra("date_range");

        transactions = new Transactions(getResources().getString(R.string.client_id), getResources().getString(R.string.secret), dateRange, getApplicationContext(), this);
        DataProcessor.deleteTransactions(TransactionDatabase.getTransactionDatabase(getApplicationContext()), transactions);

        final EditText start_date = findViewById(R.id.start_date);
        start_date.setHint(dateRange.getStartDate().toString());
        if (start_date != null) {
            start_date.setOnClickListener(onClickCalendar);
        }

        final EditText end_date = findViewById(R.id.end_date);
        end_date.setHint(dateRange.getEndDate().toString());
        if (end_date != null) {
            end_date.setOnClickListener(onClickCalendar);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_transaction);
        transactionLoadViewAdapter = new TransactionLoadViewAdapter(this);

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.getAllTransactions().observe(this, transactions -> transactionLoadViewAdapter.setData(transactions));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(transactionLoadViewAdapter);
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

    @Override
    public void loadData() {
        SharedPreferences sp = getSharedPreferences(getResources().getString(R.string.sp_filename), Activity.MODE_PRIVATE);
        if (sp != null) {
            Map<String, String> res = (Map<String, String>) sp.getAll();
            Collection<String> vals = res.values();
            float total_amount = 0.0f;
            for (String v : vals) {
                total_amount = total_amount + Float.parseFloat(v);
            }

            PieChartView pieChartView = findViewById(R.id.chart);
            List<SliceValue> pieData = new ArrayList<>();
            int[] colors = colorPick();
            DecimalFormat df = new DecimalFormat("#.#");
            int colorIdx = colors.length - 1;
            for (String k : res.keySet()) {
                if (colorIdx < 0) {
                    colorIdx = colors.length - 1;
                }
                float percentage = Float.parseFloat(res.get(k)) / total_amount;
                pieData.add(new SliceValue(percentage).setColor(colors[colorIdx]).setLabel(k + " : " + df.format(percentage * 100) + "%"));
                colorIdx--;
            }

            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true).setValueLabelTextSize(14);

            pieChartView.setPieChartData(pieChartData);
        }

        datePickerSetup();
    }

    @Override
    public void loadData(List<ReceiptDate> receiptDates) {

    }

    private void datePickerSetup() {
        final EditText start_date = findViewById(R.id.start_date);
        if (start_date != null) {
            start_date.setOnClickListener(onClickCalendar);
        }

        final EditText end_date = findViewById(R.id.end_date);
        if (end_date != null) {
            end_date.setOnClickListener(onClickCalendar);
        }
    }

    private int[] colorPick() {

        int[] colors = new int[ColorTemplate.PASTEL_COLORS.length + ColorTemplate.VORDIPLOM_COLORS.length];
        int idx = 0;

        for (int i : ColorTemplate.PASTEL_COLORS) {
            colors[idx] = i;
            idx++;
        }

        for (int i : ColorTemplate.VORDIPLOM_COLORS) {
            colors[idx] = i;
            idx++;
        }

        return colors;
    }

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

            new DatePickerDialog(getExpenseReportActivity(), date,
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

    private final ExpenseReportActivity getExpenseReportActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        MenuItem item = menu.getItem(0);
        if (item != null) {
            item.setOnMenuItemClickListener(onClickAddAccount);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private MenuItem.OnMenuItemClickListener onClickAddAccount = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            refreshActivity();
            return true;
        }
    };

    private void refreshActivity() {
        final EditText start_date = findViewById(R.id.start_date);
        final EditText end_date = findViewById(R.id.end_date);

        String start_date_str = start_date.getText().toString();
        String end_date_str = end_date.getText().toString();

        if (start_date_str.equals("") || end_date_str.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Start date and end date could not be empty!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            DateRange dr = new DateRange(start_date_str, end_date_str);
            if (dr.isValid()) {
                Intent intent = getIntent();
                intent.putExtra("date_range", dr);
                finish();
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "The start date must be prior to the end date!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        ExpenseReportActivity.super.onBackPressed();
        finish();
    }
}