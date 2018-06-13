package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
import com.devel.boaventura.carparkingmanager.model.Renting;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/28/18.
 */

public class RentingDAO {
    private static RentingDAO m_instance = null;
    private SQLiteOpenHelper m_helper;

    private RentingDAO(){
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    public static final synchronized RentingDAO getInstance(){
        if (m_instance == null)
            m_instance = new RentingDAO();
        return m_instance;
    }

    public long addRenting(Renting renting){
        long id = -1;
        SQLiteDatabase db = m_helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.RENTING_COLUMN_ID_CLIENT, renting.getClient().getId());
        values.put(DataBaseHelper.RENTING_COLUMN_ID_VACANCY, renting.getVacancy().getId() );
        values.put(DataBaseHelper.RENTING_COLUMN_START_TIME, renting.getStartTime() );
        values.put(DataBaseHelper.RENTING_COLUMN_END_TIME, renting.getEndTime() );
        values.put(DataBaseHelper.RENTING_COLUMN_STATE, renting.getState() );
        values.put(DataBaseHelper.RENTING_COLUMN_IS_PAID, renting.isPaid() );
        values.put(DataBaseHelper.RENTING_COLUMN_VALUE, renting.getValue() );

        id = db.insert(DataBaseHelper.TABLE_RENTING, null, values);
        db.close();
        return id;

    }

    public Renting getRentingById(long id){
        Renting renting = null;
        SQLiteDatabase db = m_helper.getReadableDatabase();

        ArrayList<Renting> rentingList = new ArrayList<>();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_RENTING + " WHERE "
                + DataBaseHelper.COLUMN_ID + " =?";
        String[] arguments = new String[]{ String.valueOf(id)};

        Cursor cursor = db.rawQuery(sql, arguments);

        if (cursor.moveToNext()){
            renting = new Renting();

            renting.setId( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID)));

