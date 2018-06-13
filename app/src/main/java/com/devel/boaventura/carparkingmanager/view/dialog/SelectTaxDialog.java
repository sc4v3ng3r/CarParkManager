package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.database.dao.TaxDAO;
import com.devel.boaventura.carparkingmanager.model.Tax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scavenger on 4/30/18.
 */

public class SelectTaxDialog extends DialogFragment
        implements AdapterView.OnItemSelectedListener {

    private Spinner m_taxesSpinner;
    private List<String> m_taxNames = new ArrayList<>();
    private ArrayList<Tax> m_taxesList;
    private ArrayAdapter<String> m_adapter;
    private Tax m_selectedTax = null;
    private SelectTaxDialogClickListener m_listener;

    public static final String FRAGMENT_TAG = "SelectTaxDialog";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        m_taxesList = TaxDAO.getInstance().getTaxes();

        View layout = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_select_tax_layout, null);

        m_taxesSpinner = (Spinner) layout.findViewById(R.id.SelectTaxDialogSpinner);

        builder.setView(layout)
                .setTitle("Selecionar Perfil de Taxa")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*if (m_listener != null){
                            m_listener.selectTaxDialogConfirmClicked(m_selectedTax);
                        }*/

                        ParkSettingsManager.getInstance().setTax(m_selectedTax);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        setNamesSpiner();
        return builder.create();
    }

    private void setNamesSpiner(){

        for(Tax t: m_taxesList){
            m_taxNames.add( t.getName() );
        }

        m_adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item,
                m_taxNames
        );

        m_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_taxesSpinner.setAdapter(m_adapter);
        m_taxesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        m_selectedTax = m_taxesList.get(position);
        /*Log.i("DBG", "TAXA SELECIONADA "
                + m_selectedTax.getName() + " ID: " + m_selectedTax.getId());
        */
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSelectTaxDialogListener(SelectTaxDialogClickListener listener){
        m_listener = listener;
    }

    public interface SelectTaxDialogClickListener{
        void selectTaxDialogConfirmClicked(Tax tax);
        void selectTaxDialogCancelClicked();
    }
}
