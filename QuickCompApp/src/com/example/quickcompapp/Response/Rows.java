package com.example.quickcompapp.Response;

import android.os.Parcel;
import android.os.Parcelable;

public class Rows implements Parcelable {
	private String[] Row;

    public String[] getRow ()
    {
        return Row;
    }

    public void setRow (String[] Row)
    {
        this.Row = Row;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeStringArray(Row);
	}
	
	private void readFromParcel(Parcel in) {
		in.readStringArray(Row);
	}
	
	public static final Parcelable.Creator<Rows> CREATOR = new Parcelable.Creator<Rows>() {
		public Rows createFromParcel(Parcel in) {
			Rows rows = new Rows();
			rows.Row = in.createStringArray();
			return rows;
		}
		
		public Rows[] newArray(int size) {
			return new Rows[size];
		}
	};
}
