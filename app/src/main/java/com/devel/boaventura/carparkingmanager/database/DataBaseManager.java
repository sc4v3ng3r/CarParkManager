package com.devel.boaventura.carparkingmanager.database;

import android.content.Context;

import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;

/**
 * Created by scavenger on 4/27/18.
 */

public class DataBaseManager {

    private static DataBaseManager m_instance = null;
    private static boolean m_isStarted = false;
    //private DataBaseHelper m_helper = null;
    private Context m_ctx;
    public static final synchronized boolean start(Context ctx){

        if ( !isStarted() ){
            m_instance = new DataBaseManager(ctx);
            m_isStarted = true;
            ParkSettingsManager.getInstance();
            //m_instance.m_helper = new DataBaseHelper(ctx);
        }
        return m_isStarted;
    }

    public static final synchronized DataBaseManager getInstance(){
        return m_instance;
    }

    public static final boolean isStarted(){
        return m_isStarted;
    }

    private DataBaseManager(Context ctx){
        m_ctx = ctx;
    }

    public final DataBaseHelper getDataBaseHelper(){
        return new DataBaseHelper(m_ctx);
    }


}
