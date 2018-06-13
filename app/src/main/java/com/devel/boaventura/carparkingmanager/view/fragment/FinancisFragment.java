package com.devel.boaventura.carparkingmanager.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devel.boaventura.carparkingmanager.R;

import java.util.Calendar;

/**
 * Created by scavenger on 5/7/18.
 */

/**
 * TESTAR OS FRAGMENTS COM O SET RETAIN INSTANCE E SEM ELE.
 * VER CASO, MANTER A INSTANCIA, VER SE HA COMO RECUPERAR ESTA
 * ANTES DE CRIAR UM NOVO FRAGMENT NA HORA DE FAZER O REPLACE.
 *
 */
public class FinancisFragment extends Fragment {

    public static final String FRAGMENT_TAG = "FinancisFragment";
    private TextView m_txtView;
    private String m_data = "String de dados!";

    /**
     *
     * Funcoes do clico de vida estao na ordem!
     * @param context
     */


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //m_data = ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_financis_layout, null);
        m_txtView = view.findViewById(R.id.FinancisFragmentTextView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        m_txtView.setText(m_data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FMG", "FinancisFragment::onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("FMG", "FinancisFragment::onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("FMG", "FinancisFragment::onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("FMG", "FinancisFragment::onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
