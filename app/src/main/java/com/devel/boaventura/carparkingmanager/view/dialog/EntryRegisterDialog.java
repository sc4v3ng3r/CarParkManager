package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.database.dao.VehicleDAO;
import com.devel.boaventura.carparkingmanager.model.Park;
//import com.devel.boaventura.carparkingmanager.model.Service;

import com.devel.boaventura.carparkingmanager.model.Vacancy;
import com.devel.boaventura.carparkingmanager.model.Vehicle;
import com.devel.boaventura.carparkingmanager.utils.TimeUtilities;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by scavenger on 4/19/18.
 */

public class EntryRegisterDialog extends DialogFragment {

    private TextView m_vacancyNumber, m_vacancyType, m_entryTime;
    private EditText m_plateChars, m_plateNumbers;
    private Vacancy m_currentVacancy;
    private long m_currentTime;

    public static final String ENTRY_REGISTER_VACANCY_INPUT = "ENTRY_REGISTER_VACANCY_INPUT";
    public static final String ENTRY_REGISTER_VACANCY_OUTPUT = "ENTRY_REGISTER_VACANCY_OUTPUT ";
    public static final String FRAGMENT_TAG = "EntryRegisterDialog";
    public static final int REQUEST_CODE_REGISTER = 0x01;
    private static final byte MAX_LETTERS = 3;
    private static final byte MAX_NUMBERS = 4;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_entry_register_layout, null);

        m_vacancyNumber = (TextView) dialogView.findViewById(R.id.EntryRegisterDialogVacancyNumber);
        m_vacancyType = (TextView) dialogView.findViewById(R.id.EntryRegisterDialogVacancyType);
        m_entryTime = (TextView) dialogView.findViewById(R.id.EntryRegisterDialogEntryTime);

        m_plateChars = (EditText) dialogView.findViewById(R.id.EntryRegisterDialogPlateChars);
        m_plateNumbers = (EditText) dialogView.findViewById(R.id.EntryRegisterDialogPlateNumbers);

        m_currentVacancy = getArguments().getParcelable(ENTRY_REGISTER_VACANCY_INPUT);

        String typeString = ( m_currentVacancy.getType() == Vacancy.TYPE_ROTARY)
                ?  getString(R.string.rotary) : getString(R.string._private);


        m_vacancyNumber.setText( getString(R.string.vacancy) + " " + m_currentVacancy.getNumber() );
        m_vacancyType.setText(typeString);

        DateFormat format = TimeUtilities.getDateFormat( TimeUtilities.FORMAT_DATE_AND_TIME);
        m_currentTime = System.currentTimeMillis();

        m_entryTime.setText(format.format( new Date(m_currentTime) ));

        // building a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(dialogView).
                setTitle(R.string.entry_register_dialog_title).
                setPositiveButton(R.string.confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                    Park park = getData();
                                    Intent dataIntent = new Intent();
                                    dataIntent.putExtra(ENTRY_REGISTER_VACANCY_OUTPUT, park);
                                    getTargetFragment().onActivityResult(REQUEST_CODE_REGISTER,
                                            Activity.RESULT_OK, dataIntent);
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(REQUEST_CODE_REGISTER,
                                Activity.RESULT_CANCELED, null);
                        EntryRegisterDialog.this.dismiss();
                    }
                });

        initTextWatchers();

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    /**
     *
     * Obtem os dados dos campos da dialog de resgitro de entrada
     * LEtras & numeros correspondes a placa do veiculo, verifica se
     * sao validos.
     *
     * @return null se album dado de entrada eh invalido,
     * caso contrario retorna um service valido.
     */
    private Park getData(){
        Park parking = null;
        String letters = m_plateChars.getText().toString();
        String numbers = m_plateNumbers.getText().toString();

        if ( (!letters.isEmpty()) && (!numbers.isEmpty()) &&
                ((letters.length() == MAX_LETTERS)  && (numbers.length() == MAX_NUMBERS) ) ){

            parking = new Park();
            parking.setType(Park.SERVICE_ENTRY);
            parking.setTime(m_currentTime);
            Vehicle vehicle = new Vehicle(letters.toUpperCase() + "-" + numbers );
            vehicle.setId( VehicleDAO.getInstance().addVehicle(vehicle) );
            parking.setVehicle( vehicle );
        }

        return parking;
    }

    private void initTextWatchers(){
        m_plateChars.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( (count == MAX_LETTERS) && before == (MAX_LETTERS - 1) )
                    m_plateNumbers.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        m_plateNumbers.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( (keyCode == KeyEvent.KEYCODE_DEL)
                        && (m_plateNumbers.getText().length() == 0) ){

                    m_plateChars.requestFocus();

                }
                return false;
            }
        });
    }
}
