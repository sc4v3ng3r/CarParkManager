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
import com.devel.boaventura.carparkingmanager.database.dao.ClientDAO;
import com.devel.boaventura.carparkingmanager.database.dao.ParkDAO;
import com.devel.boaventura.carparkingmanager.database.dao.RentingDAO;
import com.devel.boaventura.carparkingmanager.model.Client;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.Renting;
import com.devel.boaventura.carparkingmanager.model.Vacancy;

import java.util.ArrayList;

/**
 * Created by scavenger on 4/30/18.
 */

public class DialogVacancyDetails extends DialogFragment {
    private TextView m_vacancyNumber, m_vacancyType, m_vacancyOwner,
        m_vacancyState, m_vacancyTotal, m_vacancyTotalRotary, m_vacancyTotalPrivate;

    public static final String PARAM = "VACANCY";
    public static final String FRAGMENT_TAG = "DialogVacancyDetails";
    private Vacancy m_currentVacancy;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_vacancy_details_layout, null);

        m_vacancyNumber = (TextView) view.findViewById(R.id.DialogVacancyDetailsVacancy);
        m_vacancyType = (TextView) view.findViewById(R.id.DialogVacancyDetailsVacancyType);
        m_vacancyOwner = (TextView) view.findViewById(R.id.DialogVacancyDetailsOwner);
        m_vacancyState = (TextView) view.findViewById(R.id.DialogVacancyDetailsState);
        m_vacancyTotal = (TextView) view.findViewById(R.id.DialogVacancyDetailsTotal);
        m_vacancyTotalPrivate = (TextView) view.findViewById(R.id.DialogVacancyDetailsTotalMonthly);
        m_vacancyTotalRotary = (TextView) view.findViewById(R.id.DialogVacancyDetailsTotalRotary);

        m_currentVacancy = getArguments().getParcelable(PARAM);

        /* faz as consultas e seta os valores*/
        ArrayList<Renting> rentings = RentingDAO.getInstance()
                .getRentingByVacancyId(m_currentVacancy.getId());
        double totalRenting = rentingsSum(rentings);
        m_vacancyTotalPrivate.setText(getString(R.string.private_total) + " " + totalRenting + " R$");

        Client owner = null;

        if (!rentings.isEmpty())
           owner = rentings.get(0).getClient();

        ArrayList<Park> parks = ParkDAO.getInstance()
                .getParkListByVacancyId(m_currentVacancy.getId());
        double totalRotary = rotarySum(parks);
        m_vacancyTotalRotary.setText(getString(R.string.rotary_total) + " " + totalRotary + " R$");

        double total = totalRotary + totalRenting;

        m_vacancyNumber.setText(getString(R.string.vacancy) + " " + m_currentVacancy.getNumber());
        m_vacancyType.setText(
                m_currentVacancy.getType() == Vacancy.TYPE_PRIVATE ? getString(R.string._private)
        : getString(R.string.rotary));

        if (m_currentVacancy.getStatus() == Vacancy.STATUS_AVAILABLE){
            m_vacancyState.setText(getString(R.string.state) + " " + getString(R.string.available));

        }else{
            m_vacancyState.setText(getString(R.string.state) + " " + getString(R.string.unavailable));
        }

        if (m_currentVacancy.getType() == Vacancy.TYPE_PRIVATE){
            m_vacancyOwner.setText(getString(R.string.owner) + ": " + owner.getName());
        } else{
            m_vacancyOwner.setText( getString(R.string.owner) + ": " + "----");
        }

        m_vacancyTotal.setText("LUCRO TOTAL: " + total + " R$" );

        builder.setTitle("Vaga Detalhes")
                .setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    private double rentingsSum(ArrayList<Renting> list){
        double value = 0;

        for(Renting r: list){
            value+= r.getValue();
        }

        return value;
    }

    private double rotarySum(ArrayList<Park> list){
        double total = 0;

        if (list.isEmpty())
            return total;

        for(Park p: list){
            if (p.getType() == Park.SERVICE_EXIT)
                total+= p.getValue();
        }
        return total;
    }
}


