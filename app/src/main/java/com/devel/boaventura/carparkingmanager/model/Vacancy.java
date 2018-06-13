package com.devel.boaventura.carparkingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by scavenger on 4/18/18.
 *
 */

/**
 *
 * @class Vacancy
 * Classe que representa uma VAGA
 * no estacionamento.
 */
public class Vacancy implements Parcelable {

    private long m_id;
    private String m_number;
    private int m_status = STATUS_AVAILABLE;
    private int m_type;
    private ArrayList<Park> m_parking; // p/ manter o  historico de tudo que ocorreu com esta vaga.
    private Client m_owner = null;

    public static final int STATUS_AVAILABLE = 0x01;// utilizar recursos String
    public static final int STATUS_UNAVAILABLE = 0x00;// utilizar recuros String

    public static final int TYPE_PRIVATE = 0x01;
    public static final int TYPE_ROTARY = 0x00;

    public Vacancy(){
        m_parking = new ArrayList<>();
    } // so para teste

    public Vacancy(String number, int type,int status){
        setNumber(number);
        setType(type);
        setStatus(status);
        m_parking = new ArrayList<>();
    }

    protected Vacancy(Parcel in) {
        m_id = in.readLong();
        m_number = in.readString();
        m_status = in.readInt();
        m_type = in.readInt();
        m_parking = in.createTypedArrayList(Park.CREATOR);
        m_owner = in.readParcelable(Client.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(m_id);
        dest.writeString(m_number);
        dest.writeInt(m_status);
        dest.writeInt(m_type);
        dest.writeTypedList(m_parking);
        dest.writeParcelable(m_owner, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vacancy> CREATOR = new Creator<Vacancy>() {
        @Override
        public Vacancy createFromParcel(Parcel in) {
            return new Vacancy(in);
        }

        @Override
        public Vacancy[] newArray(int size) {
            return new Vacancy[size];
        }
    };

    public Client getClient(){ return m_owner;}
    public void setClient(Client c){ m_owner = c; }

    public final long getId() {
        return m_id;
    }

    public final void setId(long m_id) {
        this.m_id = m_id;
    }

    public final String getNumber() {
        return m_number;
    }

    public final void setNumber(String number) {
        this.m_number = number;
    }

    public final int getStatus() {
        return m_status;
    }

    public final void setStatus (int status) {
        this.m_status = status;
    }

    public final int getType() {
        return m_type;
    }

    public final void setType(int type) {
        this.m_type = type;
    }

    public Park getCurrentService(){
        if (m_parking.isEmpty())
            return null;

        return m_parking.get(m_parking.size()-1);
    }

    public ArrayList<Park> getServices(){
        return m_parking;
    }

    public void addService(Park park){
        m_parking.add(park);
        /*
        * ISSO NAO VAI FICAR AQUI!
        * EH REGRA DE NEGOCIO!
        * */
        if (park.getType() == Park.SERVICE_ENTRY)
            setStatus(Vacancy.STATUS_UNAVAILABLE);
        else
            setStatus(Vacancy.STATUS_AVAILABLE);
    }

    public Park getService(int index){
        return m_parking.get(index);
    }

    public void setServiceList(ArrayList<Park> services){
        m_parking = services;
    }
}