package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.Tax;
import com.devel.boaventura.carparkingmanager.model.Vacancy;
import com.devel.boaventura.carparkingmanager.utils.TimeUtilities;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by scavenger on 4/21/18.
 */

public class ExitRegisterDialog  extends DialogFragment {

    private Vacancy m_currentVacancy = null;
   // private Park m_exitService = null;
    private onExitRegisterListener m_listener = null;
    private TextView m_vacancyNumber, m_licensePlateNumber, m_vacancyType,
        m_entryTime, m_exitTime, m_totalTime, m_valueTotal;

    private long m_exitTimeMillis;
    private Tax m_currentTax = ParkSettingsManager.getInstance().getTax();
    public static final String ARG = "ENTRY_REGISTER_VACANCY_INPUT";
    public static final String FRAGMENT_TAG = "ExitRegisterDialog_TAG";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        m_currentVacancy = getArguments().getParcelable(ARG);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_exit_register_layout, null);

        m_vacancyNumber = (TextView) dialogView.findViewById(R.id.DialogVacancyDetailsVacancyNumber);
        m_vacancyType = (TextView) dialogView.findViewById(R.id.DialogVacancyDetailsVacancyType);
        m_licensePlateNumber = (TextView) dialogView.findViewById(R.id.ExitRegisterDialogLicensePlate);
        m_entryTime = (TextView) dialogView.findViewById(R.id.ExitRegisterDialogEntryTime);
        m_exitTime = (TextView) dialogView.findViewById(R.id.ExitRegisterDialogExitTime);
        m_totalTime = (TextView) dialogView.findViewById(R.id.ExitRegisterDialogTotalTime);
        m_valueTotal = (TextView) dialogView.findViewById(R.id.ExitRegisterDialogTotalValue);

        m_vacancyNumber.setText( m_currentVacancy.getNumber() );

        m_vacancyType.setText( (m_currentVacancy.getType() == Vacancy.TYPE_PRIVATE) ?
                getString(R.string._private) : getString(R.string.rotary) );

        m_licensePlateNumber.setText(m_currentVacancy.getCurrentService().getVehicle().getLicensePlate() );


        DateFormat fmt = TimeUtilities.getDateFormat(TimeUtilities.FORMAT_DATE_AND_TIME);
        long entryTime = m_currentVacancy.getCurrentService().getTime();
        Date date = new Date( entryTime );
        m_entryTime.setText( fmt.format( date ) );

        m_exitTimeMillis = System.currentTimeMillis();
        date.setTime(m_exitTimeMillis);
        m_exitTime.setText( fmt.format( date ) );

        TimeUtilities.ElapsedTime time = new TimeUtilities.ElapsedTime(entryTime, m_exitTimeMillis);

        if (time.getDays() > 0){
            /*Calcular valor com base em diaria!*/
            /*Existem Dias, Hora minutos & segundos*/
            m_totalTime.setText(time.getDays() + " " + getString(R.string.day)
                    + " " + time.getHours() + getString(R.string.hour)
                    + " " + time.getMinutes() + getString(R.string.minutes));
        } else {
            /*Calcular valor com base na hora!*/
            m_totalTime.setText(time.getHours() + getString(R.string.hour)
                    + " " + time.getMinutes() + getString(R.string.minutes));
        }

        // calcular valor!
        final double value = calcValue(time);

        m_valueTotal.setText( String.format("%.2f", value) + " R$");


        builder.setView(dialogView)
                .setTitle(R.string.exit_register_dialog_title)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (m_listener!= null){

                            Park exitService = new Park();
                            exitService.setType(Park.SERVICE_EXIT);
                            exitService.setValue(value);
                            exitService.setTime(m_exitTimeMillis);
                            exitService.setVehicle(m_currentVacancy.getCurrentService().getVehicle());
                            exitService.setVacancy(m_currentVacancy);
                            m_currentVacancy.addService(exitService);
                            m_listener.onExitRegisterConfirm(exitService);

                        }
                    }
                });

        return builder.create();

    }

    private double calcValue(TimeUtilities.ElapsedTime time){
        /*Eh melhor fazer o calculo por minutos*/

        if (m_currentVacancy.getType() == Vacancy.TYPE_PRIVATE){
            return 0;
        }

        double value = 3.50;
        double daily = 36; // preco da diaria!

        long hours =0;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time.getTimeDiference());
        Log.i("DBG", "TOTAL DE PASSADOS Minutos: " + minutes);

        if (minutes <=60){
            return m_currentTax.getUntil1H();
            //return (value);
        }
        /*DE 1H ATE 4 HORAS*/
        if ( (minutes > 60) && (minutes <= 4*60) ){
            hours = (minutes / 60);
            //value = hours * 2.50;
            value = hours * m_currentTax.getFrom1hTo4h();
        }

        /*De 4H ate 8 H*/
        else if ( (minutes > 4*60) && (minutes <= 8*60) ){
            hours = (minutes / 60);
            //value = hours * 3.0;
            value = hours * m_currentTax.getFrom4hTo8h();
        }
        else { /*Aqui a gente deve fazer calculo de diaria.
                ainda nao estao implementados, entao retornamos um valor FIXO!
                **/
            value = m_currentTax.getAfter8h();
        }
        return value;
    }

    public void setOnExitRegisterListener(onExitRegisterListener listener){
        m_listener = listener;
    }

    public interface onExitRegisterListener{
        void onExitRegisterConfirm(Park finalService);
        void onExitRegisterCancel();
    }
}
