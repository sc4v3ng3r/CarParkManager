package com.devel.boaventura.carparkingmanager.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
//import com.devel.boaventura.carparkingmanager.model.ServiceDetails;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/28/18.
 */

public class ServiceDetailsDAO {
    private static ServiceDetailsDAO m_instance;
    private DataBaseHelper m_helper;

    public static final synchronized ServiceDetailsDAO getInstance(){
        if (m_instance ==null)
            m_instance = new ServiceDetailsDAO();
        return m_instance;
    }

    private ServiceDetailsDAO(){
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    /*
    ArrayList<ServiceDetails> getServiceDetailsByVacancyId(long id){
        ArrayList<ServiceDetails> list = new ArrayList<>();
        SQLiteDatabase db = m_helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_SERVICE_DETAILS + " WHERE "
                + DataBaseHelper.SERVICE_DETAILS_COLUMN_ID_VACANCY + " =?";

        String[] arguments = new String[]{ String.valueOf(id)};

        Cursor cursor = db.rawQuery(sql, arguments);

        while (cursor.moveToNext()){
            ServiceDetails details = new ServiceDetails();


        }

        db.close();
        return list;

    }
    */

}
