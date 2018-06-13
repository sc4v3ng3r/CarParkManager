package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
//import com.devel.boaventura.carparkingmanager.model.Service;

/**
 * Created by scavenger on 4/28/18.
 */

public class ServiceDAO {
    private static ServiceDAO m_instance;
    private DataBaseHelper m_helper;

    private ServiceDAO(){
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    public static final synchronized ServiceDAO getInstance(){
        if(m_instance == null)
            m_instance = new ServiceDAO();

        return m_instance;
    }


}
