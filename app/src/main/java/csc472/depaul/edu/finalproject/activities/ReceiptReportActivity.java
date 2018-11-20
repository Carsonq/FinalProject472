package csc472.depaul.edu.finalproject.activities;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.db.DataProcessor;
import csc472.depaul.edu.finalproject.db.ReceiptDatabase;
import csc472.depaul.edu.finalproject.db.ReceiptDate;
import csc472.depaul.edu.finalproject.db.ReceiptViewModel;
import csc472.depaul.edu.finalproject.models.DateRange;
import csc472.depaul.edu.finalproject.models.ILoadDataObserver;
import csc472.depaul.edu.finalproject.models.ReceiptLoadViewAdapter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

public class ReceiptReportActivity extends AppCompatActivity implements ILoadDataObserver {

    private RecyclerView recyclerView;
    private ReceiptLoadViewAdapter receiptLoadViewAdapter;
    private ReceiptViewModel receiptViewModel;

    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_receipt_report);

        DateRange dateRange = (DateRange) getIntent().getParcelableExtra("date_range");


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

        DataProcessor.queryReceiptsGroupDate(ReceiptDatabase.getReceiptDatabase(getApplicationContext()), getReceiptReportActivity(), new String[]{dateRange.getStartDateDashFormat(), dateRange.getEndDateDashFormat()});

        recyclerView = (RecyclerView) findViewById(R.id.recycler_receipt);
        receiptLoadViewAdapter = new ReceiptLoadViewAdapter(this);

        receiptViewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);
        receiptViewModel.getReceipts(dateRange.getStartDateDashFormat(), dateRange.getEndDateDashFormat()).observe(this, transactions -> receiptLoadViewAdapter.setData(transactions));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(receiptLoadViewAdapter);
    }

    private final ReceiptReportActivity getReceiptReportActivity() {
        return this;
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

            new DatePickerDialog(getReceiptReportActivity(), date,
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

    @Override
    public void loadData() {
    }

    @Override
    public void loadData(List<ReceiptDate> receiptDates) {
        if (receiptDates.size() != 0) {
            ComboLineColumnChartView comboLineColumnChartView = findViewById(R.id.receipt_chart);
            ComboLineColumnChartData comboLineColumnChartData = new ComboLineColumnChartData();

            float columns_high = 0.0f;
            for (ReceiptDate r : receiptDates) {
                if (r.getDayTotal() > columns_high) {
                    columns_high = (float) r.getDayTotal();
                }
            }

            columns_high = columns_high * 1.5f;

            List<Column> columns = new ArrayList<>();
            List<SubcolumnValue> subcolumnValues;
            List<Line> lines = new ArrayList<>();
            List<PointValue> lineValues = new ArrayList<>();

            int[] colors = colorPick();
            int colorIdx = colors.length - 1;
            for (int i = 0; i < receiptDates.size(); i++) {
                if (colorIdx < 0) {
                    colorIdx = colors.length - 1;
                }

                ReceiptDate receiptDate = receiptDates.get(i);
                subcolumnValues = new ArrayList<>();
                subcolumnValues.add(new SubcolumnValue(
                        (float) receiptDate.getDayTotal(),
                        colors[colorIdx]));
                columns.add(new Column(subcolumnValues));

                lineValues.add(new PointValue(i,
                        ((float) receiptDate.getDayTotal())));

                colorIdx--;
            }

            Line line = new Line(lineValues);
            line.setHasLabels(true);
            line.setPointRadius(3);
            line.setHasLines(true);
            lines.add(line);

            comboLineColumnChartData.setColumnChartData(new ColumnChartData(columns));
            comboLineColumnChartData.setLineChartData(new LineChartData(lines));


            Axis axisX = new Axis();
            Axis axisY = Axis
                    .generateAxisFromRange(0, columns_high, columns_high / 10)
                    .setMaxLabelChars(6)
                    .setTextColor(Color.GRAY)
                    .setHasLines(true);
            axisX.setName("Date: " + receiptDates.get(0).getReceiptDate() + " ~ " + receiptDates.get(receiptDates.size() - 1).getReceiptDate());
            axisY.setName("Cost");
            comboLineColumnChartData.setAxisXBottom(axisX);
            comboLineColumnChartData.setAxisYLeft(axisY);

            comboLineColumnChartView.setComboLineColumnChartData(comboLineColumnChartData);
        }

        datePickerSetup();
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

        int[] colors = new int[ChartUtils.COLORS.length];
        int idx = 0;

        for (int i : ChartUtils.COLORS) {
            colors[idx] = i;
            idx++;
        }

        return colors;
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
        ReceiptReportActivity.super.onBackPressed();
        finish();
    }
}
