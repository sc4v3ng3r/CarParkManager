package com.devel.boaventura.carparkingmanager.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devel.boaventura.carparkingmanager.database.DataBaseHelper;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;
import com.devel.boaventura.carparkingmanager.model.Client;
import com.devel.boaventura.carparkingmanager.model.Renting;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/27/18.
 */

public class ClientDAO {
    private static ClientDAO m_instance = null;
    private DataBaseHelper m_helper = null;

    public static final synchronized ClientDAO getInstance(){

        if (m_instance == null)
            m_instance = new ClientDAO();

        return m_instance;
    }

    private ClientDAO(){
        m_helper = DataBaseManager.getInstance().getDataBaseHelper();
    }

    public long addClient(Client c){
        long id = -1;
        SQLiteDatabase db = m_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.CLIENT_COLUMN_NAME, c.getName());

        id = db.insert(DataBaseHelper.TABLE_CLIENT, null, values);

        db.close();
        return id;

    }

    public ArrayList<Client> getClientList(){
        SQLiteDatabase db = m_helper.getWritableDatabase();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_CLIENT;
        ArrayList<Client> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql,null);

        while (cursor.moveToNext()){
            Client client = new Client(
                    cursor.getString( cursor.getColumnIndex(
                            DataBaseHelper.CLIENT_COLUMN_NAME
                    ))
            );

            long id = cursor.getLong(
                    cursor.getColumnIndex(DataBaseHelper.COLUMN_ID) );
            client.setId(id);

            ArrayList<Renting> rentingList = RentingDAO
                    .getInstance()
                    .getRentingByClientId(id);
            /*
            for(Renting r: rentingList)
                r.setClient(client);

            client.setRentList(rentingList);
            list.add(client);*/
        }
        db.close();
        return list;
    }

    public ArrayList<Client> getClientsNameAndId(){
        SQLiteDatabase db = m_helper.getWritableDatabase();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_CLIENT;
        ArrayList<Client> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql,null);

        while (cursor.moveToNext()){
            Client client = new Client(
                    cursor.getString( cursor.getColumnIndex(
                            DataBaseHelper.CLIENT_COLUMN_NAME
                    ))
            );

            long id = cursor.getLong(
                    cursor.getColumnIndex(DataBaseHelper.COLUMN_ID) );

            client.setId(id);

            list.add(client);
        }
        db.close();
        return list;
    }

    public Client getClientById(long clientId){
        Client client = null;
        SQLiteDatabase db = m_helper.getWritableDatabase();
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_CLIENT
                + " WHERE " + DataBaseHelper.COLUMN_ID + " =?";

        String[] arguments = new String[]{String.valueOf(clientId)};

        Cursor cursor = db.rawQuery(sql, arguments);
        if (cursor.moveToNext()){
            client = new Client(
                    cursor.getString( cursor.getColumnIndex(
                            DataBaseHelper.CLIENT_COLUMN_NAME
                    ))
            );

            client.setId( cursor.getLong(
                    cursor.getColumnIndex(DataBaseHelper.COLUMN_ID)
            ));
            /*
            ArrayList<Renting> rentingList = RentingDAO
                    .getInstance()
                    .getRentingByClientId(clientId);

            for(Renting r: rentingList)
                r.setClient(client);

            client.setRentList(rentingList)*/;
        }
        return client;
    }

}
