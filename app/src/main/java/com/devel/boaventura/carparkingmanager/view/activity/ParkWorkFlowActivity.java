package com.devel.boaventura.carparkingmanager.view.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.model.ParkPerfil;
import com.devel.boaventura.carparkingmanager.model.Tax;
import com.devel.boaventura.carparkingmanager.view.dialog.ClientRegisterDialog;
import com.devel.boaventura.carparkingmanager.view.dialog.CreateTaxDialog;
import com.devel.boaventura.carparkingmanager.view.dialog.DialogTotalBalance;
import com.devel.boaventura.carparkingmanager.view.dialog.GenericPerfilDialog;
import com.devel.boaventura.carparkingmanager.view.dialog.SelectTaxDialog;
import com.devel.boaventura.carparkingmanager.view.dialog.TaxDetailsDialog;
import com.devel.boaventura.carparkingmanager.view.fragment.FinancisFragment;
import com.devel.boaventura.carparkingmanager.view.fragment.WorkFlowFragment;

public class ParkWorkFlowActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GenericPerfilDialog.GenericPerfilDialogListener,
        SelectTaxDialog.SelectTaxDialogClickListener,
        SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener {

    private ParkPerfil m_currentPerfil = null;
    private TextView m_perfilNameView;
    private WorkFlowFragment m_workFlowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_work_flow_layout);

        m_currentPerfil = ParkSettingsManager.getInstance().getPerfil();

        if (savedInstanceState == null) {
            Log.i("DBG", "WORFLOWActivity::perfil: " + m_currentPerfil.getPerfilName()
                    + " TOTAL VAGAS: " + m_currentPerfil.getVacancyList().size());
            m_workFlowFragment = new WorkFlowFragment();

            FragmentManager fm = getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.ParkWorkFlowActivityFragmentContainer,
                    m_workFlowFragment, WorkFlowFragment.FRAGMENT_TAG);
            ft.commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toast.makeText(this, getString(R.string.current_perfil) + " "
                + m_currentPerfil.getPerfilName() +
                "\n" + getString(R.string.current_tax) + " " +
                ParkSettingsManager.getInstance().getTax().getName(), Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        //Log.i("DBG", "FINALIZAR DIA DE TRABALHO??");
    }

    /*Menu da toolbar*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_parking_work_flow, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString( R.string.action_search) );

        searchItem.setOnActionExpandListener(this);
        m_perfilNameView = (TextView) findViewById(R.id.NavHeaderTextView);
        m_perfilNameView.setText( getString(
                R.string.current_perfil) + " " + m_currentPerfil.getPerfilName() );
        return true;
    }

    /*Listener de menu da toolbar*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId() ){
            case R.id.action_settings:
                break;
            case R.id.action_search:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    /*DraweMenu listener*/
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()){
            case R.id.nav_perfil_change:
                GenericPerfilDialog dialog = GenericPerfilDialog.getDialog(
                        getResources().getStringArray(R.array.perfil_settings_activity_options)[1],
                        R.layout.dialog_select_perfil_layout,
                        GenericPerfilDialog.DIALOG_TYPE_SELECT_PERFIL
                );
                dialog.setGenericPerfilDialogListener(this);
                dialog.show(getSupportFragmentManager(), GenericPerfilDialog.FRAGMENT_TAG);
                break;

            case R.id.nav_new_client:
                ClientRegisterDialog clientRegisterDialog = new ClientRegisterDialog();
                clientRegisterDialog.show(getSupportFragmentManager(),
                        ClientRegisterDialog.FRAGMENT_TAG);
                break;

            case R.id.nav_new_tax:
                CreateTaxDialog taxDialog = new CreateTaxDialog();
                taxDialog.show(getSupportFragmentManager(),
                        CreateTaxDialog.FRAGMENT_TAG);
                break;

            case R.id.nav_select_tax:
                SelectTaxDialog selectTaxDialog = new SelectTaxDialog();
                selectTaxDialog.show(getSupportFragmentManager(),
                        SelectTaxDialog.FRAGMENT_TAG);
                break;
            
            case R.id.nav_total_balance:
                DialogTotalBalance totalDialog = new DialogTotalBalance();

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                totalDialog.show(getSupportFragmentManager(), DialogTotalBalance.FRAGMENT_TAG);
                /*
                Fragment current = fm.findFragmentByTag(FinancisFragment.FRAGMENT_TAG);
                if ( current == null || !current.getClass()
                        .equals( FinancisFragment.class )){

                    Log.i("DBG", "FRAGMENT FINANCIS NOT IN BACK STACK!");
                    FinancisFragment financisFragment = new FinancisFragment();

                    ft.replace(R.id.ParkWorkFlowActivityFragmentContainer,
                            financisFragment, FinancisFragment.FRAGMENT_TAG);

                    ft.addToBackStack(FinancisFragment.FRAGMENT_TAG);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }*/

                break;

            case R.id.nav_show_tax:
                TaxDetailsDialog taxDetailsDialog = new TaxDetailsDialog();
                taxDetailsDialog.show(getSupportFragmentManager(),
                        TaxDetailsDialog.FRAGMENT_TAG);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    /* GENERIC PErfil dialog CALLBACK*/
    @Override
    public void onConfirmClicked(ParkPerfil perfil, int type) {

        switch (type){
            case GenericPerfilDialog.DIALOG_TYPE_SELECT_PERFIL:
                if (perfil != null ) {
                    m_currentPerfil = perfil;
                    m_workFlowFragment.updatePerfil(perfil);
                    m_perfilNameView.setText( getString(
                            R.string.current_perfil) + " " +
                            perfil.getPerfilName() );
                }
                break;

        }

    }


    @Override
    public void onCancelClicked() {
        /* GENERIC PErfil dialog CALLBACK*/

    }

    /*ESSA DIALOG ABRE COM O CLICK NO MENU DRAWER, ENTAO O SEU TRATAMENTO EH AQUI!*/
    @Override
    public void selectTaxDialogConfirmClicked(Tax tax) {
        if (tax != null) {
            ParkSettingsManager.getInstance().setTax(tax);
        }
    }

    @Override
    public void selectTaxDialogCancelClicked() {

    }

    /*Callbacks para o SearchWidget da toolbar*/
    @Override
    public boolean onQueryTextSubmit(String query) {
        m_workFlowFragment.search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.i("DBG", "Typed to search: " + query);
        m_workFlowFragment.search(query);
        return false;
    }
    /*===============================================*/


    /*Callback para onACtionExpanderListener*/
    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        Log.i("DBG", "Search item expanded!");
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        Log.i("DBG", "Search item collapse");
        return true;
    }

}
