package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.Vehicle;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/28/18.
 */

public class ParkDAO {
    private static ParkDAO m_instance = null;
    private DataBaseHelper m_helper;

    private ParkDAO(){
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    public static final synchronized ParkDAO getInstance(){
        if (m_instance == null)
            m_instance = new ParkDAO();

        return m_instance;
    }


    public long addPark(Park park){
        long id = -1;
        SQLiteDatabase db = m_helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.PARK_COLUMN_TYPE, park.getType());
        //Log.i("DBG", "ParkDAO::addPark type: " + park.getType());
        values.put(DataBaseHelper.PARK_COLUMN_TIME, park.getTime());
        //Log.i("DBG", "ParkDAO::addPark type " + park.getTime());
        values.put(DataBaseHelper.PARK_COLUMN_VALUE, park.getValue());
        //Log.i("DBG", "ParkDAO::addPark value " + park.getValue());
        values.put(DataBaseHelper.PARK_COLUMN_ID_VACANCY, park.getVacancy().getId());
        //Log.i("DBG", "ParkDAO::addPark VAGA ID: " + park.getVacancy().getId());

        values.put(DataBaseHelper.PARK_COLUMN_ID_VEHICLE, park.getVehicle().getId());
        //Log.i("DBG", "ParkDAO::addPark VEICULO PLACA: " + park.getVehicle().getLicensePlate());
        id = db.insert(DataBaseHelper.TABLE_PARK, null, values);
        db.close();

        return id;
    }

    public ArrayList<Park> getParkListByVacancyId(long vacancyID){
        ArrayList<Park> m_list = new ArrayList<>();
        SQLiteDatabase db = m_helper.getReadableDatabase();

        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_PARK + " WHERE " +
                DataBaseHelper.PARK_COLUMN_ID_VACANCY + " =?";
        String[] arguments = new String[]{ String.valueOf(vacancyID)};

        Cursor cursor = db.rawQuery(sql, arguments);

        while (cursor.moveToNext()){
            Park park = new Park();

            park.setId( cursor.getLong( cursor.getColumnIndex(DataBaseHelper.COLUMN_ID)));
            park.setType( cursor.getInt( cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_TYPE)));
            park.setTime(cursor.getLong( cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_TIME)));
            park.setValue(cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_VALUE)) );

            /** Obtendo veiculo correspondente ao park adjacente! **/
            park.setVehicle( VehicleDAO.getInstance().getVehicleById(
                    cursor.getInt( cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_ID_VEHICLE))
            ) );
            m_list.add(park);
        }
        db.close();
        return m_list;
    }

    public ArrayList<Park> getParkExitListByDate(long today, long yesterday){
        ArrayList<Park> list = new ArrayList<>();
        SQLiteDatabase db = m_helper.getReadableDatabase();

        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_PARK + " WHERE " +
                DataBaseHelper.PARK_COLUMN_TIME + " > ? and " +
                DataBaseHelper.PARK_COLUMN_TIME + " <= ?";

        String[] arguments = new String[]{
                String.valueOf(yesterday),
                String.valueOf(today)};


        Cursor cursor = db.rawQuery(sql, arguments);

        while (cursor.moveToNext()){
            Park park = new Park();

            park.setId( cursor.getLong( cursor.getColumnIndex(DataBaseHelper.COLUMN_ID)));
            park.setType( cursor.getInt( cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_TYPE)));
            park.setTime(cursor.getLong( cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_TIME)));
            park.setValue(cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_VALUE)) );

            park.setVacancy(
                    VacancyDAO.getInstance().getVacancyById(
                            cursor.getLong(cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_ID_VACANCY))
                    ) );

            /** Obtendo veiculo correspondente ao park adjacente! **/
            park.setVehicle( VehicleDAO.getInstance().getVehicleById(
                    cursor.getInt( cursor.getColumnIndex(DataBaseHelper.PARK_COLUMN_ID_VEHICLE))
            ) );

            Log.i("DBG", "PARK_DAO RECOVERY BY TIME: " + park.getId()
            +   " Valor: " + park.getValue());
            list.add(park);
        }
        db.close();
        return list;
    }
}
