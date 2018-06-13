package com.devel.boaventura.carparkingmanager.model;

/**
 * Created by scavenger on 4/21/18.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Classe que representa Taxa de cobrança
 */

public class Tax {
    private long m_id;
    private String m_name;
    private double m_until1H, m_from1hTo4h,
        m_from4hTo8h, m_after8h;


    public Tax(){}

    public long getId() {
        return m_id;
    }

    public void setId(long m_id) {
        this.m_id = m_id;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String m_name) {
        this.m_name = m_name;
    }

    public double getUntil1H() {
        return m_until1H;
    }

    public void setUntil1H(double m_until1H) {
        this.m_until1H = m_until1H;
    }

    public double getFrom1hTo4h() {
        return m_from1hTo4h;
    }

    public void setFrom1hTo4h(double m_from1hTo4h) {
        this.m_from1hTo4h = m_from1hTo4h;
    }

    public double getFrom4hTo8h() {
        return m_from4hTo8h;
    }

    public void setFrom4hTo8h(double m_from4hTo8h) {
        this.m_from4hTo8h = m_from4hTo8h;
    }

    public double getAfter8h() {
        return m_after8h;
    }

    public void setAfter8h(double m_after8h) {
        this.m_after8h = m_after8h;
    }
}
