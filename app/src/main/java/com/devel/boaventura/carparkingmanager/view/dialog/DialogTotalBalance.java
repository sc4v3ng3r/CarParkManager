package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.database.dao.ParkDAO;
import com.devel.boaventura.carparkingmanager.database.dao.RentingDAO;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.ParkPerfil;
import com.devel.boaventura.carparkingmanager.model.Renting;
import com.devel.boaventura.carparkingmanager.model.Vacancy;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/30/18.
 */

public class DialogTotalBalance extends DialogFragment {

    public static final String FRAGMENT_TAG = "DialogTotalBalance";
    private ParkPerfil m_currentPerfil;

    private TextView m_totalRotary, m_totalPrivate, m_totalGeneral;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        m_currentPerfil = ParkSettingsManager.getInstance().getPerfil();


        View layout = getActivity().getLayoutInflater().inflate(
                R.layout.dialog_total_balance_layout, null);

        m_totalPrivate = (TextView) layout.findViewById(R.id.DialogTotalBalanceTotalPrivate);
        m_totalRotary = (TextView) layout.findViewById(R.id.DialogTotalBalanceTotalRotary);
        m_totalGeneral = (TextView) layout.findViewById(R.id.DialogTotalBalanceMainTotal);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(layout)
                .setTitle("Balanço financeiro")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        double totalPrivate = getTotalPrivate();
        double totalRotary = getTotalRotary();

        m_totalPrivate.setText(getString(R.string.private_total) + " " + totalPrivate + " R$");
        m_totalRotary.setText(getString(R.string.rotary_total) + " " + totalRotary + " R$");
        m_totalGeneral.setText(getString(R.string.total_general) + " "
                + (totalPrivate + totalRotary) + " R$");

        return builder.create();
    }

    private double getTotalPrivate(){
        double total = 0;
        ArrayList<Renting> rentings;
        ArrayList<Vacancy> allVacancyes = m_currentPerfil.getVacancyList();

        for(Vacancy v: allVacancyes){
            if (v.getType() == Vacancy.TYPE_PRIVATE){
                rentings = RentingDAO.getInstance().getRentingByVacancyId(v.getId());
                for(Renting r: rentings){
                    total+= r.getValue();
                }
            }
        }
        return total;
    }

    private double getTotalRotary(){
        double total = 0;
        ArrayList<Vacancy> allVacancyes = m_currentPerfil.getVacancyList();
        for(Vacancy vacancy: allVacancyes){
            ArrayList<Park> parks = ParkDAO.getInstance().getParkListByVacancyId(vacancy.getId());
            for(Park p: parks){
                total+= p.getValue();
            }

        }

        return total;
    }
}
