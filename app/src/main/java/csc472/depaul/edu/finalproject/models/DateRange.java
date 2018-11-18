package csc472.depaul.edu.finalproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateRange implements Parcelable {
    String startDate;
    String endDate;
    String formatString;

    public DateRange(String startD, String endD) {
        formatString = "MM/dd/yyyy";
        startDate = startD;
        endDate = endD;
    }

    public DateRange(Date startD, Date endD) {
        formatString = "MM/dd/yyyy";
        startDate = parseDate(startD);
        endDate = parseDate(endD);
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartDateDashFormat() {
        String[] mdy = startDate.split("/");
        return mdy[2]+'-'+mdy[0]+'-'+mdy[1];
    }

    public String getEndDateDashFormat() {
        String[] mdy = endDate.split("/");
        return mdy[2]+'-'+mdy[0]+'-'+mdy[1];
    }

    public String getEndDate() {
        return endDate;
    }

    public Date getDStartDate() {
        try {
            return (new SimpleDateFormat(formatString)).parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getDEndDate() {
        try {
            return (new SimpleDateFormat(formatString)).parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFormatString() {
        return formatString;
    }

    public boolean isValid() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        try {
            startCalendar.setTime(dateFormat.parse(startDate));
            endCalendar.setTime(dateFormat.parse(endDate));
            return startCalendar.compareTo(endCalendar) <= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String parseDate(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
        return dateFormat.format(d);
    }

    public DateRange(Parcel in) {
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.formatString = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.formatString);
    }

    public static final Parcelable.Creator<DateRange> CREATOR = new Parcelable.Creator<DateRange>() {
        public DateRange createFromParcel(Parcel in) {
            return new DateRange(in);
        }

        public DateRange[] newArray(int size) {
            return new DateRange[size];
        }
    };
}
