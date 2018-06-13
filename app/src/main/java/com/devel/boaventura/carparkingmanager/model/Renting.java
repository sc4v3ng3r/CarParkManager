package com.devel.boaventura.carparkingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scavenger on 4/19/18.
 * @class Classe que representa um Aluguel!
 */

public class Renting implements Parcelable{

    private long m_id;
    private long m_startTime;
    private long m_endTime;
    private double m_value;
    private int m_isPaid;
    private int m_state; // valid | expired
    private Vacancy m_vacancy;
    private Client m_client;

    public static final int STATE_EXPIRED = 0x00;
    public static final int STATE_VALID = 0x01;

    public Renting(){}

    public Renting(long startTime, long endTime,
                   int payment, Vacancy vacancy){
        setStartTime(startTime);
        setEndTime(endTime);
        setPayment(payment);
        setState(STATE_VALID);
        setVacancy(vacancy);
    }

    protected Renting(Parcel in) {
        m_id = in.readLong();
        m_startTime = in.readLong();
        m_endTime = in.readLong();
        m_value = in.readDouble();
        m_isPaid = in.readInt() ;
        m_state = in.readInt();
        m_vacancy = in.readParcelable(Vacancy.class.getClassLoader());
        m_client = in.readParcelable(Client.class.getClassLoader());
    }

    public static final Creator<Renting> CREATOR = new Creator<Renting>() {
        @Override
        public Renting createFromParcel(Parcel in) {
            return new Renting(in);
        }

        @Override
        public Renting[] newArray(int size) {
            return new Renting[size];
        }
    };

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        this.m_id = id;
    }

    public long getStartTime() {
        return m_startTime;
    }

    public void setStartTime(long startTime) {
        this.m_startTime = startTime;
    }

    public long getEndTime() {
        return m_endTime;
    }

    public void setEndTime(long endTime) {
        this.m_endTime = endTime;
    }

    public double getValue() {
        return m_value;
    }

    public void setValue(double value) {
        this.m_value = value;
    }

    public boolean isPaid() {

        return m_isPaid ==1 ? true : false;
    }

    public void setPayment(int payment) {
        this.m_isPaid = payment;
    }

    public int getState() {
        return m_state;
    }

    public void setState(int state) {
        this.m_state = state;
    }

    public Vacancy getVacancy() {
        return m_vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.m_vacancy = vacancy;
    }

    public void setClient(Client c) { m_client = c;}
    public Client getClient(){ return m_client; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(m_id);
        dest.writeLong(m_startTime);
        dest.writeLong(m_endTime);
        dest.writeDouble(m_value);
        dest.writeInt(m_isPaid);
        dest.writeInt(m_state);
        dest.writeParcelable(m_vacancy, flags);
        dest.writeParcelable(m_client, flags);
    }
}
