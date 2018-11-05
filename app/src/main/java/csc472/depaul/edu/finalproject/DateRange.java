package csc472.depaul.edu.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateRange implements Parcelable {
    String startDate;
    String endDate;

    DateRange(String startD, String endD) {
        startDate = startD;
        endDate = endD;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isValid() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        try {
            startCalendar.setTime(dateFormat.parse(startDate));
            endCalendar.setTime(dateFormat.parse(endDate));
            return startCalendar.compareTo(endCalendar)<=0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public DateRange(Parcel in){
        this.startDate = in.readString();
        this.endDate = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDate);
        dest.writeString(this.startDate);
    }

    public static final Parcelable.Creator<DateRange> CREATOR = new Parcelable.Creator<DateRange>()
    {
        public DateRange createFromParcel(Parcel in)
        {
            return new DateRange(in);
        }

        public DateRange[] newArray(int size)
        {
            return new DateRange[size];
        }
    };
}
