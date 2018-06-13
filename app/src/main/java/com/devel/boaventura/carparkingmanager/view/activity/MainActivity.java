package com.devel.boaventura.carparkingmanager.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.database.DataBaseManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton m_financialButton,
            m_parkingButton, m_settingsButton, m_clientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataBaseManager.start( this.getApplicationContext());

        setContentView(R.layout.activity_main_layout);

        m_financialButton = (ImageButton) findViewById(R.id.MainActivityFinancialButton);
        m_settingsButton = (ImageButton) findViewById(R.id.MainActivitySettingButton);// settingsButton
        m_clientButton = (ImageButton) findViewById(R.id.MainActivityClientButton);
        m_parkingButton = (ImageButton) findViewById(R.id.MainActivityParkingButton);

        m_financialButton.setOnClickListener(this);
        m_settingsButton.setOnClickListener(this);
        m_parkingButton.setOnClickListener(this);
        m_clientButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent it;
        switch (v.getId()) {
            case R.id.MainActivityFinancialButton:
                Toast.makeText(MainActivity.this, "Não implementado!", Toast.LENGTH_SHORT)
                        .show();
                break;

            case R.id.MainActivitySettingButton:
                it = new Intent(this, PerfilSettingsActivity.class);
                startActivity(it);
                break;

            case R.id.MainActivityParkingButton:
                it = new Intent(this, ParkWorkFlowActivity.class);
                startActivity(it);
                break;
            case R.id.MainActivityClientButton:
                it = new Intent(this, ClientSettingsActivity.class);
                startActivity(it);
                break;

        }
    }
}
