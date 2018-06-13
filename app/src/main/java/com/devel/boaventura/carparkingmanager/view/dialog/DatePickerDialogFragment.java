package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.DatePickerDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by scavenger on 5/7/18.
 */

public class DatePickerDialogFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialogFragmentListener m_listener;
    public static final String FRAGMENT_TAG = "DatePickerDialogFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        return new DatePickerDialog(getActivity(),
                this, calendar.get(Calendar.YEAR) ,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.i("DBG", "Data: " + dayOfMonth + " Mes: " + month + " Ano: " + year);

        Calendar currentDay = new GregorianCalendar(year, month, dayOfMonth,
                23,59,59);

        Calendar yesterday = (GregorianCalendar) currentDay.clone();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);

        /*Calendar tomorrow =  (GregorianCalendar) currentDay.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        */
        if (m_listener != null) {
            //Date date = new Date(year, month, dayOfMonth);
            m_listener.dateSelected( currentDay.getTimeInMillis(), yesterday.getTimeInMillis());

        }
    }

    public interface DatePickerDialogFragmentListener {
        void dateSelected(long currentDay, long yesterday);
    }

    public void setDatePickerDialogFragmentListener(
            DatePickerDialogFragmentListener listener){

        m_listener = listener;
    }
}
