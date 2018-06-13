package com.devel.boaventura.carparkingmanager.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.database.dao.ParkDAO;
import com.devel.boaventura.carparkingmanager.database.dao.RentingDAO;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.Renting;
import com.devel.boaventura.carparkingmanager.model.Vacancy;
import com.devel.boaventura.carparkingmanager.utils.TimeUtilities;
import com.devel.boaventura.carparkingmanager.view.dialog.DatePickerDialogFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class DayFinancyActivity extends AppCompatActivity
    implements OnClickListener, DatePickerDialogFragment.DatePickerDialogFragmentListener {

    private Button m_dateButton;
    private TextView m_date, m_rotaryTotal, m_privateTotal, m_mainTotal;
    ArrayList<Park> m_parkList;
    private double m_totalRotary =0;
    private double m_totalPrivate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_financy_layout);

        m_dateButton = (Button) findViewById(R.id.DayFinancyActivityPickDate);
        m_date = (TextView) findViewById(R.id.DayFinancyActivityCurrentDate);
        m_rotaryTotal = (TextView) findViewById(R.id.DayFinancyActivityRotaryTotal);
        m_privateTotal = (TextView) findViewById(R.id.DayFinancyActivityRentingTotal);
        m_mainTotal = (TextView) findViewById(R.id.DayFinancyActivityTotalGeneral);
        m_dateButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DayFinancyActivityPickDate:
                DatePickerDialogFragment dialog = new DatePickerDialogFragment();
                dialog.setDatePickerDialogFragmentListener(this);
                dialog.show(getSupportFragmentManager(),
                        DatePickerDialogFragment.FRAGMENT_TAG);
                break;
        }

    }

    /**
     *
     *
     *                      ARMENGUE PURO!
     * TEM QUE MUDAR AQUI, OS DAO's utilizados e o banco de dados.
     *
     * Mudar a maneira de armazenar a data e hora no banco de dados
     * alterar o metodo de consulta por data NO DAO
     * moficar o metodo desta classe que utiliza os DAO's p/ realizar consulta!
     * @param today
     * @param yesterday
     */
    @Override
    public void dateSelected(long today, long yesterday) {
        m_parkList = ParkDAO.getInstance().getParkExitListByDate(today, yesterday);

        ArrayList<Renting> rentingList =
                RentingDAO.getInstance().getRentingListBetweenTime(today, yesterday);

        if ( m_parkList.isEmpty() ) {
            m_rotaryTotal.setText("Vagas Rotativas: ");
            m_privateTotal.setText("Aluguéis: ");
            m_mainTotal.setText("Faturamento Diário total: ");

            return;
        }


        for(Park park: m_parkList){
            m_totalRotary+= park.getValue();
        }


        for(Renting r: rentingList){
            m_totalPrivate+= r.getValue();
        }

        Date date = new Date(today);
        java.text.DateFormat fmt = TimeUtilities.getDateFormat(TimeUtilities.FORMAT_DAY_MONTH_YEAR);

        m_date.setText("Data: "  + fmt.format( date ));
        m_rotaryTotal.setText("Total rotativas: " +
                String.format("%.2f", m_totalRotary) + " R$");
        m_privateTotal.setText("Total Aluguéis: " +
                String.format("%.2f", m_totalPrivate) + " R$");

        m_mainTotal.setText("Faturamento Diário total: " +
                String.format("%.2f", (m_totalRotary + m_totalPrivate)) + " R$");
    }
}
