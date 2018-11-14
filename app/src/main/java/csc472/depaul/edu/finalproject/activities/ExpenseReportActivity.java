package csc472.depaul.edu.finalproject.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expense_report);

        DateRange dateRange = (DateRange) getIntent().getParcelableExtra("date_range");
        setTitle(R.string.title_activity_expense_report);

        DataProcessor.queryAccounts(AccountDatabase.getAccountDatabase(getApplicationContext()), new Transactions(getResources().getString(R.string.client_id), getResources().getString(R.string.secret), dateRange, getApplicationContext(), this));
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

//    @Override
//    public void getTransactions(List<Account> accounts)
//    {
//        //you should update your investment data before sending message below
//        Message message = Message.obtain();
//        if (message != null)
//        {
//            message.what = TIME_UPDATE;
////            message.arg1 =  nthUpdate;
//
//            mainThreadHandler.sendMessage(message);
//        }
//    }

    private Handler mainThreadHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            if ((msg != null) && (msg.what == TIME_UPDATE))
//            {
//                TextView investmentReport = findViewById(R.id.investmentReport);
//                if (investmentReport != null)
//                {
//                    investmentReport.getText();
//                    investmentReport.setText(Long.toString(msg.arg1));
//                }
//
//                TextView rate = findViewById(R.id.rate);
//                if (rate != null)
//                {
//                    float fRate = Float.valueOf(rate.getText().toString());
//
//                    TextView current = findViewById(R.id.current);
//                    if (current != null)
//                    {
//                        float fCurrent = Float.valueOf(current.getText().toString());
//
//                        fCurrent = fCurrent + (fCurrent * fRate);
//
//                        current.setText(String.format("%02.2f", fCurrent));
//                    }
//                }
//            }

            return true;
        }
    });

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
            pieChartView.setPieChartData(pieChartData);
        }
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
}
