package com.devel.boaventura.carparkingmanager.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.devel.boaventura.carparkingmanager.R;
import com.devel.boaventura.carparkingmanager.controler.ParkSettingsManager;
import com.devel.boaventura.carparkingmanager.model.Tax;

import org.w3c.dom.Text;

/**
 * Created by scavenger on 4/30/18.
 */

public class TaxDetailsDialog extends DialogFragment {

    private TextView m_until1h, m_from1hTo4h, m_from4To8h, m_after8h;
    public static final String FRAGMENT_TAG= "TaxDetailsDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_tax_details_layout, null);

        m_until1h = (TextView) view.findViewById(R.id.DialogTaxDetailsUntil1h);
        m_from1hTo4h = (TextView) view.findViewById(R.id.DialogTaxDetailsFrom1hTo4h);
        m_from4To8h = (TextView) view.findViewById(R.id.DialogTaxDetailsFrom4hTo8H);
        m_after8h = (TextView) view.findViewById(R.id.DialogTaxDetailsAfter8h);


        builder.setTitle("Taxa Detalhes")
                .setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     dismiss();
                    }
                });
        Tax current = ParkSettingsManager.getInstance().getTax();

        m_until1h.setText(getString(R.string.until_1h) + " " + current.getUntil1H() + " R$");
        m_from1hTo4h.setText(getString(R.string.from_1h_to_4h) + " " + current.getFrom1hTo4h() + " R$");
        m_from4To8h.setText(getString(R.string.from_4h_to_8h) + " " + current.getFrom4hTo8h() + " R$");
        m_after8h.setText(getString(R.string.after_8h) + " " + current.getAfter8h());

        return builder.create();
    }
}
