package com.devel.boaventura.carparkingmanager.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.VacancyRecyclerViewAdapter;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.database.dao.ParkDAO;
import com.devel.boaventura.carparkingmanager.database.dao.RentingDAO;
import com.devel.boaventura.carparkingmanager.database.dao.VacancyDAO;
import com.devel.boaventura.carparkingmanager.model.Client;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.ParkPerfil;
import com.devel.boaventura.carparkingmanager.model.Renting;
import com.devel.boaventura.carparkingmanager.model.Vacancy;
import com.devel.boaventura.carparkingmanager.view.dialog.DialogVacancyDetails;
import com.devel.boaventura.carparkingmanager.view.dialog.EntryRegisterDialog;
import com.devel.boaventura.carparkingmanager.view.dialog.ExitRegisterDialog;
import com.devel.boaventura.carparkingmanager.view.dialog.RentingClientSelectorDialog;

import java.util.ArrayList;

/**
 * Created by scavenger on 5/5/18.
 */

public class WorkFlowFragment extends Fragment implements
        VacancyRecyclerViewAdapter.OnParkingItemClickListener,
        ExitRegisterDialog.onExitRegisterListener,
        RentingClientSelectorDialog.RentingClientClickListener {

    private RecyclerView m_recyclerView;
    private ParkPerfil m_currentPerfil;
    private ArrayList<Vacancy> m_vacancyList;
    private int m_itemClickPosition;
    private Vacancy m_selectedVacancy;

    public static final String FRAGMENT_TAG = "WorkFlowFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("FMG", "WorkFlowFragment::onAttach()");
        /*Metodo que o fragment eh anexado a activity e antes que a view seja criada*/
        m_currentPerfil = ParkSettingsManager.getInstance().getPerfil();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FMG", "WorkFlowFragment::onCreate()");

        if (savedInstanceState !=null){

        }

        if (m_currentPerfil != null)
            m_vacancyList = m_currentPerfil.getVacancyList();

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("FMG", "WorkFlowFragment::onCreateView()");
        View layout = inflater.inflate(R.layout.fragment_work_flow_layout, null);

        m_recyclerView = (RecyclerView) layout.findViewById(R.id.WorkFlowFragmentRecyclerView);
        m_recyclerView.setHasFixedSize(true);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("FMG", "WorkFlowFragment::onActivityCreated()");

        RecyclerView.LayoutManager manager = new GridLayoutManager(
                getContext(), 3);
        m_recyclerView.setLayoutManager(manager);

        VacancyRecyclerViewAdapter adapter = new VacancyRecyclerViewAdapter(
                getContext(), m_vacancyList);

        adapter.setParkingItemClickListener(this);
        m_recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        registerForContextMenu(m_recyclerView);
        super.onResume();
        Log.i("FMG", "WorkFlowFragment::onResume()");
    }

    @Override
    public void onPause() {
        unregisterForContextMenu(m_recyclerView);
        super.onPause();
        Log.i("FMG", "WorkFlowFragment::onPause()");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i("FMG", "WorkFlowFragment::onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("FMG", "WorkFlowFragment::onDestroy()");
    }


    public void search(String query){
        VacancyRecyclerViewAdapter adapter = (VacancyRecyclerViewAdapter) m_recyclerView.getAdapter();
        adapter.getFilter().filter(query);
    }

    /*TRATAMENTO DOS EVENTOS DO MENU DE CONTEXTO PARA ESSE FRAGMENT!*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i("FMG", "WorkFlowFragment::onContextItemSelected()");
        VacancyRecyclerViewAdapter adapter = (VacancyRecyclerViewAdapter) m_recyclerView.getAdapter();
        int selectedPosition = adapter.getSelectedPosition();

        //m_selectedVacancy = m_vacancyList.get(selectedPosition);
        m_selectedVacancy = ParkSettingsManager.getInstance().getPerfil()
                .getVacancyList().get(selectedPosition);

        Log.i("DBG", "LONG POSITION: " + selectedPosition
                + " VACANCY NUMBER: " + m_selectedVacancy.getNumber());

        switch (item.getItemId()) {
            case 1: //change vacancy | MUDANDO O TIPO DA VAGA!

                if (m_selectedVacancy.getType() == Vacancy.TYPE_PRIVATE) {
                    //Finalizando aluguel de uma vaga!
                    Client client = m_selectedVacancy.getClient();
                    Log.i("DBG", "LIBERANDO " + m_selectedVacancy.getNumber()
                            + " owner: " + client.getName() + " ID: " + client.getId());

                    Renting currentRenting = RentingDAO.getInstance()
                            .getRentingByVacancyIdandState(m_selectedVacancy.getId()
                                    , Renting.STATE_VALID);

                    currentRenting.setClient(client);
                    currentRenting.setState(Renting.STATE_EXPIRED);
                    m_selectedVacancy.setType(Vacancy.TYPE_ROTARY);
                    m_selectedVacancy.setClient(null);
                    VacancyDAO.getInstance().update(m_selectedVacancy);
                    RentingDAO.getInstance().update(currentRenting);
                    m_recyclerView.getAdapter().notifyDataSetChanged();

                }
                else { // ALUGANDO UMA VAGA
                    RentingClientSelectorDialog dialog = new RentingClientSelectorDialog();
                    dialog.show(getFragmentManager(),
                            RentingClientSelectorDialog.FRAGMENT_TAG);

                    dialog.setRentingClientClickListener(this);
                    /*
                    * Mudacas tratadas nas callback de
                    * RetingClickSelectorDialog
                    */
                }
                break;

            case 2: // show details
                DialogVacancyDetails dialog = new DialogVacancyDetails();
                Bundle args = new Bundle();
                args.putParcelable(DialogVacancyDetails.PARAM, m_selectedVacancy);

                dialog.setArguments( args);
                dialog.show(getFragmentManager(), DialogVacancyDetails.FRAGMENT_TAG);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /*interface do adapter do recyclerView para escutar eventos de click!*/
    @Override
    public void onParkingItemClicked(Vacancy selectedVacancy, int position) {
        Bundle args = new Bundle();
        m_itemClickPosition = position;
        m_selectedVacancy = selectedVacancy;
        Log.i("DBG", "VACANCY NUMBER CLICKED: " + selectedVacancy.getNumber());

        /*
        * 1)VERIFICAR ESTADO DO VAGA DO PARKING
        *
        * 2) OCUPADA
        *       |-> 2.1) DIALOG DE REGISTRO DE SAIDA
        *
        * 3)LIVRE
        *     |-> DIALOG DE REGISTRO DE ENTRADA
        */
        switch (selectedVacancy.getStatus()) {

            case Vacancy.STATUS_AVAILABLE:
                EntryRegisterDialog d = new EntryRegisterDialog();
                args.putParcelable(EntryRegisterDialog.ENTRY_REGISTER_VACANCY_INPUT, selectedVacancy);
                d.setArguments(args);

                d.setTargetFragment(this, EntryRegisterDialog.REQUEST_CODE_REGISTER);
                d.show(getActivity().getSupportFragmentManager(),
                        EntryRegisterDialog.FRAGMENT_TAG);
                break;

            case Vacancy.STATUS_UNAVAILABLE:
                ExitRegisterDialog dialog = new ExitRegisterDialog();
                args.putParcelable(ExitRegisterDialog.ARG, selectedVacancy);
                dialog.setArguments(args);

                dialog.setOnExitRegisterListener( this );

                dialog.show(getActivity().getSupportFragmentManager(),
                        ExitRegisterDialog.FRAGMENT_TAG);
                break;

        }
    }



    @Override
    public void onExitRegisterConfirm(Park item) {
        item.setVacancy(m_selectedVacancy);
        long id = ParkDAO.getInstance().addPark( item );
        item.setId( id );

        m_selectedVacancy.addService(item);
        VacancyDAO.getInstance().update(m_selectedVacancy);
        item.getVacancy().setStatus(Vacancy.STATUS_AVAILABLE);
        m_recyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onExitRegisterCancel() {

    }

    @Override
    public void onRentingClientConfirmClick(Client client) {
        if (client != null){
            Renting renting = new Renting();
            m_selectedVacancy.setType(Vacancy.TYPE_PRIVATE);
            m_selectedVacancy.setClient( client );

            renting.setClient(client);
            renting.setVacancy(m_selectedVacancy);
            renting.setValue(90.0);
            renting.setPayment(1); // esta pago!
            renting.setState(Renting.STATE_VALID);
            renting.setStartTime(System.currentTimeMillis());

            // o tempo final tem que olhar 1 mes adiante. usar a calsse Calendar
            client.addRenting(renting);
            renting.setId(RentingDAO.getInstance().addRenting(renting));
            VacancyDAO.getInstance().update(m_selectedVacancy);
            m_recyclerView.getAdapter().notifyDataSetChanged();

        }

        Log.i("DBG", "VAGA " + m_selectedVacancy.getNumber() +
                " VAI SER DE: " + client.getName() + " ID: " + client.getId());
    }

    @Override
    public void onRentingClientCancelClick() {

    }

    public void updatePerfil(ParkPerfil perfil){
        m_currentPerfil = perfil;
        m_vacancyList = perfil.getVacancyList();
        VacancyRecyclerViewAdapter adapter = new VacancyRecyclerViewAdapter(
                getContext(), m_vacancyList );
        adapter.setParkingItemClickListener(this);
        m_recyclerView.setAdapter(
                adapter
        );
        m_recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case EntryRegisterDialog.REQUEST_CODE_REGISTER:
                    Park parkItem = data.getParcelableExtra(EntryRegisterDialog.ENTRY_REGISTER_VACANCY_OUTPUT);
                    parkItem.setVacancy(m_selectedVacancy);
                    long id = ParkDAO.getInstance().addPark( parkItem );
                    parkItem.setId( id );
                    m_selectedVacancy.addService(parkItem);
                    VacancyDAO.getInstance().update(m_selectedVacancy);
                    m_recyclerView.getAdapter().notifyDataSetChanged();
                    break;
            }

        } else if (resultCode == Activity.RESULT_CANCELED){

            switch (requestCode){
                case EntryRegisterDialog.REQUEST_CODE_REGISTER:
                    Toast.makeText(getActivity(),
                            R.string.toast_invalid_input_data,
                            Toast.LENGTH_SHORT
                    ).show();
                    break;

                default:
                    break;
            }
        }


    }
}// enf of class!


