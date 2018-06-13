package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.database.dao.ParkPerfilDAO;
import com.devel.boaventura.carparkingmanager.model.ParkPerfil;
import com.devel.boaventura.carparkingmanager.model.Vacancy;

import java.util.List;


/**
 * Created by scavenger on 4/22/18.
 */

public class GenericPerfilDialog extends DialogFragment
        implements AdapterView.OnItemSelectedListener {

    private static final String ARG_TITLE = "TITLE";
    private static final String ARG_LAYOUT = "LAYOUT";
    private static final String ARG_TYPE = "TYPE";
    public static final String FRAGMENT_TAG = "GenericPerfilDialog";

    private GenericPerfilDialogListener m_listener;

    private int m_type;
    public static final int DIALOG_TYPE_NEW_PERFIL = 0x00;
    public static final int DIALOG_TYPE_SELECT_PERFIL = 0x01;
    public static final int DIALOG_TYPE_UPDATE_PERFIL = 0x02;

    /*Creation dialog*/
    private EditText m_perfilName, m_vacancyNumber;
    private CheckBox m_activatePerfil;

    /*selection dialog*/
    private Spinner m_perfilSelector;
    private ArrayAdapter<String> m_spinnerAdapter;
    private ParkPerfil m_selectedPerfil;
    List<String> m_perfilNames = null;
    //public enum Type { NEW_PERFIL, SELECT_PERFIL, UPDATE_PERFIL };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            m_listener = (GenericPerfilDialogListener) context;
        }catch (ClassCastException ex){
            throw new ClassCastException();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater infalter = getActivity().getLayoutInflater();

        Bundle args = getArguments();
        m_type = args.getInt(ARG_TYPE);

        View layout = infalter.inflate(args.getInt(ARG_LAYOUT), null);

        builder.setView(layout)
                .setTitle(args.getString(ARG_TITLE))

                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParkPerfil perfil = null;

                        switch (m_type) {

                            case DIALOG_TYPE_NEW_PERFIL:
                                String name = m_perfilName.getText().toString();
                                int vacancyNumber = -1;

                                try {
                                    vacancyNumber = Integer.parseInt(
                                            m_vacancyNumber.getText().toString());
                                } catch (Exception ex) {
                                    vacancyNumber = -1;
                                }

                                /*Aqui eh a validacao e criacao do perfil*/
                                if ( (!name.isEmpty()) && (vacancyNumber != -1) ) {
                                    perfil = new ParkPerfil();
                                    perfil.setPerfilName(name);
                                    for (int i = 1; i <= vacancyNumber; i++) {
                                        perfil.addVacancy(
                                                new Vacancy(String.valueOf(i),
                                                        Vacancy.TYPE_ROTARY,
                                                        Vacancy.STATUS_AVAILABLE));
                                    }
                                }

                                if (m_listener != null) {
                                    if (m_activatePerfil.isChecked()) {
                                        ParkPerfilDAO dao = ParkPerfilDAO.getInstance();
                                        long id = dao.addPerfil(perfil);
                                        perfil.setId(id);
                                        ParkSettingsManager.getInstance().setPerfil(perfil);
                                        Toast.makeText(getActivity(),
                                                R.string.toast_perfil_updated,
                                                Toast.LENGTH_SHORT).show();
                                        m_listener.onConfirmClicked(null, DIALOG_TYPE_NEW_PERFIL);
                                    }
                                    else
                                        m_listener.onConfirmClicked(perfil, DIALOG_TYPE_NEW_PERFIL);
                                }
                                break;

                            case DIALOG_TYPE_SELECT_PERFIL:
                                ParkSettingsManager.getInstance().setPerfil(m_selectedPerfil);
                                if (m_listener !=null)
                                    m_listener.onConfirmClicked(m_selectedPerfil, DIALOG_TYPE_SELECT_PERFIL);
                                break;

                            case DIALOG_TYPE_UPDATE_PERFIL:
                                /*PEGAR INFORMACOES DOS CAMPOS!*/
                                /*ATUALIZAR PERFIL*/
                                /*ENVIAR PERFIL*/
                                break;
                        }


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (m_listener != null)
                            m_listener.onCancelClicked();
                    }
                });


        /*pegando referencias aos componentes de acordo ao layout!*/
        switch (m_type) {

            case DIALOG_TYPE_NEW_PERFIL:
                m_perfilName = (EditText) layout.findViewById(R.id.GenericDialogPerfilName);
                m_vacancyNumber = (EditText) layout.findViewById(R.id.GenericDialogVacancyNumber);
                m_activatePerfil = (CheckBox) layout.findViewById(R.id.GenericDialogPerfilActivePerfil);
                break;

            case DIALOG_TYPE_SELECT_PERFIL:
                m_perfilSelector = layout.findViewById(R.id.GenericDialogPerfilSpinner);
                settingPerfilSpinner();
                break;


            case DIALOG_TYPE_UPDATE_PERFIL:
                m_perfilName = (EditText) layout.findViewById(R.id.GenericDialogPerfilName);
                m_vacancyNumber = (EditText) layout.findViewById(R.id.GenericDialogVacancyNumber);
                m_perfilSelector = layout.findViewById(R.id.GenericDialogPerfilSpinner);
                settingPerfilSpinner();
                break;
        }
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;

    }

    public void setGenericPerfilDialogListener(GenericPerfilDialogListener listener) {
        m_listener = listener;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String perfilName = m_perfilNames.get(position);
        m_selectedPerfil = ParkPerfilDAO.getInstance()
                .getPerfilByName(perfilName);

        Log.i("DBG", "GOT PERFIL ID: " + m_selectedPerfil.getId() + " " + m_selectedPerfil.getPerfilName());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface GenericPerfilDialogListener {
        void onConfirmClicked(ParkPerfil perfil, int type);

        void onCancelClicked();
    }

    private void settingPerfilSpinner() {
        m_perfilNames = ParkPerfilDAO.getInstance().getPerfilNameList();
        Log.i("DBG", "settingPerfilSpinner() " + m_perfilNames.size());
        m_spinnerAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                m_perfilNames
        );

        m_spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_perfilSelector.setOnItemSelectedListener(this);
        m_perfilSelector.setAdapter(m_spinnerAdapter);
    }

    /**
     * Método estático para a criação de uma dialog genérica
     * utilizada para operaçoes sob Perfil. (Criação, Seleção e atualização)
     *
     * @param title    String de titulo
     * @param layoutId Id do recurso de layout que será utilizado na dialog
     * @param type     Inteiro que representa o típo da dialog.
     *                 O valores são:
     *                 DIALOG_TYPE_NEW_PERFIL = 0x00,    <br />
     *                 DIALOG_TYPE_SELECT_PERFIL = 0x01, <br />
     *                 DIALOG_TYPE_UPDATE_PERFIL = 0x02;
     * @return retorna um instância da dialog. Retorna null caso o tipo seja desconhecido.
     */
    public static final GenericPerfilDialog getDialog(String title, int layoutId, int type) {
        if (type < 0 || type > 2)
            return null;

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_LAYOUT, layoutId);
        args.putInt(ARG_TYPE, type);

        GenericPerfilDialog dialog = new GenericPerfilDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_listener = null;
    }
}
