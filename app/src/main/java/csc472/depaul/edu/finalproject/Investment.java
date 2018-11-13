package csc472.depaul.edu.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

public final class
Investment implements Parcelable
{
    private String  name           = null;
    private Balance initialBalance = null;
    private Balance currentBalance = null;
    private Rate    rate           = null;

    public Investment(final String sName, final Float fInitialBalance, final Float fRate)
    {
        name           = new String (sName);
        initialBalance = new Balance(fInitialBalance);
        currentBalance = new Balance(fInitialBalance);
        rate           = new Rate   (fRate);
    }

    /*
        @param dCurrentBalance - the current balance of the investment
    */

    public void setCurrentBalance(final Float fCurrentBalance)
    {
        currentBalance = new Balance(fCurrentBalance);
    }

    /*
        @return name - the name of the investment
     */

    public final String getName()
    {
        return name;
    }

    /*
        @return initialBalance - the initial balance of the investment
     */

    public final Balance getInitialBalance()
    {
        return initialBalance;
    }

    /*
        @return currentBalance - the current balance of the investment
     */

    public final Balance getCurrentBalance()
    {
        return currentBalance;
    }

    /*
        @return rate - the rate of the investment
     */

    public final Rate getRate()
    {
        return rate;
    }

    public Investment(Parcel in){
        this.name = in.readString();
        this.initialBalance = in.readParcelable(Balance.class.getClassLoader());
        this.currentBalance = in.readParcelable(Balance.class.getClassLoader());
        this.rate = in.readParcelable(Rate.class.getClassLoader());
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.initialBalance, 0);
        dest.writeParcelable(this.currentBalance, 0);
        dest.writeParcelable(this.rate, 0);
    }

    public static final Parcelable.Creator<Investment> CREATOR = new Parcelable.Creator<Investment>()
    {
        public Investment createFromParcel(Parcel in)
        {
            return new Investment(in);
        }

        public Investment[] newArray(int size)
        {
            return new Investment[size];
        }
    };
}