            renting.setState( cursor.getInt( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_STATE
            )));

            renting.setStartTime( cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_START_TIME
            )));

            renting.setEndTime( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_END_TIME
            )));

            renting.setPayment( cursor.getInt(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_IS_PAID
            )));


            renting.setValue( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_VALUE
            )));
            /*
            renting.setClient( ClientDAO.getInstance().getClientById(
                    cursor.getLong(cursor.getColumnIndex(
                            DataBaseHelper.RENTING_COLUMN_ID_CLIENT
                    ))
            ));*/

            renting.setVacancy( VacancyDAO.getInstance().getVacancyById(
                    cursor.getLong( cursor.getColumnIndex(
                            DataBaseHelper.RENTING_COLUMN_ID_VACANCY
                    )) ) );

        }

        db.close();
        return renting;
    }

    public Renting getRentingByVacancyIdandState(long vacancyid, int state){
        Renting renting = null;

        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_RENTING + " WHERE " +
                DataBaseHelper.RENTING_COLUMN_ID_VACANCY + " =? " + "AND " +
                DataBaseHelper.RENTING_COLUMN_STATE + " =?";

        String[] arguments = new String[]{ String.valueOf( vacancyid),
                String.valueOf(state) };

        SQLiteDatabase db = m_helper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, arguments);

        if (cursor.moveToNext()){
            renting = new Renting();
            renting.setId( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID)));

            renting.setState( cursor.getInt( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_STATE
            )));

            renting.setStartTime( cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_START_TIME
            )));

            renting.setEndTime( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_END_TIME
            )));

            renting.setPayment( cursor.getInt(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_IS_PAID
            )));


            renting.setValue( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_VALUE
            )));

            renting.setVacancy( VacancyDAO.getInstance().getVacancyById(
                    cursor.getLong( cursor.getColumnIndex(
                            DataBaseHelper.RENTING_COLUMN_ID_VACANCY
                    )) ) );
        }
        return renting;
    }

    public ArrayList<Renting> getRentingByClientId(long clientId){
        SQLiteDatabase db = m_helper.getReadableDatabase();

        ArrayList<Renting> rentingList = new ArrayList<>();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_RENTING + " WHERE "
                + DataBaseHelper.RENTING_COLUMN_ID_CLIENT + " =?";
        String[] arguments = new String[]{ String.valueOf(clientId)};

        Cursor cursor = db.rawQuery(sql, arguments);

        if (cursor.moveToNext()){
            Renting renting = new Renting();

            renting.setId( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID)));

            renting.setState( cursor.getInt( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_STATE
            )));

            renting.setStartTime( cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_START_TIME
            )));

            renting.setEndTime( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_END_TIME
            )));

            renting.setPayment( cursor.getInt(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_IS_PAID
            )));


            renting.setValue( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_VALUE
            )));

            renting.setClient( ClientDAO.getInstance().getClientById( clientId ) );


            renting.setVacancy( VacancyDAO.getInstance().getVacancyById(
                    cursor.getLong( cursor.getColumnIndex(
                            DataBaseHelper.RENTING_COLUMN_ID_VACANCY
                    )) ) );

            rentingList.add(renting);
        }
        db.close();
        return rentingList;
    }

    public ArrayList<Renting> getRentingByVacancyId(long vacancyId){
        Renting renting = null;
        SQLiteDatabase db = m_helper.getReadableDatabase();

        ArrayList<Renting> rentingList = new ArrayList<>();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_RENTING + " WHERE "
                + DataBaseHelper.RENTING_COLUMN_ID_VACANCY + " =?";
        String[] arguments = new String[]{ String.valueOf(vacancyId)};

        Cursor cursor = db.rawQuery(sql, arguments);

        while (cursor.moveToNext()){
            renting = new Renting();

            renting.setId( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID)));

            renting.setState( cursor.getInt( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_STATE
            )));

            renting.setStartTime( cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_START_TIME
            )));

            renting.setEndTime( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_END_TIME
            )));

            renting.setPayment( cursor.getInt(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_IS_PAID
            )));


            renting.setValue( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_VALUE
            )));

            renting.setClient( ClientDAO.getInstance().getClientById(
                    cursor.getLong(cursor.getColumnIndex(
                            DataBaseHelper.RENTING_COLUMN_ID_CLIENT
                    ))
            ));

            renting.setVacancy( VacancyDAO.getInstance().getVacancyById(vacancyId) );
            rentingList.add(renting);
        }

        db.close();

        return rentingList;
    }

    public boolean update(Renting renting){
        SQLiteDatabase db = m_helper.getWritableDatabase();
        String where = DataBaseHelper.COLUMN_ID + " =?";
        String[] arg = new String[]{ String.valueOf(renting.getId())};

        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.RENTING_COLUMN_ID_CLIENT, renting.getClient().getId());
        values.put(DataBaseHelper.RENTING_COLUMN_ID_VACANCY, renting.getVacancy().getId() );
        values.put(DataBaseHelper.RENTING_COLUMN_START_TIME, renting.getStartTime() );
        values.put(DataBaseHelper.RENTING_COLUMN_END_TIME, renting.getEndTime() );
        values.put(DataBaseHelper.RENTING_COLUMN_STATE, renting.getState() );
        values.put(DataBaseHelper.RENTING_COLUMN_IS_PAID, renting.isPaid() );
        values.put(DataBaseHelper.RENTING_COLUMN_VALUE, renting.getValue() );

        return db.update(DataBaseHelper.TABLE_RENTING, values,
                where, arg) > 0 ? true : false;
    }

    public ArrayList<Renting> getRentingListBetweenTime(long today, long yesterday){
        ArrayList<Renting> list = new ArrayList<>();
        Renting renting = null;
        SQLiteDatabase db = m_helper.getReadableDatabase();

        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_RENTING + " WHERE "
                + DataBaseHelper.RENTING_COLUMN_START_TIME + " > ? and " +
                  DataBaseHelper.RENTING_COLUMN_START_TIME + " <= ?";

        String[] arguments = new String[]{ String.valueOf(yesterday), String.valueOf(today)};

        Cursor cursor = db.rawQuery(sql, arguments);

        while (cursor.moveToNext()){
            renting = new Renting();

            renting.setId( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID)));

            renting.setState( cursor.getInt( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_STATE
            )));

            renting.setStartTime( cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_START_TIME
            )));

            renting.setEndTime( cursor.getLong( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_END_TIME
            )));

            renting.setPayment( cursor.getInt(cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_IS_PAID
            )));


            renting.setValue( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.RENTING_COLUMN_VALUE
            )));
            /*
            renting.setClient( ClientDAO.getInstance().getClientById(
                    cursor.getLong(cursor.getColumnIndex(
                            DataBaseHelper.RENTING_COLUMN_ID_CLIENT
                    ))
            ));
            */
            //renting.setVacancy( VacancyDAO.getInstance().getVacancyById(vacancyId) );
            list.add(renting);
        }

        db.close();
        return list;
    }
}
