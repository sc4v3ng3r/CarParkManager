package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
import com.devel.boaventura.carparkingmanager.model.Vehicle;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/26/18.
 */

public class VehicleDAO {

    private static VehicleDAO m_instance = null;
    private ArrayList<Vehicle> m_data;
    private DataBaseHelper m_helper;

    public static final synchronized VehicleDAO getInstance(){
        if (m_instance == null)
            m_instance = new VehicleDAO();

        return m_instance;
    }

    private VehicleDAO(){
        m_data = new ArrayList<>();
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    public long addVehicle(final Vehicle v){
        long id = -1;

        SQLiteDatabase db = m_helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.VEHICLE_COLUMN_LICENSE_PLATE, v.getLicensePlate());

        id = db.insert(DataBaseHelper.TABLE_VEHICLE, null, values);
        db.close();
        return id;
    }

    public Vehicle getVehicleById(long id){
        Vehicle v = null;
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_VEHICLE + " WHERE " +
                DataBaseHelper.COLUMN_ID + " =?";
        String[] arguments = new String[]{ String.valueOf(id) };
        SQLiteDatabase db = m_helper.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, arguments);

        if (cursor.moveToNext()){
            v = new Vehicle( cursor.getString(
                    cursor.getColumnIndex(DataBaseHelper.VEHICLE_COLUMN_LICENSE_PLATE)
            ));

            v.setId( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID
            )));
        }

        db.close();
        return v;
    }


    /*FALTA FAZER UPDATE*/

    /*Falta fazer remove*/
}
