package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.database.dao.ClientDAO;
import com.devel.boaventura.carparkingmanager.model.Client;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/29/18.
 */

public class RentingClientSelectorDialog extends DialogFragment {

    private ArrayList<Client> m_clientList;
    public static final String FRAGMENT_TAG = "RentingClientSelectorDialog";
    private RentingClientClickListener m_listener;
    private Client m_selectedClient = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        m_clientList = ClientDAO.getInstance().getClientsNameAndId();

        String[] names = new String[m_clientList.size()];

        for(int i=0; i < names.length; i++){
            names[i] = m_clientList.get(i).getName();
        }

        builder.setSingleChoiceItems(names, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                m_selectedClient = m_clientList.get(position);
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (m_listener != null)
                    m_listener.onRentingClientConfirmClick(m_selectedClient);

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (m_listener !=null)
                    m_listener.onRentingClientCancelClick();
            }
        }).setTitle("Selecionar Cliente");

        return builder.create();
    }


    public void setRentingClientClickListener(RentingClientClickListener listener){
        m_listener = listener;
    }

    public interface RentingClientClickListener{
        public void onRentingClientConfirmClick(/*long id*/ Client client);
        public void onRentingClientCancelClick();
    }
}
