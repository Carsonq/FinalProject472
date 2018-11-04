package csc472.depaul.edu.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

public final class Rate implements Parcelable
{
    private Float rate = 0.0f;

    public Rate(final Float fInitialRate)
    {
        rate = fInitialRate;
    }

    //read only pattern - no setters

    /*
        @return rate - the rate of the investment
     */

    public final Float getRate()
    {
        return rate;
    }

    @Override
    public final String toString()
    {
        return rate.toString();
    }

    public Rate(Parcel in){
        this.rate = in.readFloat();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.rate);
    }

    public static final Parcelable.Creator<Rate> CREATOR = new Parcelable.Creator<Rate>()
    {
        public Rate createFromParcel(Parcel in)
        {
            return new Rate(in);
        }

        public Rate[] newArray(int size)
        {
            return new Rate[size];
        }
    };
}
