package com.devel.boaventura.carparkingmanager.model;

/**
 * Created by scavenger on 4/21/18.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Classe que contem as atuais configurações de
 * um Park.
 *
 *  - Nome
 *  - Número total de vagas
 *  - Número de vagas Privadas
 *  - Número de vafas Rotativas
 *  - Tarifas
 *
 */

public class ParkPerfil implements Parcelable{
    private long m_id;
    private String m_perfilName;
    private int m_rotaryVacancys = 0;
    private int m_privateVacancyes = 0;
    //private ArrayList<Tax> m_taxes;
    private ArrayList<Vacancy> m_vacancyList;

    public ParkPerfil(){
        m_vacancyList = new ArrayList<>();
        //m_taxes = new ArrayList<>();
    }

    protected ParkPerfil(Parcel in) {
        m_id = in.readLong();
        m_perfilName = in.readString();
        m_rotaryVacancys = in.readInt();
        m_privateVacancyes = in.readInt();
       // m_taxes = in.createTypedArrayList(Tax.CREATOR);
        m_vacancyList = in.createTypedArrayList(Vacancy.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(m_id);
        dest.writeString(m_perfilName);
        dest.writeInt(m_rotaryVacancys);
        dest.writeInt(m_privateVacancyes);
        //dest.writeTypedList(m_taxes);
        dest.writeTypedList(m_vacancyList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParkPerfil> CREATOR = new Creator<ParkPerfil>() {
        @Override
        public ParkPerfil createFromParcel(Parcel in) {
            return new ParkPerfil(in);
        }

        @Override
        public ParkPerfil[] newArray(int size) {
            return new ParkPerfil[size];
        }
    };

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        this.m_id = id;
    }

    public String getPerfilName() {
        return m_perfilName;
    }

    public void setPerfilName(String perfilName) {
        this.m_perfilName = perfilName;
    }

    public int getRotaryVacancys() {
        return m_rotaryVacancys;
    }

    public void setRotaryVacancys(int rotaryVacancys) {

        this.m_rotaryVacancys = rotaryVacancys;
        //setTotalVacancyes(this.m_privateVacancyes + this.m_rotaryVacancys);
    }

    public int getPrivateVacancyes() {
        return m_privateVacancyes;
    }

    public void setPrivateVacancyes(int privateVacancyes) {
        this.m_privateVacancyes = privateVacancyes;
        //setTotalVacancyes(this.m_privateVacancyes + this.m_rotaryVacancys);
    }

    public int getTotalVacancyes() {
        return m_vacancyList.size();
    }

   /* private void setTotalVacancyes(int totalVacancyes) {
        this.m_totalVacancyes = totalVacancyes;
    }
    */
    public ArrayList<Vacancy> getVacancyList(){ return m_vacancyList; }

    public void setVacancyList(ArrayList<Vacancy> list){
        m_vacancyList = list;
    }

    public void addVacancy(Vacancy v){
        m_vacancyList.add(v);
    }

    public Vacancy getVacancy(final int number){
        if (number <= 0)
            return null;
        return m_vacancyList.get(number-1);
    }

}
