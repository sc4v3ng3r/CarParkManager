package com.devel.boaventura.carparkingmanager.utils;

/**
 * Created by scavenger on 4/19/18.
 */

import android.util.Log;

//import com.devel.boaventura.carparkingmanager.model.Service;
import com.devel.boaventura.carparkingmanager.model.Park;
import com.devel.boaventura.carparkingmanager.model.Vacancy;
import com.devel.boaventura.carparkingmanager.model.Vehicle;

import java.util.Random;

/**
 *  INTERVALOS
 *  1 - 26 LETRAS MAIUSCULAS
 *
 * */
public class GeneratorClass {

    private static final char[] LETTERS = {'A','B', 'C','D', 'E',
    'F', 'G', 'H', 'I', 'J', 'K','L','M','N','O','P','Q','R',
            'S', 'T','U','V','W','X','Y','Z' };

    private static final char[] NUMBERS = {'1','2', '3','4',
            '5','6','7','8','9', '0'};

    private static final Random m_random = new Random();

    public static final String licensePlateGenerator(){
        String license = "";
        int code = 0;


        for (int i=0; i < 3; i++){
            code = m_random.nextInt(26);
            license+= LETTERS[code];
        }

        license+="-";

        for(int i =0; i< 4; i++){
            code = m_random.nextInt(10);
            license+= NUMBERS[code];
        }

        Log.i("DBG", "GENERATED: " + license);
        return license;

    }

    public static final int oneOrZero(){
        return m_random.nextInt(2);
    }

    public static final Vehicle getVehicle(){

        return new Vehicle( licensePlateGenerator() );
    }

    public static final Vacancy getVacancy(){
        int x = oneOrZero();
        int y = oneOrZero();
        Vacancy v;

        if (x == 0) {
            v = new Vacancy();
            v.setType(Vacancy.TYPE_ROTARY);
        }
        else {
            v = new Vacancy();
            v.setType(Vacancy.TYPE_PRIVATE);
        }

        if (y==1){

            v.setStatus(Vacancy.STATUS_UNAVAILABLE);
            Park service = new Park();
            service.setTime(System.currentTimeMillis());
            service.setType(Park.SERVICE_ENTRY);
            service.setVehicle(getVehicle());
            /*v.addService(new Service(
                    Service.SERVICE_ENTRY, System.currentTimeMillis(),
                    getVehicle()
            ));*/

        } else
            v.setStatus(Vacancy.STATUS_AVAILABLE);

        v.setNumber(String.valueOf( m_random.nextInt(101) ) );

        return v;
    }
}
