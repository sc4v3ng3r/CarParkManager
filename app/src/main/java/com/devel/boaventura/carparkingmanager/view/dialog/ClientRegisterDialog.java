package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.database.dao.ClientDAO;
import com.devel.boaventura.carparkingmanager.model.Client;

/**
 * Created by scavenger on 4/28/18.
 */

public class ClientRegisterDialog extends DialogFragment {

    private EditText m_clientNameField;
    public static final String FRAGMENT_TAG = "ClientRegisterDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = inflater.inflate(R.layout.dialog_client_register_layout, null);

        m_clientNameField = (EditText) view.findViewById(R.id.ClientRegisterDialogClientName);

        builder.setView(view)
                .setTitle(getString(R.string.client_register_dialog_title))
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name= m_clientNameField.getText().toString();

                        if (!name.isEmpty()){
                            Client client = new Client(name);
                            ClientDAO.getInstance().addClient(client);
                            Toast.makeText(getActivity(), R.string.client_added,Toast.LENGTH_SHORT)
                                    .show();
                        } else{
                            Toast.makeText(getActivity(), R.string.client_not_added,Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }
}
