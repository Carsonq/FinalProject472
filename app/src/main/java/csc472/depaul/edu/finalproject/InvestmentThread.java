package csc472.depaul.edu.finalproject;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InvestmentThread implements Runnable
{
    private int investment = 0;
    private ScheduledThreadPoolExecutor scheduler = null;
    private ArrayList<IInvestmentObserver> investmentObservers = new ArrayList<IInvestmentObserver>();
	private static InvestmentThread investmentThread = null;

	static
	{
		investmentThread = new InvestmentThread();
	}

	private InvestmentThread()
	{
	}

	public static InvestmentThread getInvestmentThread()
	{
		return investmentThread;
	}

	public synchronized void start(final IInvestmentObserver iInvestmentObserver)
	{
        investmentObservers.add(iInvestmentObserver);

        if (scheduler == null)
        {
            scheduler = new ScheduledThreadPoolExecutor(1, new csc472.depaul.edu.finalproject.NamedThreadFactory("InvestmentThread"));
            scheduler.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
		}
	}

    public synchronized void stop(final IInvestmentObserver iInvestmentObserver)
    {
        investmentObservers.remove(iInvestmentObserver);

        if (scheduler != null)
        {
            scheduler.shutdown();

            scheduler = null;
        }
    }

	@Override
	public synchronized void run()
	{
	    Log.v("InvestmentThread", "Running");
        ++investment;

        if (investmentObservers != null)
        {
            Iterator<IInvestmentObserver> iiInvestmentObservers = investmentObservers.iterator();
            while (iiInvestmentObservers.hasNext())
            {
                IInvestmentObserver iInvestmentObservers = iiInvestmentObservers.next();
                if (iInvestmentObservers != null)
                {
                    iInvestmentObservers.investmentUpdate(investment);
                }
            }
        }
	}
}
