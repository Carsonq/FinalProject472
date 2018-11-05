package csc472.depaul.edu.finalproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ExpenseReportActivity extends AppCompatActivity implements IInvestmentObserver
{
    private final static int TIME_UPDATE = 0x123456;
    private static String NAME_HOLDER = "NAME_HOLDER";
    private static String INITIAL_HOLDER = "INITIAL_HOLDER";
    private static String CURRENT_HOLDER = "CURRENT_HOLDER";
    private static String RATE_HOLDER = "RATE_HOLDER";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expense_report);

        if (savedInstanceState != null)
        {
            setTitle(savedInstanceState.getString(NAME_HOLDER));
//            final TextView name = findViewById(R.id.name);
//            if (name != null) {
//                name.setText(savedInstanceState.getString(NAME_HOLDER));
//            }

            final TextView initial = findViewById(R.id.initial);
            if (initial != null) {
                initial.setText(savedInstanceState.getString(INITIAL_HOLDER));
            }

            final TextView current = findViewById(R.id.current);
            if (current != null) {
                current.setText(savedInstanceState.getString(CURRENT_HOLDER));
            }

            final TextView rate = findViewById(R.id.rate);
            if (rate != null) {
                rate.setText(savedInstanceState.getString(RATE_HOLDER));
            }
        }
        else {
            DateRange dateRange = (DateRange) getIntent().getParcelableExtra("date_range");
            setTitle(dateRange.getStartDate());

//            final TextView name = findViewById(R.id.name);
//            if (name != null) {
//                name.setText(investment.getName());
//            }

//            final TextView initial = findViewById(R.id.initial);
//            if (initial != null) {
//                initial.setText(investment.getInitialBalance().toString());
//            }
//
//            final TextView current = findViewById(R.id.current);
//            if (current != null) {
//                current.setText(investment.getInitialBalance().toString());
//            }
//
//            final TextView rate = findViewById(R.id.rate);
//            if (rate != null) {
//                rate.setText(investment.getRate().toString());
//            }
        }
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
    protected void onStart()
    {
        super.onStart();
//        startUpdate();
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
//        stopUpdate();
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
//            final TextView name = findViewById(R.id.name);
//            if (name != null)
//            {
//                savedInstanceState.putString(NAME_HOLDER, name.getText().toString());
//            }

            final TextView initial = findViewById(R.id.initial);
            if (initial != null)
            {
                savedInstanceState.putString(INITIAL_HOLDER, initial.getText().toString());
            }

            final TextView current = findViewById(R.id.current);
            if (current != null)
            {
                savedInstanceState.putString(CURRENT_HOLDER, current.getText().toString());
            }

            final TextView rate = findViewById(R.id.rate);
            if (rate != null)
            {
                savedInstanceState.putString(RATE_HOLDER, rate.getText().toString());
            }
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void investmentUpdate(int nthUpdate)
    {
        //you should update your investment data before sending message below
        Message message = Message.obtain();
        if (message != null)
        {
            message.what = TIME_UPDATE;
            message.arg1 =  nthUpdate;

            mainThreadHandler.sendMessage(message);
        }
    }

    private Handler mainThreadHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if ((msg != null) && (msg.what == TIME_UPDATE))
            {
                TextView investmentReport = findViewById(R.id.investmentReport);
                if (investmentReport != null)
                {
                    investmentReport.getText();
                    investmentReport.setText(Long.toString(msg.arg1));
                }

                TextView rate = findViewById(R.id.rate);
                if (rate != null)
                {
                    float fRate = Float.valueOf(rate.getText().toString());

                    TextView current = findViewById(R.id.current);
                    if (current != null)
                    {
                        float fCurrent = Float.valueOf(current.getText().toString());

                        fCurrent = fCurrent + (fCurrent * fRate);

                        current.setText(String.format("%02.2f", fCurrent));
                    }
                }
            }

            return true;
        }
    });
}
