package com.devel.boaventura.carparkingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scavenger on 4/18/18.
 */

public class Vehicle implements Parcelable {
    private long m_id;
    private String m_licensePlate;

    public Vehicle(String licensePlate){
        setLicensePlate(licensePlate);
    }

    protected Vehicle(Parcel in) {
        m_id = in.readLong();
        m_licensePlate = in.readString();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        this.m_id = id;
    }

    public String getLicensePlate() {
        return m_licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.m_licensePlate = licensePlate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(m_id);
        dest.writeString(m_licensePlate);
    }
}
