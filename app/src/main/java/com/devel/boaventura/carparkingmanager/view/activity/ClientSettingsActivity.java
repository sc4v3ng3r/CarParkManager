package com.devel.boaventura.carparkingmanager.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.view.dialog.ClientRegisterDialog;

public class ClientSettingsActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private ListView m_listView;
    private ArrayAdapter<String> m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_settings_layout);

        m_listView = findViewById(R.id.ClientSettingsActivityListView);

        String[] options = getResources().getStringArray(R.array.client_settings_activity_options);

        m_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, options);
        m_listView.setOnItemClickListener(this);
        m_listView.setAdapter(m_adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position){
            case 0: //cadastrar
                ClientRegisterDialog dialog = new ClientRegisterDialog();
                dialog.show(getSupportFragmentManager(), ClientRegisterDialog.FRAGMENT_TAG);
                Log.i("DBG", "CADASTRAR CLIENTE");
                break;
            case 1: // editar
                break;
            case 2: //deletar
                break;
        }
    }
}
