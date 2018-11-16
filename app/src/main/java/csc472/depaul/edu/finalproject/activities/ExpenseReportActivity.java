package csc472.depaul.edu.finalproject.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import csc472.depaul.edu.finalproject.models.DateRange;
import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.models.ILoadDataObserver;
import csc472.depaul.edu.finalproject.models.Transactions;
import csc472.depaul.edu.finalproject.db.AccountDatabase;
import csc472.depaul.edu.finalproject.db.DataProcessor;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ExpenseReportActivity extends AppCompatActivity implements ILoadDataObserver {
    private final static int TIME_UPDATE = 0x123456;
    private static String NAME_HOLDER = "NAME_HOLDER";
    private static String INITIAL_HOLDER = "INITIAL_HOLDER";
    private static String CURRENT_HOLDER = "CURRENT_HOLDER";
    private static String RATE_HOLDER = "RATE_HOLDER";
    private PieChart pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_expense_report);

        DateRange dateRange = (DateRange) getIntent().getParcelableExtra("date_range");

        //DataProcessor.queryAccounts(AccountDatabase.getAccountDatabase(getApplicationContext()), new Transactions(getResources().getString(R.string.client_id), getResources().getString(R.string.secret), dateRange, getApplicationContext(), this));


        pieChartView = findViewById(R.id.chart);
        pieChartView.setBackgroundColor(Color.WHITE);

        moveOffScreen();
        pieChartView.setUsePercentValues(true);
        pieChartView.getDescription().setEnabled(false);
        pieChartView.setDrawHoleEnabled(true);
        pieChartView.setMaxAngle(180);
        pieChartView.setRotationAngle(180);
        pieChartView.setCenterTextOffset(0, -20);
        setData(4, 100);
        pieChartView.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        Legend l = pieChartView.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(50f);

        pieChartView.setEntryLabelColor(Color.WHITE);
        pieChartView.setEntryLabelTextSize(12f);
    }

//    private void startUpdate()
//    {
//        InvestmentThread.getInvestmentThread().start(this);
//    }

//    private void stopUpdate()
//    {
//        InvestmentThread.getInvestmentThread().stop(this);
//    }

    @Override
    protected void onStart() {
        super.onStart();
//        startUpdate();
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
//        stopUpdate();
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
////            final TextView name = findViewById(R.id.name);
////            if (name != null)
////            {
////                savedInstanceState.putString(NAME_HOLDER, name.getText().toString());
////            }
//
//            final TextView initial = findViewById(R.id.initial);
//            if (initial != null)
//            {
//                savedInstanceState.putString(INITIAL_HOLDER, initial.getText().toString());
//            }
//
//            final TextView current = findViewById(R.id.current);
//            if (current != null)
//            {
//                savedInstanceState.putString(CURRENT_HOLDER, current.getText().toString());
//            }
//
//            final TextView rate = findViewById(R.id.rate);
//            if (rate != null)
//            {
//                savedInstanceState.putString(RATE_HOLDER, rate.getText().toString());
//            }
//        }
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

            pieChartView = findViewById(R.id.chart);
            pieChartView.setBackgroundColor(Color.WHITE);

            moveOffScreen();
            setData(4, 100);

            List<SliceValue> pieData = new ArrayList<>();

            ArrayList<Integer> colors = colorPick();
            DecimalFormat df = new DecimalFormat("#.#");
            for (String k : res.keySet()) {
                String outputFormat = df.format(Float.parseFloat(res.get(k)));
                pieData.add(new SliceValue(Float.parseFloat(res.get(k)) / total_amount).setColor(colors.get(0)).setLabel(k + " : " + outputFormat));
                colors.remove(0);
                if (colors.isEmpty()) {
                    colors = colorPick();
                }
            }

            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true).setValueLabelTextSize(14);
//            pieChartData.setHasCenterCircle(true).setCenterText1("Consumption Categories").setCenterText1FontSize(14).setCenterText1Color(Color.parseColor("#0097A7"));
//            pieChartView.setPieChartData(pieChartData);
        }
    }

    String[] countries = new String[] {"a", "b", "c", "d", "e"};
    private void setData(int count, int range) {
        ArrayList<PieEntry> values = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) ((Math.random()*range)+range/5);
            values.add(new PieEntry(val, countries[i]));
        }

        PieDataSet dataSet = new PieDataSet(values, "Partner");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);

        pieChartView.setData(data);
        pieChartView.invalidate();
    }

    private void moveOffScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int offset = (int) (height * 0.1);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pieChartView.getLayoutParams();
        params.setMargins(0, 0, 0, -offset);
        pieChartView.setLayoutParams(params);
    }

    private ArrayList<Integer> colorPick() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        return colors;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
