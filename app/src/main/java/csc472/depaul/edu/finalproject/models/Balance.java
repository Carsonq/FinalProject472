package csc472.depaul.edu.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public final class Balance implements Parcelable
{
    private Float balance = 0.0f;

    public Balance(final Float fBalance)
    {
        balance = fBalance;
    }

    /*
        @param newBalance - the new balance of the investment
    */

    public void setBalance(final Float fBalance)
    {
        balance = fBalance;
    }

    /*
        @return balance - the balance of the investment
     */

    public final Float getBalance()
    {
        return balance;
    }

    @Override
    public final String toString()
    {
        return Float.toString(balance);
    }

    public Balance(Parcel in){
        this.balance = in.readFloat();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.balance);
    }

    public static final Parcelable.Creator<Balance> CREATOR = new Parcelable.Creator<Balance>()
    {
        public Balance createFromParcel(Parcel in)
        {
            return new Balance(in);
        }

        public Balance[] newArray(int size)
        {
            return new Balance[size];
        }
    };
}
