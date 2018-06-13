package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
import com.devel.boaventura.carparkingmanager.model.ParkPerfil;
import com.devel.boaventura.carparkingmanager.model.Tax;
import com.devel.boaventura.carparkingmanager.model.Vacancy;

/**
 * Created by scavenger on 4/29/18.
 */

public class SettingsDAO {
    private static SettingsDAO m_instace = null;
    private SQLiteOpenHelper m_helper;

    private SettingsDAO(){
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    public static final synchronized SettingsDAO getInstance(){
        if (m_instace == null)
            m_instace = new SettingsDAO();
        return m_instace;
    }

    public ParkPerfil getLastPerfil(long id) {
        return ParkPerfilDAO.getInstance().getPerfilById(id);
    }

    public void updateLastPerfil(ParkPerfil perfil){
        SQLiteDatabase db = m_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String where = DataBaseHelper.COLUMN_ID + " =? ";
        String[] args = new String[]{ String.valueOf(ParkSettingsManager.ID) };

        values.put(DataBaseHelper.APP_SETTINGS_PERFIL_ID, perfil.getId());
        int rws = db.update(DataBaseHelper.TABLE_APP_SETTINGS, values, where, args);
        Log.i("DBG", "UPDATE LAST USED PERFIL " + rws);
        db.close();
    }

    public void updateLastTax(Tax tax){
        SQLiteDatabase db = m_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String where = DataBaseHelper.COLUMN_ID + " =? ";
        String[] args = new String[]{ String.valueOf(ParkSettingsManager.ID) };

        values.put(DataBaseHelper.APP_SETTINGS_TAX_ID, tax.getId());
        int rws = db.update(DataBaseHelper.TABLE_APP_SETTINGS, values, where, args);
        Log.i("DBG", "UPDATE LAST USED TAX " + rws);
        db.close();

    }

    public long getLastPerfilId(){
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_APP_SETTINGS;
        SQLiteDatabase db = m_helper.getReadableDatabase();
        long last_perfilId = -1;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            last_perfilId = cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.APP_SETTINGS_PERFIL_ID
            ));
        }

        db.close();
        return last_perfilId;
    }

    public long getLastTaxId(){
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_APP_SETTINGS;
        SQLiteDatabase db = m_helper.getReadableDatabase();
        long lastTaxId = -1;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()){
            lastTaxId = cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.APP_SETTINGS_TAX_ID
            ));
        }

        db.close();
        return lastTaxId;
    }

    public void createEntry(){

        SQLiteDatabase db = m_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_ID, ParkSettingsManager.ID);
        //values.put(DataBaseHelper.APP_SETTINGS_PERFIL_ID,  ParkSettingsManager.ID);
        db.insert(DataBaseHelper.TABLE_APP_SETTINGS, null, values);
        Log.i("DBG", "DATABASE ENTRY CREATED!");
        db.close();
    }

    public ParkPerfil createDefaultPerfilEntry(){
        ParkPerfil perfil = new ParkPerfil();
        perfil.setPerfilName("PERFIL DEFAULT");

        for(int i=1; i <= 30; i++){
            Vacancy v = new Vacancy(String.valueOf(i),
                    Vacancy.TYPE_ROTARY, Vacancy.STATUS_AVAILABLE );

            v.setClient(null);
            perfil.addVacancy(v);
        }

        perfil.setId( ParkPerfilDAO.getInstance().addPerfil(perfil) );
        return perfil;
        //ParkSettingsManager.getInstance().setPerfil(perfil);
    }
    public Tax createDefaultTaxesEntry(){

        Tax taxDefault = new Tax();
        taxDefault.setName("TAXA DEFAULT");
        taxDefault.setUntil1H(3.50);
        taxDefault.setFrom1hTo4h(3.00);
        taxDefault.setFrom4hTo8h(2.50);
        taxDefault.setAfter8h(36);

        taxDefault.setId( TaxDAO.getInstance().addTax(taxDefault));
        Log.i("", "");
        //ParkSettingsManager.getInstance().setTax(taxDefault);
        return taxDefault;
    }

}
