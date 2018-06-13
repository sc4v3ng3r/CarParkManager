package com.devel.boaventura.carparkingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * Created by scavenger on 4/19/18.
 *
 */

public class Client implements Parcelable {

    private long m_id;
    private String m_name;
    private ArrayList<Renting> m_clientRentList;
    //private HashMap<Long, Renting> m_rentings;

    public Client(String name) {
        setName(name);
        m_clientRentList = new ArrayList<>();
    }

    public Client(long id, String name, ArrayList<Renting> rentList){
        setId(id);
        setName(name);
        setRentList(rentList);
    }

    protected Client(Parcel in) {
        m_id = in.readLong();
        m_name = in.readString();
        m_clientRentList = in.createTypedArrayList(Renting.CREATOR);
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        this.m_id = id;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        this.m_name = name;
    }


    public ArrayList<Renting> getClientRentList() {
        return m_clientRentList;
    }


    public void setRentList(ArrayList<Renting> clientRentList) {
        this.m_clientRentList = clientRentList;
    }

    public void addRenting(Renting renting){
        m_clientRentList.add(renting);
    }

    public Renting getCurrentRenting(){
        Renting renting = null;
        if ( !m_clientRentList.isEmpty() ){
            renting = m_clientRentList.get(m_clientRentList.size()-1);
        }
        return renting;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(m_id);
        dest.writeString(m_name);
        dest.writeTypedList(m_clientRentList);
    }

}