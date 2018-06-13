package com.devel.boaventura.carparkingmanager.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.database.dao.ParkPerfilDAO;
import com.devel.boaventura.carparkingmanager.model.ParkPerfil;
import com.devel.boaventura.carparkingmanager.view.dialog.GenericPerfilDialog;

import java.util.ArrayList;

public class PerfilSettingsActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener,
    GenericPerfilDialog.GenericPerfilDialogListener {

    private ListView m_listView;
    private String[] m_options;
    private ArrayAdapter<String> m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_settings_layout);

        m_listView = findViewById(R.id.PerfilSettingsActivityListView);
        m_listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);


        m_options = getResources()
                .getStringArray(R.array.perfil_settings_activity_options);

        m_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, m_options);

        m_listView.setAdapter(m_adapter);
        m_listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GenericPerfilDialog dialog = null;
        Log.i("DBG", "PerfilSettingsActivity Click at " + position);
        switch (position){
            case 0: // create New perfil
                dialog = GenericPerfilDialog.getDialog(m_options[0],
                        R.layout.dialog_create_perfil_layout, GenericPerfilDialog.DIALOG_TYPE_NEW_PERFIL);
                dialog.setGenericPerfilDialogListener(this);

                dialog.show( getSupportFragmentManager() , GenericPerfilDialog.FRAGMENT_TAG);
                break;


            case 1:// select a perfil
                dialog = GenericPerfilDialog.getDialog(m_options[1],
                        R.layout.dialog_select_perfil_layout, GenericPerfilDialog.DIALOG_TYPE_SELECT_PERFIL);
                dialog.setGenericPerfilDialogListener(this);
                dialog.show( getSupportFragmentManager() , GenericPerfilDialog.FRAGMENT_TAG);
                break;

            case 2: // update some perfil
                dialog = GenericPerfilDialog.getDialog(m_options[2],
                        R.layout.dialog_update_perfil_layout,
                        GenericPerfilDialog.DIALOG_TYPE_UPDATE_PERFIL);
                dialog.setGenericPerfilDialogListener(this);
                dialog.show( getSupportFragmentManager() , GenericPerfilDialog.FRAGMENT_TAG);
                break;
            /*TALVEZ SEJA NECESSARIO SOMENTE 1 PERFIL!*/

            case 3: //remove perfil

                break;
        }

    }

    @Override
    public void onConfirmClicked(ParkPerfil perfil, int type) {
        if (perfil == null){
            Log.i("DBG", "PERFIL NULL");
            return;
        }

        ParkSettingsManager pManager = ParkSettingsManager.getInstance();
        switch (type){

            case GenericPerfilDialog.DIALOG_TYPE_NEW_PERFIL:

                Log.i("DBG", "REgistrando perfil" + perfil.getPerfilName());
                    ParkPerfilDAO dao = ParkPerfilDAO.getInstance();
                    long id = dao.addPerfil(perfil);

                    perfil.setId(id);
                break;

            case GenericPerfilDialog.DIALOG_TYPE_SELECT_PERFIL:
                    //pManager.setPerfil(perfil);
                break;

            case GenericPerfilDialog.DIALOG_TYPE_UPDATE_PERFIL:
                break;
        }
    }

    @Override
    public void onCancelClicked() {

    }
}
