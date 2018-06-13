package com.devel.boaventura.carparkingmanager.controler;

import android.util.Log;

import com.devel.boaventura.carparkingmanager.database.dao.ParkPerfilDAO;
import com.devel.boaventura.carparkingmanager.database.dao.SettingsDAO;
import com.devel.boaventura.carparkingmanager.database.dao.TaxDAO;
import com.devel.boaventura.carparkingmanager.model.ParkPerfil;
import com.devel.boaventura.carparkingmanager.model.Tax;

/**
 * Created by scavenger on 4/21/18.
 */

public class ParkSettingsManager {
    private static ParkSettingsManager m_instance = null;
    public static final long ID = 1;

    private ParkPerfil m_currentPerfil = null;
    private Tax m_currentTax = null;
    /*Talvez a lista de perfil nao precisa para realizar o necessario.
    * Pois so de ter varias taxas em um mesmo perfil, seja necessario somente
    * gerencias quais taxas entrarao em uso nas configuracoes do perfil.*/

    private ParkSettingsManager(){
        long lastPerfilId = SettingsDAO.getInstance().getLastPerfilId();
        long lastTaxId = SettingsDAO.getInstance().getLastTaxId();

        if ( (lastPerfilId == -1) || (lastPerfilId == -1) ){
            SettingsDAO configs = SettingsDAO.getInstance();
            configs.createEntry();
            ParkPerfil perfil = configs.createDefaultPerfilEntry();
            Tax tax = configs.createDefaultTaxesEntry();

            setPerfil(perfil);
            setTax(tax);

        } else {
            setPerfil( ParkPerfilDAO.getInstance().getPerfilById(lastPerfilId) );
            setTax( TaxDAO.getInstance().getTax(lastTaxId) );
        }

    }

    public static synchronized final ParkSettingsManager getInstance(){
        if (m_instance == null)
            m_instance = new ParkSettingsManager();
        return m_instance;
    }

    public void setPerfil(ParkPerfil perfil){
        m_currentPerfil = perfil;
        Log.i("DBG", "last perfil ID and NAme: " +
            m_currentPerfil.getId() + " " + m_currentPerfil.getPerfilName());

        SettingsDAO.getInstance().updateLastPerfil(m_currentPerfil);
    }

    /**
     * metodo que recuperar o ParkPerfil atual
     * em uso.
     * @return Retorna o ParkPerfil atual
     */
    public ParkPerfil getPerfil(){

        return  m_currentPerfil;
    }

    public Tax getTax(){
        return m_currentTax;
    }

    public void setTax(Tax tax){
        m_currentTax = tax;
        SettingsDAO.getInstance().updateLastTax(m_currentTax);
    }

}
