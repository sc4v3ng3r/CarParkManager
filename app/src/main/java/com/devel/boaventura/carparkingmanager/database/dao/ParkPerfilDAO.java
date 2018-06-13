package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
import com.devel.boaventura.carparkingmanager.model.ParkPerfil;
import com.devel.boaventura.carparkingmanager.model.Vacancy;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/26/18.
 */

public class ParkPerfilDAO {

    private static ParkPerfilDAO m_instance = null;
    DataBaseHelper m_helper;
    ArrayList<ParkPerfil> m_data;

    public static synchronized ParkPerfilDAO getInstance() {

        if (m_instance == null)
            m_instance = new ParkPerfilDAO();

        return m_instance;
    }

    private ParkPerfilDAO() {
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
        m_data = new ArrayList<>();
    }

    public long addPerfil(ParkPerfil perfil) {
        long id = -1;

        SQLiteDatabase db = m_helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.PERFIL_COLUMN_NAME, perfil.getPerfilName());
        id = db.insert(DataBaseHelper.TABLE_PERFIL, null, values);
        perfil.setId(id);

        /*Adicionando as vagas correspondetes deste perfil!*/
        ArrayList<Vacancy> list = perfil.getVacancyList();
        VacancyDAO dao = VacancyDAO.getInstance();
        for (Vacancy v : list) {
            long idVacancy = dao.addVacancy(v, id);
            v.setId(idVacancy);
            Log.i("DBG", "ADD V_ID: " + idVacancy + " PERFIL ID: " + perfil.getId());
        }
        //Log.i("DBG", "addPerfil() DATABASE OK! " + id);
        db.close();
        return id;

    }

    public ArrayList<ParkPerfil> getPerfilListfromDataBase() {
        SQLiteDatabase db = m_helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_PERFIL;
        ArrayList<ParkPerfil> perfilList = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        Log.i("DBG", db.getPath());
        while (cursor.moveToNext()) {

            ParkPerfil perfil = new ParkPerfil();
            perfil.setId(
                    cursor.getLong(
                            cursor.getColumnIndex(DataBaseHelper.COLUMN_ID))
            );

            perfil.setPerfilName(cursor.getString(
                    cursor.getColumnIndex(DataBaseHelper.PERFIL_COLUMN_NAME)
            ));


            /*
            * Obtendo as vagas do determinado perfil!
            * */
            ArrayList<Vacancy> list = VacancyDAO
                    .getInstance()
                    .getVacancyListByPerfilId(perfil.getId());
            perfil.setVacancyList(list);

            perfilList.add(perfil);
        }

        db.close();
        return perfilList;
    }

    public ArrayList<String> getPerfilNameList() {
        SQLiteDatabase db = m_helper.getReadableDatabase();
        String sql = "SELECT " + DataBaseHelper.PERFIL_COLUMN_NAME + " FROM " + DataBaseHelper.TABLE_PERFIL;
        ArrayList<String> perfilNameList = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            perfilNameList.add(
                    cursor.getString(
                            cursor.getColumnIndex(DataBaseHelper.PERFIL_COLUMN_NAME)
                    )
            );

            Log.i("DBG", "PERFIL NAME LIST SIZE: " + perfilNameList.size());
        }
        db.close();
        return perfilNameList;
    }

    public ParkPerfil getPerfilByName(String name) {
        SQLiteDatabase db = m_helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_PERFIL +
                " WHERE " + DataBaseHelper.PERFIL_COLUMN_NAME + " =?";
        String[] arguments = new String[]{ name };

        Cursor cursor = db.rawQuery(sql, arguments);

        ParkPerfil perfil = new ParkPerfil();

       if (cursor.moveToNext()) {
            /*PEga o perfil*/

           perfil.setId(cursor.getLong(cursor.getColumnIndex(
                   DataBaseHelper.COLUMN_ID)));

           perfil.setPerfilName(cursor.getString(cursor.getColumnIndex(
                   DataBaseHelper.PERFIL_COLUMN_NAME)));

       }

        ArrayList<Vacancy> perfilVacancys = VacancyDAO.getInstance()
                .getVacancyListByPerfilId(perfil.getId());
        Log.i("DBG", "ParkPerfilDao::getPerfilByName VAcancy: " + perfilVacancys.size());
        perfil.setVacancyList(perfilVacancys);


        db.close();
        return perfil;
    }

    public ParkPerfil getPerfilById(long id) {
        SQLiteDatabase db = m_helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_PERFIL +
                " WHERE " + DataBaseHelper.COLUMN_ID + " =?";
        String[] arguments = new String[]{ String.valueOf(id) };

        Cursor cursor = db.rawQuery(sql, arguments);

        ParkPerfil perfil = new ParkPerfil();

        if (cursor.moveToNext()) {
            /*PEga o perfil*/

            perfil.setId(cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID)));

            perfil.setPerfilName(cursor.getString(cursor.getColumnIndex(
                    DataBaseHelper.PERFIL_COLUMN_NAME)));

        }

        ArrayList<Vacancy> perfilVacancys = VacancyDAO.getInstance()
                .getVacancyListByPerfilId(perfil.getId());
        Log.i("DBG", "ParkPerfilDao::getPerfilByName VAcancy: " + perfilVacancys.size());
        perfil.setVacancyList(perfilVacancys);

        db.close();
        return perfil;
    }
}
