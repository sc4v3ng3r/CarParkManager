package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
import com.devel.boaventura.carparkingmanager.model.Tax;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/30/18.
 */

public class TaxDAO {
    private static TaxDAO m_instance = null;
    private SQLiteOpenHelper m_helper;

    private TaxDAO(){
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    public static final synchronized TaxDAO getInstance(){
        if (m_instance == null)
            m_instance = new TaxDAO();
        return m_instance;
    }


    public long addTax(Tax tax){
        SQLiteDatabase db = m_helper.getWritableDatabase();
        long id = -1;
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.TAX_COLUMN_NAME, tax.getName());
        values.put(DataBaseHelper.TAX_COLUMN_UNTIL_1H, tax.getUntil1H());
        values.put(DataBaseHelper.TAX_COLUMN_FROM_1H_TO_4H, tax.getFrom1hTo4h());
        values.put(DataBaseHelper.TAX_COLUMN_FROM_4H_TO_8H, tax.getFrom4hTo8h());
        values.put(DataBaseHelper.TAX_COLUMN_AFTER_8H, tax.getAfter8h());

        id = db.insert(DataBaseHelper.TABLE_TAX, null, values);
        db.close();
        return id;
    }

    public Tax getTax(long id){
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_TAX + " WHERE " +
                DataBaseHelper.COLUMN_ID + " =?";
        String[] args = new String[]{ String.valueOf(id) };
        Tax tax = null;
        SQLiteDatabase db = m_helper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, args);

        if (cursor.moveToNext()){
            tax = new Tax();
            tax.setId( cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID
            )));

            tax.setName( cursor.getString( cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_NAME
            )));

            tax.setUntil1H( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_UNTIL_1H
            )));

            tax.setFrom1hTo4h( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_FROM_1H_TO_4H
            )));

            tax.setFrom4hTo8h( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_FROM_4H_TO_8H
            )));

            tax.setAfter8h( cursor.getDouble(cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_AFTER_8H
            )));
        }

        db.close();
        return tax;

    }

    public ArrayList<Tax> getTaxes(){
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_TAX;
        Tax tax = null;
        ArrayList<Tax> list = new ArrayList<>();
        SQLiteDatabase db = m_helper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()){
            tax = new Tax();
            tax.setId( cursor.getLong(cursor.getColumnIndex(
                    DataBaseHelper.COLUMN_ID
            )));

            tax.setName( cursor.getString( cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_NAME
            )));

            tax.setUntil1H( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_UNTIL_1H
            )));

            tax.setFrom1hTo4h( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_FROM_1H_TO_4H
            )));

            tax.setFrom4hTo8h( cursor.getDouble( cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_FROM_4H_TO_8H
            )));

            tax.setAfter8h( cursor.getDouble(cursor.getColumnIndex(
                    DataBaseHelper.TAX_COLUMN_AFTER_8H
            )));

            list.add(tax);
        }

        db.close();
        return  list;

    }
}
