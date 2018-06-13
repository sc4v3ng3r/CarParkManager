package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
import com.devel.boaventura.carparkingmanager.model.Client;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.Vacancy;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/27/18.
 */

public class VacancyDAO {
    private static VacancyDAO m_instance = null;
    private DataBaseHelper m_helper;

    private VacancyDAO(){
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    public static synchronized VacancyDAO getInstance(){
        if (m_instance == null)
            m_instance = new VacancyDAO();

        return m_instance;
    }

    public long addVacancy(Vacancy vacancy, long perfilId){
        long id = -1;
        SQLiteDatabase db = m_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.VACANCY_COLUMN_NUMBER, vacancy.getNumber());
        values.put(DataBaseHelper.VACANCY_COLUMN_TYPE, vacancy.getType());
        values.put(DataBaseHelper.VACANCY_COLUMN_ID_PERFIL, perfilId);
        values.put(DataBaseHelper.VACANCY_COLUMN_STATE, vacancy.getStatus());
        Client c = vacancy.getClient();
        if (c != null)
        values.put(DataBaseHelper.VACANCY_COLUMN_ID_CLIENT, c.getId());

        /*FALTA O CLIENT ID!
        * caso seja privada!
        * */

        /*FALTA OS SERVICOS*/


        id = db.insert(DataBaseHelper.TABLE_VACANCY, null, values);
        vacancy.setId(id);
        db.close();
        return id;
    }

    public ArrayList<Vacancy> getVacancyListByPerfilId(long perfilId){

        ArrayList<Vacancy> vacancyList = new ArrayList<>();
        SQLiteDatabase db = m_helper.getReadableDatabase();

        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_VACANCY +
                " WHERE " + DataBaseHelper.VACANCY_COLUMN_ID_PERFIL + " =?";

        String[] arguments = new String[]{ String.valueOf(perfilId) };

        Cursor cursor = db.rawQuery(sql, arguments);

        //Log.i("DBG", "VacancyDAO:: getVacancyListByPerfilId ROWS: " + cursor.getCount());

        while (cursor.moveToNext()){
            Vacancy v = new Vacancy();

            long id = cursor.getLong( cursor.getColumnIndex( DataBaseHelper.COLUMN_ID ) );
            ArrayList<Park> serviceList = ParkDAO.getInstance().getParkListByVacancyId(id);

            v.setId( id );
            v.setServiceList(serviceList);
            v.setNumber( cursor.getString( cursor.getColumnIndex( DataBaseHelper.VACANCY_COLUMN_NUMBER)));
            //Log.i("DBG", "VacancyDAO::getVacancyListByPerfilId() VAGA: " + v.getNumber() + " TOTAL PARK's: " + serviceList.size());
            v.setType(cursor.getInt( cursor.getColumnIndex( DataBaseHelper.VACANCY_COLUMN_TYPE) ) );
            v.setStatus(cursor.getInt( cursor.getColumnIndex( DataBaseHelper.VACANCY_COLUMN_STATE )) );
            v.setClient( ClientDAO.getInstance().getClientById(
                    cursor.getLong( cursor.getColumnIndex(DataBaseHelper.RENTING_COLUMN_ID_CLIENT))
            ));

            vacancyList.add(v);
        }
        return vacancyList;

    }

    public void update(Vacancy vacancy){
        SQLiteDatabase db =m_helper.getWritableDatabase();
        String where = DataBaseHelper.COLUMN_ID + " =?";

        String[] arguments = new String[]{ String.valueOf( vacancy.getId() )};

        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.VACANCY_COLUMN_STATE, vacancy.getStatus());
        values.put(DataBaseHelper.VACANCY_COLUMN_TYPE, vacancy.getType() );
        values.put(DataBaseHelper.VACANCY_COLUMN_NUMBER, vacancy.getNumber() );
        Client c = vacancy.getClient();
        if (c!= null)
        values.put(DataBaseHelper.VACANCY_COLUMN_ID_CLIENT, c.getId());

        if ( (db.update(DataBaseHelper.TABLE_VACANCY, values, where, arguments) ) > 0 )
            Log.i("DBG", "VacancyDAO::update() OK!");

        db.close();
    }

    public Vacancy getVacancyById(long id){
        Vacancy vacancy = null;
        SQLiteDatabase db = m_helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_VACANCY + " WHERE "
                + DataBaseHelper.COLUMN_ID + " =?";

        String[] arguments = new String[]{ String.valueOf(id)};

        Cursor cursor = db.rawQuery(sql, arguments);

        if (cursor.moveToNext()){
            vacancy = new Vacancy();
            vacancy.setId( cursor.getLong( cursor.getColumnIndex( DataBaseHelper.COLUMN_ID ) ) );
            ArrayList<Park> serviceList = ParkDAO.getInstance().getParkListByVacancyId(id);

            vacancy.setId( id );
            vacancy.setServiceList(serviceList);
            vacancy.setNumber( cursor.getString( cursor.getColumnIndex( DataBaseHelper.VACANCY_COLUMN_NUMBER)));
            //Log.i("DBG", "VacancyDAO::getVacancyListByPerfilId() VAGA: " + vacancy.getNumber() + " TOTAL PARK's: " + serviceList.size());
            vacancy.setType(cursor.getInt( cursor.getColumnIndex( DataBaseHelper.VACANCY_COLUMN_TYPE) ) );
            vacancy.setStatus(cursor.getInt( cursor.getColumnIndex( DataBaseHelper.VACANCY_COLUMN_STATE )) );
            vacancy.setClient( ClientDAO.getInstance().getClientById(
                    cursor.getLong( cursor.getColumnIndex(DataBaseHelper.RENTING_COLUMN_ID_CLIENT))
            ));
        }

        db.close();
        return vacancy;

    }
}