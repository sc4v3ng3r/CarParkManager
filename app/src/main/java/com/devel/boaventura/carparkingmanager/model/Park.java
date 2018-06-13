package com.devel.boaventura.carparkingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scavenger on 4/28/18.
 */

public class Park implements Parcelable{

    private long m_id;
    private long m_time;
    private int m_type;
    private double m_value = 0;
    private Vehicle m_vehicle;
    private Vacancy m_vacancy;

    public static final int SERVICE_ENTRY = 0x01;
    public static final int SERVICE_EXIT = 0x00;

    public Park(){}

    protected Park(Parcel in) {
        m_id = in.readLong();
        m_time = in.readLong();
        m_type = in.readInt();
        m_value = in.readDouble();
        m_vehicle = in.readParcelable(Vehicle.class.getClassLoader());
        m_vacancy = in.readParcelable(Vacancy.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(m_id);
        dest.writeLong(m_time);
        dest.writeInt(m_type);
        dest.writeDouble(m_value);
        dest.writeParcelable(m_vehicle, flags);
        dest.writeParcelable(m_vacancy, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Park> CREATOR = new Creator<Park>() {
        @Override
        public Park createFromParcel(Parcel in) {
            return new Park(in);
        }

        @Override
        public Park[] newArray(int size) {
            return new Park[size];
        }
    };

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        this.m_id = id;
    }

    public long getTime() {
        return m_time;
    }

    public void setTime(long time) {
        this.m_time = time;
    }

    public int getType() {
        return m_type;
    }

    public void setType(int type) {
        this.m_type = type;
    }

    public double getValue() {
        return m_value;
    }

    public void setValue(double value) {
        this.m_value = value;
    }

    public Vehicle getVehicle() {
        return m_vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.m_vehicle = vehicle;
    }

    public Vacancy getVacancy() {
        return m_vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.m_vacancy = vacancy;
    }
}
