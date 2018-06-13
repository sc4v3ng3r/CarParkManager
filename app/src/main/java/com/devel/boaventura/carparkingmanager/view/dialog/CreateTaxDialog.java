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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.database.dao.TaxDAO;
import com.devel.boaventura.carparkingmanager.model.Tax;

/**
 * Created by scavenger on 4/30/18.
 */

public class CreateTaxDialog extends DialogFragment{

    private EditText m_name, m_until1H, m_from1hTo4h,
        m_from4hTo8h, m_after8h;
    private CheckBox m_check;

    private CreateTaxDialogOnClickListener m_listener;
    public static final String FRAGMENT_TAG="CreateTaxDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater infalter = getActivity().getLayoutInflater();

        View layout = infalter.inflate(R.layout.dialog_create_tax_layout, null);

        m_name = (EditText) layout.findViewById(R.id.CreateTaxDialogNameEditText);
        m_until1H = (EditText) layout.findViewById(R.id.CreateTaxDialogUntil1h);
        m_from1hTo4h = (EditText) layout.findViewById(R.id.CreateTaxDialogFrom1To4h);
        m_from4hTo8h = (EditText) layout.findViewById(R.id.CreateTaxDialogFrom4hTo8h);
        m_after8h = (EditText) layout.findViewById(R.id.CreateTaxDialogAfter8h);
        m_check = (CheckBox) layout.findViewById(R.id.CreateTaxDialogCheckBox);

        builder.setView(layout)
                .setTitle("Criar Perfil de Taxas")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tax tax = createTax();
                        if (m_listener!=null){
                            m_listener.CreateTaxDialogConfirmClick( tax );

                        }

                        else {
                            long id = TaxDAO.getInstance().addTax(tax);
                            if (id != -1){
                                tax.setId(id);
                                Toast.makeText(getActivity(),"Taxa criada",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }

                        if( m_check.isChecked())
                            ParkSettingsManager.getInstance().setTax( tax );

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    private Tax createTax(){
        Tax tax = new Tax();

        tax.setName( m_name.getText().toString());
        tax.setUntil1H( Double.parseDouble( m_until1H.getText().toString()));
        tax.setFrom1hTo4h( Double.parseDouble( m_from1hTo4h.getText().toString()) );
        tax.setFrom4hTo8h( Double.parseDouble( m_from4hTo8h.getText().toString()) );
        tax.setAfter8h( Double.parseDouble( m_after8h.getText().toString() ));

        return tax;
    }

    public void setCreateTaxDialogOnClickListener(CreateTaxDialogOnClickListener listener){
        m_listener = listener;
    }

    public interface CreateTaxDialogOnClickListener{
        void CreateTaxDialogConfirmClick(Tax tax);
        void CreateTaxDialogCancelClick();
    }
}
